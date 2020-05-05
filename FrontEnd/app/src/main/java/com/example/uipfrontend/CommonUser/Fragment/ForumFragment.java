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
import com.example.uipfrontend.R;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForumFragment extends Fragment {

    private static final int FAILURE = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;
    
    private static final int PAGE_SIZE = 10; // 默认一次请求10条数据
    private int CUR_PAGE_NUM = 1;
    
    private static final int myRequestCode = 233;
    private static final int myResultCode1 = 1; // 新建帖子
    private static final int myResultCode2 = 2; // 删除帖子
    
    private static final String s1 = "insertComment";
    private static final String s2 = "deleteComment";
    private static final String s3 = "increaseLikeInPostDetail";
    private static final String s4 = "decreaseLikeInPostDetail";
    private static final String s5  = "refresh";
    
    private MyBroadcastReceiver receiver1, receiver2, // 评论增减
                                receiver3, receiver4, // 点赞数增减
                                receiver5; // 刷新

    private View rootView;
    
    // 搜索高亮字体颜色
    private ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.rgb(255, 0, 0));
    
    private EditText et_search;   // 搜索栏
    private ImageView iv_delete;  // 清除输入按钮
    private ImageView iv_new;     // 新建帖子按钮

    private TextView tv_blank_text; // 搜索结果为空提示

    private XRecyclerView xRecyclerView;
    private ForumListRecyclerViewAdapter adapter;
    
    private List<ForumPosts> posts;
    private List<ForumPosts> whole;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_cu_forum, null);
            tv_blank_text = rootView.findViewById(R.id.tv_blank);
            posts = new ArrayList<>();
            getPosts();
            initView();
            registerBroadCast();
            setListener();
        }
        return rootView;
    }

    /**
     * 描述：分页获取论坛帖子
     */
    private void getPosts() {
        CUR_PAGE_NUM = 1;

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAILURE:
                        Log.i("获取帖子: ", "失败");
                        tv_blank_text.setText("好像出了点问题");
                        tv_blank_text.setVisibility(View.VISIBLE);
                        break;
                    case ZERO:
                        Log.i("获取帖子: ", "空");
                        tv_blank_text.setText("还没有帖子，去发一条");
                        tv_blank_text.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        Log.i("获取帖子: ", "成功");
                        tv_blank_text.setVisibility(View.GONE);
                        posts.clear();
                        posts.addAll(whole);
                        adapter.setList(posts);
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        queryPosts(handler, false);
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
                    Log.i("获取帖子: ", e.getMessage());
                    msg.what = FAILURE;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();

                    ResponsePosts responsePosts = new Gson().fromJson(resStr, ResponsePosts.class);

                    if (isLoadMore) {
                        whole.addAll(responsePosts.getPostsList());
                        posts.addAll(responsePosts.getPostsList());
                        if (CUR_PAGE_NUM * PAGE_SIZE >= responsePosts.getTotal()) {
                            msg.what = ZERO;
                        } else {
                            msg.what = SUCCESS;
                        }
                    } else {
                        whole = responsePosts.getPostsList();
                        if (whole == null) {
                            whole = new ArrayList<>();
                            msg.what = FAILURE;
                        } else if (whole.size() == 0) {
                            msg.what = ZERO;
                        } else {
                            Log.i("获取帖子: ", whole.toString());
                            msg.what = SUCCESS;
                        }
                    }
                    handler.sendMessage(msg);
                }
            });
            
        }).start();
    }
    
    private void setListener() {
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
                changeKeyWordColor(editable.toString().trim());
            }
        });

        // 清空搜索框
        iv_delete.setOnClickListener(view -> et_search.setText(""));
        
        // 进入帖子详情页面
        adapter.setOnItemClickListener((view, pos) -> {
            Intent intent = new Intent(rootView.getContext(), PostDetailActivity.class);
            intent.putExtra("pos", pos);
            intent.putExtra("beginFrom", "forumList");
            intent.putExtra("detail", posts.get(pos));
            startActivityForResult(intent, myRequestCode);
        });
        
        // 进入新建帖子页面
        iv_new.setOnClickListener(view -> {
            UserInfo user = (UserInfo) Objects.requireNonNull(getActivity()).getApplication();
            if (user.getUserId() == null) {
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
                                    posts.clear();
                                    posts.addAll(whole);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case FAILURE:
                                    Log.i("刷新帖子", "失败");
                                    break;
                                case ZERO:
                                    Log.i("刷新帖子", "0");
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
                            xRecyclerView.loadMoreComplete();
                            switch (msg.what) {
                                case SUCCESS:
                                    Log.i("加载帖子", "成功");
                                    adapter.notifyDataSetChanged();
                                    break;
                                case FAILURE:
                                    Log.i("加载帖子", "失败");
                                    break;
                                case ZERO:
                                    Log.i("加载帖子", "0");
                                    xRecyclerView.setNoMore(true);
                                    break;
                            }
                        }
                    };
                    CUR_PAGE_NUM++;
                    queryPosts(handler, true);
                    
                }, 1500);
            }
        });
    }

    private void changeKeyWordColor(String keyWord){
        // 搜索帖子标题，关键词：keyWord
        if (!keyWord.equals("")) {
            posts.clear();
            for (int i = 0; i < whole.size(); i++) {
                if (whole.get(i).getTitle().contains(keyWord)) {
                    posts.add(whole.get(i));
                }
            }
            adapter.setKeyWordColor(keyWord, redSpan);
            refreshUI();
        }
    }
    
    private void refreshUI(){
        if(adapter == null) {
            adapter = new ForumListRecyclerViewAdapter(rootView.getContext(), posts);
            xRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
            xRecyclerView.scheduleLayoutAnimation();
        }
        if(posts.size() == 0) {
            tv_blank_text.setText("未找到相关结果");
            tv_blank_text.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        et_search = rootView.findViewById(R.id.edt_cu_forum_search);
        iv_delete = rootView.findViewById(R.id.imgv_cu_forum_delete);
        iv_new = rootView.findViewById(R.id.imgv_cu_forum_new);
        
        adapter = new ForumListRecyclerViewAdapter(rootView.getContext(), posts);

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
    
    private void initData() {

//        posts.add(new ForumPosts(95588,"震惊！一名男子疫情期间仍然在外捡垃圾，即使遇到古董也没有察觉，反而砸掉了。网友：太可怜了",
//                "法外狂徒张三", "2020/3/26", 0));
//        posts.add(new ForumPosts(10086,"震惊！著名LOL玩家和DOTA玩家互斥对方不算男人，现场数万人围观！",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(10010,"震惊！管理层出游竟包下一辆火车！上市公司员工曝出惊天内幕！",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(800820,"天呐!喝了这么多年，你也不一定知道的小秘密!看完我马上转了!",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(8820,"生死极限!他竟和一个陌生男人在孤岛生活28年!",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(12135,"震惊！男人看了会沉默，女人看了会流泪！不转不是中国人！",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(95588,"震惊！一名男子疫情期间仍然在外捡垃圾，即使遇到古董也没有察觉，反而砸掉了。网友：太可怜了",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(10086,"震惊！著名LOL玩家和DOTA玩家互斥对方不算男人，现场数万人围观！",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(10010,"震惊！管理层出游竟包下一辆火车！上市公司员工曝出惊天内幕！",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(800820,"天呐!喝了这么多年，你也不一定知道的小秘密!看完我马上转了!",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(8820,"生死极限!他竟和一个陌生男人在孤岛生活28年!",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(12135,"震惊！男人看了会沉默，女人看了会流泪！不转不是中国人！",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(95588,"震惊！一名男子疫情期间仍然在外捡垃圾，即使遇到古董也没有察觉，反而砸掉了。网友：太可怜了",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(10086,"震惊！著名LOL玩家和DOTA玩家互斥对方不算男人，现场数万人围观！",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(10010,"震惊！管理层出游竟包下一辆火车！上市公司员工曝出惊天内幕！",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(800820,"天呐!喝了这么多年，你也不一定知道的小秘密!看完我马上转了!",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(8820,"生死极限!他竟和一个陌生男人在孤岛生活28年!",
//                "法外狂徒张三", "2020/3/26", 666));
//        posts.add(new ForumPosts(12135,"震惊！男人看了会沉默，女人看了会流泪！不转不是中国人！",
//                "法外狂徒张三", "2020/3/26", 666));
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
                getPosts();
            } else if (resultCode == myResultCode2) {
                int pos = data.getIntExtra("pos", -1);
                if (pos != -1) {
                    whole.remove(pos);
                    posts.clear();
                    posts.addAll(whole);
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
                        int n = whole.get(pos).getReplyNumber();
                        n = isIncrease ? n+1 : n-1;
                        whole.get(pos).setReplyNumber(n);
                        posts.get(pos).setReplyNumber(n);
                        break;
                    case 2:
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
                case s5: getPosts(); break;
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
