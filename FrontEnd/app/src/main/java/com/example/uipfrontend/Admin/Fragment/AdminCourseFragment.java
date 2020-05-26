package com.example.uipfrontend.Admin.Fragment;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.Admin.Activity.AdminAddCourseActivity;
import com.example.uipfrontend.Admin.Activity.AdminCourseDetailActivity;
import com.example.uipfrontend.Admin.Adapter.CourseAdapter;
import com.example.uipfrontend.Entity.ResponseCourse;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.Course;
//import com.example.uipfrontend.Student.Activity.CourseDetailActivity;
//import com.example.uipfrontend.Student.Adapter.StudentCourseRecyclerViewAdapter;
import com.example.uipfrontend.Student.Fragment.StudentCommentFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
//import com.qlh.dropdownmenu.view.MultiMenusView;
import com.example.uipfrontend.Utils.MultiMenusView;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;


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
    private static final int PAGE_SIZE = 20;   //默认一次请求6条数据
    private static int CUR_PAGE_NUM = 1;

    //记录下拉筛选菜单选择的学校ID和学院ID
    private int selectedUniversity = 0; //默认为0
    private int selectedInstitute = -1;  //默认为-1


    private static final int myRequestCode = 166;

    private MyActivityBroadcastReceiver receiver1,receiver2,receiver3,receiver4;//删除课程、删除评论、编辑课程,新建课程变化

    private static final String s1 = "deleteCourse";
    private static final String s2 = "deleteCourseComment";
    private static final String s3 = "addCourse";

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

        initMenus();

        getData(getResources().getString(R.string.serverBasePath) +
                getResources().getString(R.string.queryCourse)
                + "/?pageNum=1&pageSize=" + PAGE_SIZE );

        initListener();
        initFAB();
        registerBroadCast();
    }
    //请求后端课程数据
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
                        Toast.makeText(recyclerView.getContext(),"暂时没有新的信息！",Toast.LENGTH_SHORT).show();
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
//                            System.out.println("课程列表为:"+courses.toString());
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

        multiMenusView = new MultiMenusView(this.getContext(),levelOneMenu,levelTwoMenu);
        popupViews.add(multiMenusView);
        //初始化内容视图
        //View contentView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_admin_course,null);
        RelativeLayout contentView = rootContentView.findViewById(R.id.rl_admin_course_course);

        //装载
        dropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,contentView);

    }

    private void initListener() {

        multiMenusView.setOnSelectListener(new com.example.uipfrontend.Utils.MultiMenusView.OnSelectListener() {
            @Override
            public void getMenuOne(String var1, int position) {
                Toast.makeText(rootView.getContext(), var1, Toast.LENGTH_SHORT).show();
                selectedUniversity = position + 1;
            }

            @Override
            public void getMenuTwo(String var1, int position) {
                Toast.makeText(rootView.getContext(), var1, Toast.LENGTH_SHORT).show();
                selectedInstitute = position + 1;

                if (selectedUniversity <= 0)
                    selectedUniversity = 1;
                System.out.println("university:" + selectedUniversity + "  institute:" + selectedInstitute);

                getData(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryCourseByUniAndIns)
                        + "/?pageNum=" + 1 + "&pageSize=" + PAGE_SIZE + "&universityId=" + selectedUniversity + "&instituteId=" + selectedInstitute);


                dropDownMenu.setTabText(var1);
                dropDownMenu.closeMenu();
            }
        });
    }
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

                Intent intent = new Intent(rootView.getContext(), AdminCourseDetailActivity.class);
//                intent.putExtra("coursedetail",courses.get(position));
//
//                startActivity(intent);
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

    private LocalBroadcastManager broadcastManager;

    //接受课程详情的广播
    private class MyActivityBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                case s1:
                case s2:
                case s3:
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

        receiver3 = new MyActivityBroadcastReceiver();
        rootView.getContext().registerReceiver(receiver3, new IntentFilter(s3));



    }

    //描述：取消广播注册
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver1);
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver2);
        Objects.requireNonNull(getActivity()).unregisterReceiver(receiver3);
    }

}
