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

import com.example.uipfrontend.CommonUser.Activity.AddResActivity;
import com.example.uipfrontend.CommonUser.Adapter.ResInfoAdapter;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.qlh.dropdownmenu.view.MultiMenusView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResourceFragment extends Fragment {

    private View rootView;
    private View rootContentView;

    private XRecyclerView  xRecyclerView;
    private ResInfoAdapter resInfoAdapter;

    private List<ResInfo> posts;
    private List<ResInfo> whole;

    private DropDownMenu        dropDownMenu;
    private ForegroundColorSpan span;
    private EditText            et_search;
    private ImageView           iv_delete;

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
            rootContentView = inflater.inflate(R.layout.fragment_cu_resource_content, null);

            initDropDownMenu();
            initData();
            initXRecyclerView();
            setSearch();
            initFab();
        }
        return rootView;
    }

    private void initDropDownMenu() {
        String[] headers = new String[]{"资源类型"};
        String[] levelOneMenu = {"全部", "哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学", "艺术学"};
        String[][] levelTwoMenu = {
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"}
        };

        MultiMenusView multiMenusView = new MultiMenusView(this.getContext(), levelOneMenu, levelTwoMenu);

        List<View> popupViews = new ArrayList<>();
        popupViews.add(multiMenusView);

        dropDownMenu = rootView.findViewById(R.id.ddm_cu_resource);
        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, rootContentView.findViewById(R.id.rl_cu_resource));

        multiMenusView.setOnSelectListener(new MultiMenusView.OnSelectListener() {
            @Override
            public void getValue(String showText) {
                dropDownMenu.setTabText(showText);
                dropDownMenu.closeMenu();
            }
        });
    }

    private void initData() {
        posts = new ArrayList<>();
        whole = new ArrayList<>();

        posts.add(new ResInfo(0, 0, "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg",
                "张咩阿", "支付宝破解版", "清明前后，种瓜种豆。当然，那都是为漫长夏日的怎样过活做准备的，儿时生在乡下的人，" +
                "这些话多半还是有些耳熟的。一般的话，清明过后是一日热胜一日，偶尔会有个不应时的桃花暮春雪，也是个稀少的意外，大多这个时候，" +
                "植物随着气候变化也即将转换着面目，即便是在城里，也能掐算着哪个时候能见上什么。踏春的时尚，之外哪能少的了吃货们的小算计呢。",
                "www.baidu.com", "2020-02-02 00:00", 100, true));
        posts.add(new ResInfo(0, 0, "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg",
                "张咩阿", "支付宝破解版", "清明前后，种瓜种豆。当然，那都是为漫长夏日的怎样过活做准备的，儿时生在乡下的人，" +
                "这些话多半还是有些耳熟的。一般的话，清明过后是一日热胜一日，偶尔会有个不应时的桃花暮春雪，也是个稀少的意外，大多这个时候，" +
                "植物随着气候变化也即将转换着面目，即便是在城里，也能掐算着哪个时候能见上什么。踏春的时尚，之外哪能少的了吃货们的小算计呢。",
                "www.baidu.com", "2020-02-02 00:00", 100, false));
        for (int i = 0; i < 10; i++)
            posts.add(new ResInfo(0, 0, "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg",
                    "张咩阿", "支付宝破解版", null, "www.baidu.com", "2020-02-02 00:00", 0, false));

        whole.addAll(posts);
    }

    private void initXRecyclerView() {
        xRecyclerView = rootContentView.findViewById(R.id.rv_cu_res);

        resInfoAdapter = new ResInfoAdapter(posts, rootContentView.getContext());
        xRecyclerView.setAdapter(resInfoAdapter);

        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootContentView.getContext()) {
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

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(rootContentView.getContext(), R.anim.layout_animation);
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
        span = new ForegroundColorSpan(Color.rgb(255, 0, 0));
        et_search = rootContentView.findViewById(R.id.edt_cu_res_search);
        iv_delete = rootContentView.findViewById(R.id.imgv_cu_res_delete);

        iv_delete.setOnClickListener(view -> et_search.setText(""));

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
            resInfoAdapter.setText(text, span);
        }
        resInfoAdapter.notifyDataSetChanged();
        xRecyclerView.scheduleLayoutAnimation();
    }

    public void initFab() {
        fab = rootContentView.findViewById(R.id.fab_cu_res);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContentView.getContext(), AddResActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == 1) {
                ResInfo receiveData = (ResInfo) data.getSerializableExtra("resInfo");
                posts.add(0, receiveData);
                whole.add(0, receiveData);
                resInfoAdapter.notifyDataSetChanged();
            }
    }
}
