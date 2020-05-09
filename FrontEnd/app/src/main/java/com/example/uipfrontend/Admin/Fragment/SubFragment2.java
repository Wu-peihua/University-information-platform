package com.example.uipfrontend.Admin.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.Admin.Adapter.AdminReportCourseCommentAdapter;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Admin.Adapter.AdminReportRecruitRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.qlh.dropdownmenu.view.MultiMenusView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class SubFragment2 extends Fragment {

    private String[] headers;//菜单头部选项
    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu dropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private XRecyclerView recyclerView;  //下拉刷新上拉加载
    private AdminReportCourseCommentAdapter studentCourseRecyclerViewAdapter;     //课程内容适配器

    private View rootView;
    private List<CourseComment> mTags=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // T.showShort(getActivity(), "SubFragment1==onCreateView");
//        TextView tv = new TextView(getActivity());
//        tv.setTextSize(25);
//        tv.setText("课程点评");
//        tv.setGravity(Gravity.CENTER);
//        return tv;
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_admin_course, null);
            recyclerView = rootView.findViewById(R.id.adminrv_student_course);

            init();
        }
        return rootView;
    }
    private void init(){
        initCommentData();
        initMenus();
        initRecyclerView();
        initListener();
    }
    private void initMenus() {

        dropDownMenu = rootView.findViewById(R.id.admin_dropDownMenu_student_course);
        headers = new String[]{"所属院校"};
        //初始化多级菜单
        final String[] levelOneMenu = {"华南师范大学"};
        //学院 暂定18个
        final String[][] levelTwoMenu = {
                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"}

        };
        multiMenusView = new MultiMenusView(this.getContext(),levelOneMenu,levelTwoMenu);
        popupViews.add(multiMenusView);
        //初始化内容视图
        View contentView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_admin_course,null);
        //装载
        dropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,contentView);

    }

    private void initListener() {

        //下拉菜单
        multiMenusView.setOnSelectListener(new MultiMenusView.OnSelectListener() {
            @Override
            public void getValue(String showText) {
                dropDownMenu.setTabText(showText);
                dropDownMenu.closeMenu();
            }
        });




        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                recyclerView.refreshComplete();

            }
            @Override
            public void onLoadMore() {
                recyclerView.setNoMore(true);
            }
        });


    }


    public void initCommentData() {

        /*
        mTags.add (new CourseComment(2001,"LinussPP", "2020-4-25 22:44", "有趣", 4.50,10));
        mTags.add (new CourseComment(2002,"ZhouKK", "2020-4-25 22:44", "学到了很多", 4.50,12));
        mTags.add (new CourseComment(2003,"MandyWong", "2020-4-25 22:44", "没意思", 3.50,13));
        mTags.add (new CourseComment(3008,"LarryChen", "2020-4-25 22:44", "课程难度大", 3.50,20));
        mTags.add (new CourseComment(4010,"LinYii", "2020-4-25 22:44", "作业量惊人", 2.50,12));
        mTags.add (new CourseComment(2020,"Oliver", "2020-4-25 22:44", "不推荐", 1.50,10));
        mTags.add (new CourseComment(2034,"Patric", "2020-4-25 22:44", "推荐", 4.50,2));



         */
        DateFormat datefomat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date commentDate = null;
        try {
            commentDate = datefomat.parse(datefomat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i=0;i<10;i++) {
            mTags.add(new CourseComment((long) 1, (long) 3, "interesting", (float)4, commentDate));

        }
        //count = mTags.size();
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentCourseRecyclerViewAdapter = new AdminReportCourseCommentAdapter(this.getContext(),mTags);
        recyclerView.setAdapter(studentCourseRecyclerViewAdapter);




        recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
/*
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                recyclerView.refreshComplete();

            }
            @Override
            public void onLoadMore() {
                recyclerView.setNoMore(true);
            }
        });

 */

    }


}
