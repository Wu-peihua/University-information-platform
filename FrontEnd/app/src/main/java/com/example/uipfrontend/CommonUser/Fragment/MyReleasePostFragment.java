package com.example.uipfrontend.CommonUser.Fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uipfrontend.CommonUser.Activity.PostDetailActivity;
import com.example.uipfrontend.CommonUser.Activity.WritePostActivity;
import com.example.uipfrontend.CommonUser.Adapter.MyReleasePostAdapter;
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

public class MyReleasePostFragment extends Fragment {

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;

    private static final int PAGE_SIZE = 10; // 默认一次请求10条数据
    private int CUR_PAGE_NUM = 1;

    private static final int myRequestCode = 2333;
    private static final int myResultCode3 = 3; // 删除帖子
    private static final int myResultCode4 = 4; // 修改帖子

    private View rootView;
    
    private List<ForumPosts> list;
    private XRecyclerView xRecyclerView;
    private MyReleasePostAdapter adapter;
    
    private TextView tv_blank_text; // 空白提示

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        if(rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_my_release_forum,null);
            tv_blank_text = rootView.findViewById(R.id.tv_blank);
            list = new ArrayList<>();
            getPosts();
            initRecyclerView();
            setListener();
        }
        return rootView;
    }

    /**
     * 描述：分页获取用户发过的帖子
     */
    private void getPosts() {
        
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case NETWORK_ERR:
                        Log.i("获取发过的帖子: ", "失败 - 网络错误");
                        tv_blank_text.setText("网络好像出了点问题");
                        tv_blank_text.setVisibility(View.VISIBLE);
                        break; 
                    case SERVER_ERR:
                        Log.i("获取发过的帖子: ", "失败 - 服务器错误");
                        tv_blank_text.setText("好像出了点问题");
                        tv_blank_text.setVisibility(View.VISIBLE);
                        break;
                    case ZERO:
                        Log.i("获取发过的帖子: ", "空");
                        tv_blank_text.setText("还没有发过的帖子，去发一条");
                        tv_blank_text.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        Log.i("获取发过的帖子: ", "成功");
                        tv_blank_text.setVisibility(View.GONE);
                        adapter.setList(list);
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

        UserInfo user = (UserInfo) Objects.requireNonNull(getActivity()).getApplication();
        
        new Thread(()->{

            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.selectPosts)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE
                            + "&userId=" + user.getUserId())
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取发过的帖子: ", Objects.requireNonNull(e.getMessage()));
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
                            Log.i("获取发过的帖子: ", list.toString());
                            msg.what = SUCCESS;
                        }
                    }
                    handler.sendMessage(msg);
                }
            });

        }).start();
    }
    
    private void setListener() {
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
                                    Log.i("刷新发过的帖子", "成功");
                                    adapter.setList(list);
                                    adapter.notifyDataSetChanged();
                                    break;
                                case ZERO:
                                    Log.i("刷新发过的帖子", "0");
                                    break;
                                case SERVER_ERR:
                                    Log.i("刷新发过的帖子", "失败 - 服务器错误");
                                    break;
                                case NETWORK_ERR:
                                    Log.i("刷新发过的帖子", "失败 - 网络错误");
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
                                    Log.i("加载发过的帖子", "成功");
                                    adapter.notifyDataSetChanged();
                                    break;
                                case ZERO:
                                    Log.i("加载发过的帖子", "0");
                                    xRecyclerView.setNoMore(true);
                                    break;
                                case SERVER_ERR:
                                    Log.i("加载发过的帖子", "失败 - 服务器错误");
                                    break;
                                case NETWORK_ERR:
                                    Log.i("加载发过的帖子", "失败 - 网络错误");
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
        
        adapter.setOnDetailClickListener((view, pos) -> {
            Intent intent = new Intent(rootView.getContext(), PostDetailActivity.class);
            intent.putExtra("pos", pos);
            intent.putExtra("beginFrom", "myReleasePost");
            intent.putExtra("detail", list.get(pos));
            startActivityForResult(intent, myRequestCode);
        });
        
        adapter.setOnModifyClickListener((view, pos) -> {
            Intent intent = new Intent(rootView.getContext(), WritePostActivity.class);
            intent.putExtra("pos", pos);
            intent.putExtra("post", list.get(pos));
            startActivityForResult(intent, myRequestCode);
        });
    }
    
    private void initRecyclerView(){
        adapter = new MyReleasePostAdapter(rootView.getContext(), list);
        
        xRecyclerView = rootView.findViewById(R.id.xrv_mr_forum);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

    }
    

    /**
     * 描述：result3 用户删除了帖子
     *      result4 用户修改了帖子
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == myRequestCode) {
            if (resultCode == myResultCode3) {
                int pos = data.getIntExtra("pos", -1);
                if (pos != -1) {
                    list.remove(pos);
                    adapter.notifyDataSetChanged();
                }
            } else if (resultCode == myResultCode4) {
                int pos = data.getIntExtra("pos", -1);
                ForumPosts post = (ForumPosts) Objects.requireNonNull(data.getExtras()).get("newPost");
                if (pos != -1 && post != null) {
                    list.get(pos).setTitle(post.getTitle());
                    list.get(pos).setContent(post.getContent());
                    list.get(pos).setPictures(post.getPictures());
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
    
    public void onStop() {
        Intent intent = new Intent();
        intent.setAction("refresh");
        Objects.requireNonNull(getActivity()).sendBroadcast(intent);
        super.onStop();
    }
}