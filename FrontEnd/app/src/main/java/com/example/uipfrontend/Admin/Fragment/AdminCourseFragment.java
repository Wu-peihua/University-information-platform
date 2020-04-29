package com.example.uipfrontend.Admin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.Admin.Activity.AdminAddCourseActivity;
import com.example.uipfrontend.Admin.Activity.AdminCourseDetailActivity;
import com.example.uipfrontend.Admin.Adapter.CourseAdapter;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.Course;
//import com.example.uipfrontend.Student.Activity.CourseDetailActivity;
//import com.example.uipfrontend.Student.Adapter.StudentCourseRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.qlh.dropdownmenu.view.MultiMenusView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AdminCourseFragment extends Fragment {

    private String[] headers;//菜单头部选项
    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu dropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private XRecyclerView recyclerView;  //下拉刷新上拉加载
    private CourseAdapter studentCourseRecyclerViewAdapter;     //课程内容适配器

    private View rootView;
    private View rootContentView;    //根视图内容
    private List<Course> courses =new ArrayList<>();//课程实体数组
    //private List<Course> AllCourses = new ArrayList<>();
    private FloatingActionButton fabtn;  //浮动按钮，发布新的组队信息


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_admin_course, null);
            rootContentView=inflater.inflate(R.layout.fragment_admin_course_content,null);
           // recyclerView = rootContentView.findViewById(R.id.adminrv_student_course);

            init();
        }
        return rootView;
    }

    private void init() {

        initData();
        initMenus();
        initRecyclerView();
        initListener();
        initFAB();

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
        //View contentView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_admin_course,null);
        RelativeLayout contentView = rootContentView.findViewById(R.id.rl_admin_course_course);

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


        //设置item 点击跳转至课程详情页面
        studentCourseRecyclerViewAdapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Long courseID = courses.get(position).getCourseID();
                Log.i("点击了","courseId:"+courseID.toString());
                Log.e("course位置", "" + position + "被点击了！");

                Intent intent = new Intent(getContext(), AdminCourseDetailActivity.class);
                intent.putExtra("coursedetail",courses.get(position));

                startActivity(intent);

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


    public void initData() {


        courses.add(new Course((long) 1001,"大数据与云计算", "Mr.ZHANG", "大数据与云计算平台使用", 4));

        courses.add(new Course((long) 1002,"计算机网络", "Mr.ZHU", "了解互联网基础", 3));

        courses.add(new Course((long) 1010,"数据库原理", "Mr.ZHENG", "数据库基本原理，常用数据库操作", 4));


        courses.add(new Course((long) 1028,"操作系统", "Mr.CHEN", "操作系统构建及运行原理", 3));


        courses.add(new Course((long) 2019,"算法设计", "Mr.LIN", "基础算法与数据结构", 2));

        courses.add(new Course((long) 1003,"大数据与云计算", "Mr.ZHANG", "大数据与云计算平台使用", 4));

        courses.add(new Course((long) 1004,"计算机网络", "Mr.ZHU", "了解互联网基础", 3));

        courses.add(new Course((long) 1015,"数据库原理", "Mr.ZHENG", "数据库基本原理，常用数据库操作", 4));


        courses.add(new Course((long) 1022,"操作系统", "Mr.CHEN", "操作系统构建及运行原理", 3));


        courses.add(new Course((long) 2023,"算法设计", "Mr.LIN", "基础算法与数据结构", 2));


        //AllCourses.addAll(courses);

        //count = mTags.size();
    }

    private void initRecyclerView() {
        recyclerView = rootContentView.findViewById(R.id.rv_admin_group);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentCourseRecyclerViewAdapter = new CourseAdapter(this.getContext(), courses);
        recyclerView.setAdapter(studentCourseRecyclerViewAdapter);


        //设置item 点击跳转至课程详情页面
        /*studentCourseRecyclerViewAdapter.setOnItemClickListener(new StudentCourseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Course course = (Course) AllCourses.get(position);
                String name = course.getName();
                Log.i("点击了","name:"+name);
                Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                intent.putExtras(bundle);
                Log.e("Main", "" + position + "被点击了！！");
                startActivity(intent);

            }
        });

         */



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
    private void initFAB(){
        fabtn = rootContentView.findViewById(R.id.fabtn_admin_addcourse);
        fabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContentView.getContext(), AdminAddCourseActivity.class);
                startActivity(intent);
            }
        });
    }



}
