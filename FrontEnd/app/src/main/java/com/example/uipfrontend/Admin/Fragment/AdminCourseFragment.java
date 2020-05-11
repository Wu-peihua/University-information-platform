package com.example.uipfrontend.Admin.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.Admin.Activity.AdminAddCourseActivity;
import com.example.uipfrontend.Admin.Activity.AdminCourseDetailActivity;
import com.example.uipfrontend.Admin.Adapter.CourseAdapter;
import com.example.uipfrontend.Entity.ResponseCourse;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.Course;
//import com.example.uipfrontend.Student.Activity.CourseDetailActivity;
//import com.example.uipfrontend.Student.Adapter.StudentCourseRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
//import com.qlh.dropdownmenu.view.MultiMenusView;
import com.example.uipfrontend.Utils.MultiMenusView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


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

    //多级菜单
    private String[] levelOneMenu;
    private String[][] levelTwoMenu;

    //分页请求数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    private static final int PAGE_SIZE = 6;   //默认一次请求6条数据
    private static int CUR_PAGE_NUM = 1;



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
            //recyclerView = rootContentView.findViewById(R.id.adminrv_student_course);

            init();
        }
        return rootView;
    }

    private void init() {

        getMenusData();
        //initData();

        initMenus();

        getData();
        //initRecyclerView();
        initListener();
        initFAB();

    }
    //请求后端课程数据
    private void getData(){
        //courses = new ArrayList<>();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message){
                switch (message.what){
                    case SUCCESS:
                        Log.i("获取: ", "成功");
                        //初始化列表
                        initRecyclerView();
                        break;

                    case FAIL:
                        Log.i("获取: ", "失败");
                        break;

                    case ZERO:
                        Log.i("获取: ", "0");
                        Toast.makeText(recyclerView.getContext(),"暂时没有新的信息！",Toast.LENGTH_SHORT).show();
                        initRecyclerView();
                        break;
                }
            }
        };

        new Thread(()->{
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryCourse)
                            + "/?pageNum=1&pageSize=" + PAGE_SIZE )
                    .get()
                    .build();
            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("获取: ", e.getMessage());
                    msg.what = FAIL;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String data = response.body().string();
                    //System.out.println("课程请求返回数据:"+data);
                    ResponseCourse responseCourse = new Gson().fromJson(data,
                            ResponseCourse.class);

                    /*if(responseCourse==null) {
                        System.out.println("response获取失败");
                    }

                     */
                    courses = responseCourse.getCourseInfoList();
                    if (courses == null){
                        System.out.println("没有课程数据");
                    }
                    else {
                        if (courses.size() == 0) { //获取的数量为0
                            msg.what = ZERO;
                            System.out.println("课程列表为空\n");
                        } else {
                            msg.what = SUCCESS;

                            System.out.println("课程列表为:"+courses.toString());
                        }
                        handler.sendMessage(msg);
                        Log.i("获取: ", String.valueOf(courses.size()));
                    }
                }
            });
        }).start();
    }


    private void getMenusData(){

        //通过sharepreference获取顶部筛选菜单数据
        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("data",MODE_PRIVATE);
        //第二个参数为缺省值，如果不存在该key，返回缺省值
        String strUniversity = sp.getString("university",null);
        String strInstitute = sp.getString("institute",null);

        List<String> universityList = new ArrayList<>();
        List<String> instituteList = new ArrayList<>();
        //将String转为List
        String str1[] = strUniversity.split(",");
        universityList = Arrays.asList(str1);
        String str[] = strInstitute.split(",");
        instituteList = Arrays.asList(str);

        levelOneMenu = universityList.toArray(new String[0]);
        String[] temp = instituteList.toArray(new String[0]);
        levelTwoMenu = new String [levelOneMenu.length][temp.length];

        for(int i = 0;i<levelOneMenu.length;++i){
            System.arraycopy(temp, 0, levelTwoMenu[i], 0, temp.length);
        }

    }


    private void initMenus() {

        dropDownMenu = rootView.findViewById(R.id.admin_dropDownMenu_student_course);
        headers = new String[]{"所属院校"};
//        //初始化多级菜单
//        final String[] levelOneMenu = {"华南师范大学"};
//        //学院 暂定18个
//        final String[][] levelTwoMenu = {
//                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
//                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
//                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
//                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
//                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
//                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
//                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
//                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"}
//
//        };
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
//        multiMenusView.setOnSelectListener(new MultiMenusView.OnSelectListener() {
//            @Override
//            public void getValue(String showText) {
//                dropDownMenu.setTabText(showText);
//                dropDownMenu.closeMenu();
//            }
//        });
        multiMenusView.setOnSelectListener(new com.example.uipfrontend.Utils.MultiMenusView.OnSelectListener() {
            @Override
            public void getMenuOne(String var1, int position) {
                Toast.makeText(rootView.getContext(),var1,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getMenuTwo(String var1, int position) {
                Toast.makeText(rootView.getContext(),var1,Toast.LENGTH_SHORT).show();

                dropDownMenu.setTabText(var1);
                dropDownMenu.closeMenu();
            }
        });



//        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                recyclerView.refreshComplete();
//
//            }
//            @Override
//            public void onLoadMore() {
//                recyclerView.setNoMore(true);
//            }
//        });



    }

    /*
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


     */
    private void initRecyclerView() {
        recyclerView = rootContentView.findViewById(R.id.rv_admin_group);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentCourseRecyclerViewAdapter = new CourseAdapter(this.getContext(), courses);
        studentCourseRecyclerViewAdapter.setHasStableIds(true);
        recyclerView.setAdapter(studentCourseRecyclerViewAdapter);



        //设置item 点击跳转至课程详情页面
        studentCourseRecyclerViewAdapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                System.out.println("点击了"+"courseId:\n");
                Long courseID = courses.get(position).getCourseID();
//                Log.i("点击了","courseId:"+courseID.toString());
//                Log.e("course位置", "" + position + "被点击了！");
                System.out.println("点击了"+"courseId:"+courseID.toString());

                Intent intent = new Intent(getContext(), AdminCourseDetailActivity.class);
                intent.putExtra("coursedetail",courses.get(position));

                startActivity(intent);

            }
        });


        recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(() -> {
                    @SuppressLint("HandlerLeak")
                    Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            switch (msg.what){
                                case SUCCESS:
                                    Log.i("刷新", "成功");
                                    studentCourseRecyclerViewAdapter.setList(courses);
                                    studentCourseRecyclerViewAdapter.notifyDataSetChanged();
                                    break;
                                case FAIL:
                                    Log.i("刷新", "失败");
                                    break;
                                case ZERO:
                                    Log.i("刷新", "0");
                                    break;
                            }
                            recyclerView.refreshComplete();
                        }
                    };

                    new Thread(()->{
                        CUR_PAGE_NUM = 1;
                        Request request = new Request.Builder()
                                .url(getResources().getString(R.string.serverBasePath) +
                                        getResources().getString(R.string.queryCourse)
                                        + "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE +"&state=0")
                                .get()
                                .build();
                        Message msg = new Message();
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.i("获取: ", e.getMessage());
                                msg.what = FAIL;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                ResponseCourse responseCourse = new Gson().fromJson(response.body().string(),
                                        ResponseCourse.class);
                                courses = responseCourse.getCourseInfoList();
                                if(courses.size() == 0) {
                                    msg.what = ZERO;
                                } else {
                                    msg.what = SUCCESS;
                                }
                                handler.sendMessage(msg);
                                Log.i("获取: ", String.valueOf(courses.size()));
                            }
                        });
                    }).start();
                }, 1500);
                recyclerView.refreshComplete();

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(() -> {
                    @SuppressLint("HandlerLeak")
                    Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            switch (msg.what){
                                case SUCCESS:
                                    Log.i("加载", "成功");
                                    recyclerView.refreshComplete();
                                    studentCourseRecyclerViewAdapter.notifyDataSetChanged();
                                    break;
                                case FAIL:
                                    Log.i("加载", "失败");
                                    break;
                                case ZERO:
                                    Log.i("加载", "0");
                                    recyclerView.setNoMore(true);
                                    break;
                            }
                        }
                    };

                    new Thread(()->{
                        CUR_PAGE_NUM++;
                        Request request = new Request.Builder()
                                .url(getResources().getString(R.string.serverBasePath) +
                                        getResources().getString(R.string.queryCourse)
                                        + "/?pageNum="+ CUR_PAGE_NUM +"&pageSize=" + PAGE_SIZE + "&state=0")
                                .get()
                                .build();
                        Message msg = new Message();
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.i("获取: ", e.getMessage());
                                msg.what = FAIL;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                ResponseCourse responseCourse= new Gson().fromJson(response.body().string(),
                                        ResponseCourse.class);
                                courses.addAll(responseCourse.getCourseInfoList());
                                if((CUR_PAGE_NUM - 2) * PAGE_SIZE + responseCourse.getPageSize() <
                                        responseCourse.getTotal() ){
                                    msg.what = ZERO;
                                } else {
                                    msg.what = SUCCESS;
                                }
                                handler.sendMessage(msg);
                                Log.i("获取: ", String.valueOf(courses.size()));
                            }
                        });
                    }).start();
                }, 1500);
                recyclerView.setNoMore(true);
            }
        });


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
