package com.example.uipfrontend.Admin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uipfrontend.Admin.AdminCourseDetailActivity;
import com.example.uipfrontend.Admin.Adapter.CourseAdapter;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.R;


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

    private ListView StudentCourseList;
    private  View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_admin_course,container,false);

        /***************************学院与学科分类筛选*************************/
        dropDownMenu = root.findViewById(R.id.admin_dropDownMenu_student_course);

        init();

        return root;
    }

    public void init() {

        initMenus();
        initListener();


    }

    private void initCourseList() {

        /****************************课程列表********************************/

        ArrayList<Course> courseList = new ArrayList<>();


        //添加list数据项
        for(int i = 1;i<=3;i++) {

            courseList.add(new Course(1001,"大数据与云计算", "Mr.ZHANG", "大数据与云计算平台使用", 4.50));

            courseList.add(new Course(1002,"计算机网络", "Mr.ZHU", "了解互联网基础", 3.50));

            courseList.add(new Course(1010,"数据库原理", "Mr.ZHENG", "数据库基本原理，常用数据库操作", 4));


            courseList.add(new Course(1028,"操作系统", "Mr.CHEN", "操作系统构建及运行原理", 3));


            courseList.add(new Course(2019,"算法设计", "Mr.LIN", "基础算法与数据结构", 2.50));


        }

        /* 创建设置课程数据适配器 */
        CourseAdapter adapter = new CourseAdapter(getContext(), R.layout.admin_course_carditem, courseList);

        StudentCourseList.setAdapter(adapter);


        //跳转到课程详情页面
        StudentCourseList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListView listView = (ListView) parent;
                Course course = (Course) listView.getItemAtPosition(position);
                String name = course.getName();
                System.out.println(name);

                Intent intent = new Intent(getContext(), AdminCourseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });
    }


    private void initMenus() {

        headers = new String[]{"所属院校"};
        //初始化多级菜单
        final String[] levelOneMenu = {"华南师范大学"};
        //学院 暂定18个
        final String[][] levelTwoMenu = {
                {"全部","计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
                {"全部","计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
                {"全部","计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
                {"全部","计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"}

        };
        multiMenusView = new MultiMenusView(this.getContext(),levelOneMenu,levelTwoMenu);
        popupViews.add(multiMenusView);
        //初始化内容视图
        View contentView = LayoutInflater.from(this.getContext()).inflate(R.layout.list_sudent_course,null);
        StudentCourseList = contentView.findViewById(R.id.StudentCourseList);

        initCourseList();

        // View contentView = (ListView) StudentCourseList;
        //StudentCourseList = contentView.findViewById(R.id.StudentCourseList);

        //contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //contentView.setText("内容显示区域");
        //contentView.setGravity(Gravity.CENTER);
        //contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        //StudentCourseList=contentView.findViewById(R.id.StudentCourseList);

        //装载
        Log.i("检查",String.valueOf(Arrays.asList(headers).size())+" "+String.valueOf(popupViews.size()));
        dropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,contentView);
        //这句会运行多次oncreate，会报错。

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

    }

}