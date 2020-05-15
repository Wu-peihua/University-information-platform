package com.example.uipfrontend.Student.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Fragment.ForumFragment;
import com.example.uipfrontend.Entity.ResponseCourse;

import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Student.Activity.CourseDetailActivity;
import com.example.uipfrontend.Student.Adapter.StudentCourseRecyclerViewAdapter;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.example.uipfrontend.Utils.MultiMenusView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;


import com.qlh.dropdownmenu.DropDownMenu;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class StudentCommentFragment extends Fragment {

    private String[] headers;//菜单头部选项
    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu dropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private XRecyclerView recyclerView;  //下拉刷新上拉加载
    private StudentCourseRecyclerViewAdapter studentCourseRecyclerViewAdapter;     //课程内容适配器

    private View rootView;
    private List<Course> courses =new ArrayList<>();//课程实体数组
    //private List<Course> AllCourses = new ArrayList<>();

    //多级菜单
    private String[] levelOneMenu;
    private String[][] levelTwoMenu;

    //分页请求数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    private static final int PAGE_SIZE = 20;   //默认一次请求20条数据
    private static int CUR_PAGE_NUM = 1;

    //记录下拉筛选菜单选择的学校ID和学院ID
    private int selectedUniversity = 0; //默认为0
    private int selectedInstitute = -1;  //默认为-1

    private MyFragmentBroadcastReceiver receiver3,receiver4;//MyreleaseFragment用户评分发生变化
    private MyActivityBroadcastReceiver receiver1,receiver2;//CourseDetailAcitivity评论变化

    private static final int myRequestCode = 166;


    private static final String s1 = "insertCoursecomment";
    private static final String s2 = "deleteCourseComment";

    private static final String s3 = "deleteFromMyRelease";
    private static final String s4 = "updateFromMyRelease";
    private UserInfo user;//登录用户数据
    //private static final String s5  = "refresh";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_student_comment, null);
            recyclerView = rootView.findViewById(R.id.rv_student_course);

            init();
        }
        return rootView;
    }

    private void init() {
        //user = (UserInfo) getActivity().getApplication();
        //if(user.getUserType()==null||user.getUserType()!=2){
        //    Toast.makeText(getContext(),"非学生用户无法使用该模块噢！",Toast.LENGTH_LONG).show();
        //}else {
        getMenusData();

        initMenus();

        getData(getResources().getString(R.string.serverBasePath) +
                getResources().getString(R.string.queryCourse)
                + "/?pageNum=1&pageSize=" + PAGE_SIZE);

        initListener();
        registerBroadCast();
    //}

    }

    private void getMenusData(){
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

    //按照类型请求后端课程数据
    private void getData(String requestUrl){
        //courses = new ArrayList<>();
        ZLoadingDialog dialog = new ZLoadingDialog(getContext());
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(getResources().getColor(R.color.blue))//颜色
                .setHintText("加载中...")
                .show();
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
                        //Toast.makeText(recyclerView.getContext(),"暂时没有新的信息！",Toast.LENGTH_SHORT).show();
                        initRecyclerView();
                        break;
                }
            }
        };

        new Thread(()->{
            Request request = new Request.Builder()
                    .url(requestUrl)
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
                            //System.out.println("课程列表为:"+courses.toString());
                        }
                        handler.sendMessage(msg);
                        Log.i("获取: ", String.valueOf(courses.size()));
                    }
                }
            });
        }).start();
    }

    //请求所有课程数据
    private void fetchCourseInfo(String requestUrl){
        new Handler().postDelayed(() -> {
            @SuppressLint("HandlerLeak")
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case SUCCESS:
                            Log.i("获取", "成功");
                            studentCourseRecyclerViewAdapter.setList(courses);
                            studentCourseRecyclerViewAdapter.notifyDataSetChanged();
                            break;
                        case FAIL:
                            Log.i("获取", "失败");
                            break;
                        case ZERO:
                            Log.i("获取", "0");
                            break;
                    }
                    recyclerView.refreshComplete();
                }
            };

            new Thread(()->{
                CUR_PAGE_NUM = 1;
                Request request = new Request.Builder()
                        .url(requestUrl)
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

                        if( courses.size() == 0 ) {
                            msg.what = ZERO;
                        } else {
                            msg.what = SUCCESS;
                        }
                        handler.sendMessage(msg);
                        Log.i("获取 ", String.valueOf(courses.size()));

                    }
                });
            }).start();
        }, 1500);

    }
    /*
        public void initData() {

            courses = new ArrayList<>();
            courses.add(new Course((long)1001,"大数据与云计算", "Mr.ZHANG", "大数据与云计算平台使用", 4));

            courses.add(new Course((long)1002,"计算机网络", "Mr.ZHU", "了解互联网基础", 3));

            courses.add(new Course((long)1010,"数据库原理", "Mr.ZHENG", "数据库基本原理，常用数据库操作", 4));


            courses.add(new Course((long)1028,"操作系统", "Mr.CHEN", "操作系统构建及运行原理", 3));


            courses.add(new Course((long)2019,"算法设计", "Mr.LIN", "基础算法与数据结构", 2));

            courses.add(new Course((long)1003,"大数据与云计算", "Mr.ZHANG", "大数据与云计算平台使用", 4));

            courses.add(new Course((long)1004,"计算机网络", "Mr.ZHU", "了解互联网基础", 3));

            courses.add(new Course((long)1015,"数据库原理", "Mr.ZHENG", "数据库基本原理，常用数据库操作", 4));


            courses.add(new Course((long)1022,"操作系统", "Mr.CHEN", "操作系统构建及运行原理", 3));


            courses.add(new Course((long)2023,"算法设计", "Mr.LIN", "基础算法与数据结构", 2));


            //AllCourses.addAll(courses);

            //count = mTags.size();
        }


     */
    private void initMenus() {

        dropDownMenu = rootView.findViewById(R.id.dropDownMenu_student_course);
        headers = new String[]{"所属院校"};

//        //初始化多级菜单
//        final String[] levelOneMenu = {"全部", "华南师范大学", "华南理工大学", "中山大学"};
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
        View contentView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_student_comment,null);
        //装载
        dropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,contentView);

    }

    private void initListener() {

        //下拉菜单

        multiMenusView.setOnSelectListener(new com.example.uipfrontend.Utils.MultiMenusView.OnSelectListener() {
            @Override
            public void getMenuOne(String var1, int position) {
                Toast.makeText(rootView.getContext(),var1,Toast.LENGTH_SHORT).show();
                selectedUniversity = position + 1;
            }

            @Override
            public void getMenuTwo(String var1, int position) {
                Toast.makeText(rootView.getContext(),var1,Toast.LENGTH_SHORT).show();
                selectedInstitute = position + 1;

                if(selectedUniversity <= 0 )
                    selectedUniversity = 1;
                System.out.println("university:"+selectedUniversity+"  institute:"+selectedInstitute);

                getData(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryCourseByUniAndIns)
                        + "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE  + "&universityId=" + selectedUniversity + "&instituteId=" + selectedInstitute);



                dropDownMenu.setTabText(var1);
                dropDownMenu.closeMenu();
            }
        });

    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentCourseRecyclerViewAdapter = new StudentCourseRecyclerViewAdapter(this.getContext(), courses);
        studentCourseRecyclerViewAdapter.setHasStableIds(true);
        //点赞数据错乱修复
        recyclerView.setAdapter(studentCourseRecyclerViewAdapter);

        //设置item 点击跳转至课程详情页面
        studentCourseRecyclerViewAdapter.setOnItemClickListener(new StudentCourseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                //System.out.println("点击了"+"courseId:\n");
                Long courseID = courses.get(position).getCourseID();
                //Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                //intent.putExtra("coursedetail",courses.get(position));
                // startActivity(intent);
                Intent intent = new Intent(rootView.getContext(), CourseDetailActivity.class);
                intent.putExtra("pos", position);
                intent.putExtra("beginFrom", "courseList");
                intent.putExtra("coursedetail", courses.get(position));
                startActivityForResult(intent, myRequestCode);

            }
        });

        recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);



        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if(selectedUniversity <= 0 && selectedInstitute <=0 ){
                    fetchCourseInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryCourse) +
                            "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE );
                }else{
                    fetchCourseInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryCourseByUniAndIns) +
                            "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE + "&universityId=" + selectedUniversity + "&instituteId=" + selectedInstitute);
                }

                recyclerView.refreshComplete();

            }

            @Override
            public void onLoadMore() {
                fetchCourseInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryCourse) +
                        "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE );

                if(selectedUniversity <= 0 && selectedInstitute <=0 ){
                    fetchCourseInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryCourse) +
                            "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE );
                }else{
                    fetchCourseInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryCourseByUniAndIns) +
                            "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE + "&universityId=" + selectedUniversity + "&instituteId=" + selectedInstitute);
                }
                recyclerView.setNoMore(true);
            }

        });


    }

    private LocalBroadcastManager broadcastManager;


    //接受StudentMyreleaseFragment的广播
    private class MyFragmentBroadcastReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case s3:
                case s4:
                    //System.out.println("StudentFragment接受数据>>>>>>>>>>>>>>>>>");
                    getData(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryCourse)
                            + "/?pageNum=1&pageSize=" + PAGE_SIZE );
                    studentCourseRecyclerViewAdapter.notifyDataSetChanged();
                    break;

            }
        }
    }

    //接受课程详情的广播
    private class MyActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case s1:
                case s2:
                    getData(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryCourse)
                            + "/?pageNum=1&pageSize=" + PAGE_SIZE );
                    studentCourseRecyclerViewAdapter.notifyDataSetChanged();
                    break;

            }
        }
    }

    //描述：注册广播
    private void registerBroadCast() {

        broadcastManager = LocalBroadcastManager.getInstance(getActivity());

        receiver1 = new MyActivityBroadcastReceiver ();
        rootView.getContext().registerReceiver(receiver1, new IntentFilter(s1));

        receiver2 = new MyActivityBroadcastReceiver ();
        rootView.getContext().registerReceiver(receiver2, new IntentFilter(s2));

        receiver3 = new MyFragmentBroadcastReceiver();
        broadcastManager.registerReceiver(receiver3, new IntentFilter(s3));

        receiver4 = new MyFragmentBroadcastReceiver();
        broadcastManager.registerReceiver(receiver4, new IntentFilter(s4));
    }

    //描述：取消广播注册
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver1);
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver2);
        broadcastManager.unregisterReceiver(receiver3);
        broadcastManager.unregisterReceiver(receiver4);
    }


}