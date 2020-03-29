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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Adapter.ResInfoAdapter;
import com.example.uipfrontend.CommonUser.AddResActivity;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ResourceFragment extends Fragment {

    private View rootView;

    private XRecyclerView xRecyclerView;
    private ResInfoAdapter resInfoAdapter;

    private List<ResInfo> posts;
    private List<ResInfo> whole;

    private ForegroundColorSpan blueSpan;
    private EditText et_search;
    private ImageView iv_delete;

    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_cu_resource, null);

            blueSpan = new ForegroundColorSpan(Color.rgb(255, 0, 0));
            et_search = rootView.findViewById(R.id.edt_cu_res_search);
            iv_delete = rootView.findViewById(R.id.imgv_cu_res_delete);

            initData();
            initRecyclerView();
            setSearch();
            initFab();
        }
        return rootView;
    }

    private void initData() {
        posts = new ArrayList<>();
        whole = new ArrayList<>();

        posts.add(new ResInfo("http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg", "腰间盘突出",
                "支付宝破解版", "小朋友你是否有很多问号", "www.baidu.com", "2020-02-02 00:00"));
        posts.add(new ResInfo("http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg", "腰间盘突出",
                "支付宝破解版", "小朋友你是否有很多问号", "www.baidu.com", "2020-02-02 00:00"));
        posts.add(new ResInfo("http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg", "腰间盘突出",
                "支付宝破解版", "小朋友你是否有很多问号", "www.baidu.com", "2020-02-02 00:00"));
        posts.add(new ResInfo("http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg", "腰间盘突出",
                "支付宝破解版", "小朋友你是否有很多问号", "www.baidu.com", "2020-02-02 00:00"));
        posts.add(new ResInfo("http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg", "腰间盘突出",
                "支付宝破解版", "小朋友你是否有很多问号", "www.baidu.com", "2020-02-02 00:00"));
        posts.add(new ResInfo("http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg", "腰间盘突出",
                "支付宝破解版", "小朋友你是否有很多问号", "www.baidu.com", "2020-02-02 00:00"));

        whole.addAll(posts);
    }

    private void initRecyclerView() {
        xRecyclerView = rootView.findViewById(R.id.rv_cu_res);

        resInfoAdapter = new ResInfoAdapter(posts, rootView.getContext());
        xRecyclerView.setAdapter(resInfoAdapter);

        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext()) {
            @Override
            public boolean canScrollVertically() {
                if (posts == null || posts.size() == 0)
                    return false;
                return super.canScrollVertically();
            }
        };
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(rootView.getContext(), R.anim.layout_animation);
        xRecyclerView.setLayoutAnimation(animationController);

//        resInfoAdapter.setOnItemClickListener(new ResInfoAdapter.OnItemClickListener() {
//            @Override
//            public void onClick(int position) {
//                Toast.makeText(rootView.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
//            }
//        });

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

    private void setSearch() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    iv_delete.setVisibility(View.GONE);
                } else {
                    iv_delete.setVisibility(View.VISIBLE);
                }

                iv_delete.setOnClickListener(view -> et_search.setText(""));

                setSearchResult(editable.toString().trim());
            }
        });
    }

    private void setSearchResult(String text) {
        posts.clear();
        if (text.equals("")) {
            posts.addAll(whole);
            resInfoAdapter.setText(null, null);
        } else {
            for (int i = 0; i < whole.size(); i++) {
                if (whole.get(i).getTitle().contains(text)) {
                    posts.add(whole.get(i));
                }
            }
            resInfoAdapter.setText(text, blueSpan);
        }
        resInfoAdapter.notifyDataSetChanged();
        xRecyclerView.scheduleLayoutAnimation();
    }

    public void initFab() {
        fab = rootView.findViewById(R.id.fab_cu_res);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), AddResActivity.class);
                startActivity(intent);
            }
        });
    }
}
