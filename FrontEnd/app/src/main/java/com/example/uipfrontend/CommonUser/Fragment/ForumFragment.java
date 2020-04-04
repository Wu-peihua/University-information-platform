package com.example.uipfrontend.CommonUser.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Activity.PostDetailActivity;
import com.example.uipfrontend.CommonUser.Activity.WritePostActivity;
import com.example.uipfrontend.CommonUser.Adapter.ForumListRecyclerViewAdapter;
import com.example.uipfrontend.Entity.ForumPosts;
import com.example.uipfrontend.R;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ForumFragment extends Fragment {

    private View rootView;
    
    // 搜索高亮字体颜色
    private ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.rgb(255, 0, 0));
    
    private EditText et_search;   // 搜索栏
    private ImageView iv_delete;  // 清除输入按钮
    private ImageView iv_new;     // 新建帖子按钮

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
            initData();
            initView();
            setListener();
        }
        return rootView;
    }

    private void initData() {
        posts = new ArrayList<>();
        whole = new ArrayList<>();
        posts.add(new ForumPosts(95588,"震惊！一名男子疫情期间仍然在外捡垃圾，即使遇到古董也没有察觉，反而砸掉了。网友：太可怜了",
                "法外狂徒张三", "2020/3/26", 0));
        posts.add(new ForumPosts(10086,"震惊！著名LOL玩家和DOTA玩家互斥对方不算男人，现场数万人围观！",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(10010,"震惊！管理层出游竟包下一辆火车！上市公司员工曝出惊天内幕！",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(800820,"天呐!喝了这么多年，你也不一定知道的小秘密!看完我马上转了!",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(8820,"生死极限!他竟和一个陌生男人在孤岛生活28年!",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(12135,"震惊！男人看了会沉默，女人看了会流泪！不转不是中国人！",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(95588,"震惊！一名男子疫情期间仍然在外捡垃圾，即使遇到古董也没有察觉，反而砸掉了。网友：太可怜了",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(10086,"震惊！著名LOL玩家和DOTA玩家互斥对方不算男人，现场数万人围观！",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(10010,"震惊！管理层出游竟包下一辆火车！上市公司员工曝出惊天内幕！",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(800820,"天呐!喝了这么多年，你也不一定知道的小秘密!看完我马上转了!",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(8820,"生死极限!他竟和一个陌生男人在孤岛生活28年!",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(12135,"震惊！男人看了会沉默，女人看了会流泪！不转不是中国人！",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(95588,"震惊！一名男子疫情期间仍然在外捡垃圾，即使遇到古董也没有察觉，反而砸掉了。网友：太可怜了",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(10086,"震惊！著名LOL玩家和DOTA玩家互斥对方不算男人，现场数万人围观！",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(10010,"震惊！管理层出游竟包下一辆火车！上市公司员工曝出惊天内幕！",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(800820,"天呐!喝了这么多年，你也不一定知道的小秘密!看完我马上转了!",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(8820,"生死极限!他竟和一个陌生男人在孤岛生活28年!",
                "法外狂徒张三", "2020/3/26", 666));
        posts.add(new ForumPosts(12135,"震惊！男人看了会沉默，女人看了会流泪！不转不是中国人！",
                "法外狂徒张三", "2020/3/26", 666));
        whole.addAll(posts);
    }

    private void setListener() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

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
            intent.putExtra("detail", posts.get(pos));
            startActivity(intent);
        });
        
        // 进入新建帖子页面
        iv_new.setOnClickListener(view -> {
            Intent intent = new Intent(rootView.getContext(), WritePostActivity.class);
            startActivity(intent);
        });
        
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

    private void changeKeyWordColor(String keyWord){
        // 搜索帖子标题，关键词：keyWord
        posts.clear();
        if(keyWord.equals("")){
            posts.addAll(whole);
            adapter.setKeyWordColor(null, null);
        } else {
            for (int i = 0; i < whole.size(); i++) {
                if(whole.get(i).getTitle().contains(keyWord)) {
                    posts.add(whole.get(i));
                }
            }
            adapter.setKeyWordColor(keyWord, redSpan);
        }
        refreshUI();
    }
    
    private void refreshUI(){
        if(adapter == null) {
            adapter = new ForumListRecyclerViewAdapter(rootView.getContext(), posts);
            xRecyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
            xRecyclerView.scheduleLayoutAnimation();
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
}
