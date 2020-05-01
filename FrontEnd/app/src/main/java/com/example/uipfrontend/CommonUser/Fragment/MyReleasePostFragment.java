package com.example.uipfrontend.CommonUser.Fragment;


import android.annotation.SuppressLint;
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

import com.example.uipfrontend.CommonUser.Adapter.MyReleasePostAdapter;
import com.example.uipfrontend.Entity.ForumPosts;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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

    private static final int FAILURE = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;

    private View rootView;
    
    private List<ForumPosts> list;
    private XRecyclerView xRecyclerView;
    private MyReleasePostAdapter adapter;
    
    private TextView tv_blank_text; // 空白提示

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ){
        if(rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_my_release_forum,null);
            tv_blank_text = rootView.findViewById(R.id.tv_blank);
        }
        getPosts();
        return rootView;
    }

    private void getPosts() {
        
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
                        break;
                }
                initRecyclerView();
                super.handleMessage(msg);
            }
        };

        new Thread(() -> {
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();
            UserInfo user = (UserInfo) Objects.requireNonNull(getActivity()).getApplication();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) 
                            + getResources().getString(R.string.selectPosts) 
                            + "/?userId=" + user.getUserId())
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
                    JsonObject object = new JsonParser().parse(resStr).getAsJsonObject();
                    JsonArray array = object.getAsJsonArray("postsList");

                    Log.i("获取帖子: ", resStr);

                    list = new ArrayList<>();
                    for (JsonElement element : array) {
                        ForumPosts post = new Gson().fromJson(element, new TypeToken<ForumPosts>(){}.getType());
                        list.add(post);
                    }

                    msg.what = list.size() == 0 ? ZERO : SUCCESS;
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }
    
    public void initRecyclerView(){
        adapter = new MyReleasePostAdapter(rootView.getContext(), list);
        
        xRecyclerView = rootView.findViewById(R.id.xrv_mr_forum);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        // 刷新和加载更多
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                xRecyclerView.setNoMore(true);
            }
        });
    }
    
    private void initData() {
        list = new ArrayList<>();
        
        for(int i = 10; i > 0; i--) {
            ForumPosts post = new ForumPosts();
            post.setTitle("我发布的我发布的我发布的我发布的我发布的我发布的我发布的我发布的我发布的我发布的");
            post.setUserName("荣耀");
            post.setCreated("2020-4-" + i + " 14:30");
            list.add(post);
        }
        
        if(list == null || list.size() == 0) {
            tv_blank_text.setVisibility(View.VISIBLE);
        }
    }
    
}