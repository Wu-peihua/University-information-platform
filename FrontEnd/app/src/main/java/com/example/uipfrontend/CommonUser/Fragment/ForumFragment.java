package com.example.uipfrontend.CommonUser.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Activity.PostDetailActivity;
import com.example.uipfrontend.CommonUser.Activity.WritePostActivity;
import com.example.uipfrontend.CommonUser.Adapter.ForumListRecyclerViewAdapter;
import com.example.uipfrontend.Entity.ForumPosts;
import com.example.uipfrontend.Entity.ResponsePosts;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.LoginAndRegist.LoginActivity;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Utils.KeyboardStateObserver;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForumFragment extends Fragment {

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;
    private static final int NOTHING = 2;
    
    private static final int PAGE_SIZE = 10; // 默认一次请求10条数据
    private int CUR_PAGE_NUM = 1;
    
    private static final int myRequestCode = 233;
    private static final int myResultCode1 = 1; // 新建帖子
    private static final int myResultCode2 = 2; // 删除帖子
    
    private static final String s1 = "insertComment";
    private static final String s2 = "deleteComment";
    private static final String s3 = "increaseLikeInPostDetail";
    private static final String s4 = "decreaseLikeInPostDetail";
    private static final String s5 = "refresh";
    
    private static boolean clickSearch = false; // 软键盘搜索按钮 收起软键盘
    
    private MyBroadcastReceiver receiver1, receiver2, // 评论增减
                                receiver3, receiver4, // 点赞数增减
                                receiver5; // 刷新

    private View rootView;
    
    // 搜索高亮字体颜色
    private ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(0, 0, 255));
    
    private EditText et_search;   // 搜索栏
    private ImageView iv_delete;  // 清除输入按钮
    private ImageView iv_new;     // 新建帖子按钮

    private TextView tv_blank_text; // 空提示

    private XRecyclerView xRecyclerView;
    private ForumListRecyclerViewAdapter adapter;

    private List<ForumPosts> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
    {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_cu_forum, null);
            tv_blank_text = rootView.findViewById(R.id.tv_blank);
            list = new ArrayList<>();
            getPosts("", true);
            initView();
            registerBroadCast();
            setListener();
        }
        return rootView;
    }

    /**
     * 描述：分页获取论坛帖子
     * 参数：keyword- 为""表示获取所有，否则按关键词查找
     *      showDialog- 是否显示加载中
     */
    private void getPosts(String keyword, boolean showDialog) {
        CUR_PAGE_NUM = 1;

        ZLoadingDialog dialog = new ZLoadingDialog(rootView.getContext());
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE) //设置类型
                .setLoadingColor(getResources().getColor(R.color.blue)) //颜色
                .setHintText("加载中...")
                .setCanceledOnTouchOutside(false);
        if (showDialog) dialog.show();
        
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("获取帖子: ", "失败 - 网络错误");
                        setTip("网络好像出了点问题，请检查网络设置", View.VISIBLE);
                        break;
                    case SERVER_ERR:
                        Log.i("获取帖子: ", "失败 - 服务器错误");
                        setTip("好像出了点问题，请稍候再试", View.VISIBLE);
                    case ZERO:
                        Log.i("获取帖子: ", "空");
                        setTip("还没有帖子，去发一条", View.VISIBLE);
                        break;
                    case NOTHING:
                        Log.i("搜索帖子: ", "未找到相关结果");
                        new SweetAlertDialog(rootView.getContext(), SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText("未找到相关结果")
                                .setContentText("换个关键词试试")
                                .setConfirmText("关闭")
                                .setConfirmClickListener(SweetAlertDialog::cancel)
                                .show();
                        break;
                    case SUCCESS:
                        Log.i("获取帖子: ", "成功");
                        setTip("", View.GONE);
                        
                        adapter.setKeyWordColor(keyword, colorSpan);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        break;
                }
                if (showDialog) dialog.dismiss();
                super.handleMessage(msg);
            }
        };

        if (keyword.equals(""))
            queryPosts(handler, false);
        else 
            searchPosts(handler, keyword);
    }

    /**
     * 描述：启动线程搜索论坛帖子
     * 参数：keyword- 关键词
     */
    private void searchPosts(Handler handler, String keyword) {

        new Thread(()->{

            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.searchPosts)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE 
                            + "&keyword=" + keyword)
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取帖子: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();

                    ResponsePosts responsePosts = new Gson().fromJson(resStr, ResponsePosts.class);

                    list = responsePosts.getPostsList();
                    if (list == null) {
                        list = new ArrayList<>();
                        msg.what = SERVER_ERR;
                    } else if (list.size() == 0) {
                        msg.what = NOTHING;
                    } else {
                        Log.i("搜索帖子: ", list.toString());
                        msg.what = SUCCESS;
                    }
                    handler.sendMessage(msg);
                }
            });

        }).start();
        
    }

    /**
     * 描述：启动线程获取帖子
     * 参数: handler: 消息处理
     * 参数: isLoadMore: 加载处理
     * 返回：void
     */
    private void queryPosts(Handler handler, boolean isLoadMore) {
        
        new Thread(()->{

            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.queryPosts)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE )
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取帖子: ", Objects.requireNonNull(e.getMessage()));
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();

                    ResponsePosts responsePosts = new Gson().fromJson(resStr, ResponsePosts.class);

                    if (isLoadMore) {
                        list.addAll(responsePosts.getPostsList());
                        if (CUR_PAGE_NUM * PAGE_SIZE >= responsePosts.getTotal()) {
                            msg.what = ZERO;
                        } else {
                            msg.what = SUCCESS;
                        }
                    } else {
                        list = responsePosts.getPostsList();
                        if (list == null) {
                            list = new ArrayList<>();
                            msg.what = SERVER_ERR;
                        } else if (list.size() == 0) {
                            msg.what = ZERO;
                        } else {
                            Log.i("获取帖子: ", list.toString());
                            msg.what = SUCCESS;
                        }
                    }
                    handler.sendMessage(msg);
                }
            });
            
        }).start();
    }
    
    private void setListener() {
        
        // 当软键盘收起时调用getPosts方法
        KeyboardStateObserver.getKeyboardStateObserver(getActivity()).
                setKeyboardVisibilityListener(new KeyboardStateObserver.OnKeyboardVisibilityListener() {
                    @Override
                    public void onKeyboardShow() {
                        clickSearch = false;
                    }

                    @Override
                    public void onKeyboardHide() {
                        if (!clickSearch) {
                            String keyword = et_search.getText().toString().trim();
                            if (keyword.length() > 0)
                                getPosts(keyword, true);
                        }
                    }
                });
        
        // 软键盘搜索按钮监听
        et_search.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                String keyword = et_search.getText().toString().trim();
                clickSearch = true;
                if (keyword.length() > 0) 
                    getPosts(keyword, true);
                return true;
            }
            return false;
        });
        
        // 当搜索框内有文字时显示清除全部按钮
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() == 0) {
                    iv_delete.setVisibility(View.GONE);
                } else {
                    iv_delete.setVisibility(View.VISIBLE);
                }
            }
        });

        // 清空搜索框
        iv_delete.setOnClickListener(view -> {
            et_search.setText("");
            getPosts("", false);
        });
        
        // 进入帖子详情页面
        adapter.setOnItemClickListener((view, pos) -> {
            Intent intent = new Intent(rootView.getContext(), PostDetailActivity.class);
            intent.putExtra("pos", pos);
            intent.putExtra("beginFrom", "forumList");
            intent.putExtra("detail", list.get(pos));
            startActivityForResult(intent, myRequestCode);
        });
        
        // 进入新建帖子页面
        iv_new.setOnClickListener(view -> {
            UserInfo user = (UserInfo) Objects.requireNonNull(getActivity()).getApplication();
            if (user.getUserId() == null) {
                // todo 登录成功后进入新建帖子
                Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                intent.putExtra("loginAfter", "WritePost");
                startActivity(intent);
                Toast.makeText(rootView.getContext(), "请登录", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(rootView.getContext(), WritePostActivity.class);
                intent.putExtra("userId", user.getUserId());
                startActivityForResult(intent, myRequestCode);
            }
        });
        
        // 刷新和加载更多
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(()->{

                    @SuppressLint("HandlerLeak")
                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case SUCCESS:
                                    Log.i("刷新帖子", "成功");
                                    adapter.setList(list);
                                    adapter.notifyDataSetChanged();
                                    setTip("", View.GONE);
                                    break;
                                case ZERO:
                                    Log.i("刷新帖子", "0");
                                    setTip("还没有帖子，去发一条", View.VISIBLE);
                                    break;
                                case SERVER_ERR:
                                    Log.i("刷新帖子", "失败 - 服务器错误");
                                    setTip("好像出了点问题，请稍候再试", View.VISIBLE);
                                    break;
                                case NETWORK_ERR:
                                    Log.i("刷新帖子", "失败 - 网络错误");
                                    setTip("网络好像出了点问题，请检查网络设置", View.VISIBLE);
                                    break;
                            }
                            xRecyclerView.refreshComplete();
                        }
                    };
                    CUR_PAGE_NUM = 1;
                    queryPosts(handler, false);
                    
                }, 1500);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(()->{

                    @SuppressLint("HandlerLeak")
                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            switch (msg.what) {
                                case SUCCESS:
                                    Log.i("加载帖子", "成功");
                                    adapter.notifyDataSetChanged();
                                    break;
                                case ZERO:
                                    Log.i("加载帖子", "0");
                                    xRecyclerView.setNoMore(true);
                                    break;
                                case SERVER_ERR:
                                    Log.i("加载帖子", "失败 - 服务器错误");
                                    break;
                                case NETWORK_ERR:
                                    Log.i("加载帖子", "失败 - 网络错误");
                                    break;
                            }
                            xRecyclerView.loadMoreComplete();
                        }
                    };
                    CUR_PAGE_NUM++;
                    queryPosts(handler, true);
                    
                }, 1500);
            }
        });
    }

    private void initView() {
        et_search = rootView.findViewById(R.id.edt_cu_forum_search);
        iv_delete = rootView.findViewById(R.id.imgv_cu_forum_delete);
        iv_new = rootView.findViewById(R.id.imgv_cu_forum_new);
        
        adapter = new ForumListRecyclerViewAdapter(rootView.getContext(), list);

        xRecyclerView = rootView.findViewById(R.id.rv_cu_forum);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        LayoutAnimationController animationController = AnimationUtils
                .loadLayoutAnimation(rootView.getContext(),R.anim.layout_animation);
        xRecyclerView.setLayoutAnimation(animationController);
    }
    
    /**
     * 描述：对初始化和刷新结果显示/隐藏提示语
     * 参数：tip- 提示语
     *      visibility- 可见性
     * 返回：void
     */
    private void setTip(String tip, int visibility) {
        tv_blank_text.setText(tip);
        tv_blank_text.setVisibility(visibility);
    }
    
    /**
     * 描述：result1: 用户发表了帖子
     *      result2: 用户删除了帖子
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == myRequestCode) {
            if (resultCode == myResultCode1) {
                getPosts("", false);
            } else if (resultCode == myResultCode2) {
                int pos = data.getIntExtra("pos", -1);
                if (pos != -1) {
                    list.remove(pos);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
    
    /**
     * 描述：自定义广播
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {
        
        /**
         * 描述：处理广播
         * 参数：intent: 广播发送的数据
         *      isIncrease: 增or减
         *      where: 1~ReplyNumber; 2~LikeNumber
         * 返回：void
         */
        private void solve(Intent intent, boolean isIncrease, int where) {
            int pos = intent.getIntExtra("pos", -1);
            if (pos != -1) {
                switch (where) {
                    case 1:
                        int n = list.get(pos).getReplyNumber();
                        n = isIncrease ? n+1 : n-1;
                        list.get(pos).setReplyNumber(n);
                        break;
                    case 2: 
                        n = list.get(pos).getLikeNumber();
                        n = isIncrease ? n+1 : n-1;
                        list.get(pos).setLikeNumber(n);
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case s1: solve(intent, true, 1); break;
                case s2: solve(intent, false, 1); break;
                case s3: solve(intent, true, 2); break;
                case s4: solve(intent, false, 2); break;
                case s5: getPosts("", false); break;
            }
        }
    }
    
    /**
     * 描述：注册广播
     */
    private void registerBroadCast() {
        receiver1 = new MyBroadcastReceiver();
        rootView.getContext().registerReceiver(receiver1, new IntentFilter(s1));

        receiver2 = new MyBroadcastReceiver();
        rootView.getContext().registerReceiver(receiver2, new IntentFilter(s2));

        receiver3 = new MyBroadcastReceiver();
        rootView.getContext().registerReceiver(receiver3, new IntentFilter(s3));

        receiver4 = new MyBroadcastReceiver();
        rootView.getContext().registerReceiver(receiver4, new IntentFilter(s4));
        
        receiver5 = new MyBroadcastReceiver();
        rootView.getContext().registerReceiver(receiver5, new IntentFilter(s5));
        
    }

    /**
     * 描述：取消广播注册
     */
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver1);
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver2);
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver3);
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver4);
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver5);
    }
}
