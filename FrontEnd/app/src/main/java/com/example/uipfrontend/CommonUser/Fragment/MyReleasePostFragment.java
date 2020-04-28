package com.example.uipfrontend.CommonUser.Fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uipfrontend.CommonUser.Adapter.MyReleasePostAdapter;
import com.example.uipfrontend.Entity.ForumPosts;
import com.example.uipfrontend.R;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyReleasePostFragment extends Fragment {

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
        initData();
        initRecyclerView();
        return rootView;
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