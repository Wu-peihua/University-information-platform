package com.example.uipfrontend.Admin.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.Admin.Adapter.AdminReportCourseCommentAdapter;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.Entity.ResponseCourseComment;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Admin.Adapter.AdminReportRecruitRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.example.uipfrontend.Utils.MultiMenusView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class SubFragment2 extends Fragment {

//    private String[] headers;//菜单头部选项
//    private List<View> popupViews = new ArrayList<>();//菜单列表视图
//    private DropDownMenu dropDownMenu;
//    private MultiMenusView multiMenusView;//多级菜单
//    private XRecyclerView recyclerView;  //下拉刷新上拉加载
//    private AdminReportCourseCommentAdapter studentCourseRecyclerViewAdapter;     //课程评论适配器
//
//    private View rootView;
//    private List<CourseComment> mTags=new ArrayList<>();
    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu dropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private XRecyclerView recyclerView;  //下拉刷新上拉加载
    private AdminReportCourseCommentAdapter studentCourseRecyclerViewAdapter;


    private List<CourseComment> comments =new ArrayList<>();//用户评论列表

    private View rootView;  //根视图（下拉筛选框）
    private View rootContentView;    //根视图内容
    private TextView tv_blank;


    private String[] levelOneMenu;
    private String[][] levelTwoMenu;

    //分页请求数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    private static final int PAGE_SIZE = 6;   //默认一次请求6条数据
    private static int CUR_PAGE_NUM = 1;

    //记录下拉筛选菜单选择的学校ID和学院ID
    private int selectedUniversity = 0; //默认为0
    private int selectedInstitute = -1;  //默认为-1

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
            rootView = inflater.inflate(R.layout.fragment_student_recruit, null);
            rootContentView = inflater.inflate(R.layout.fragment_admin_recruit_content,null);

            //recyclerView = rootContentView.findViewById(R.id.rv_student_group);
            tv_blank = rootContentView.findViewById(R.id.admin_tv_blank);
            init();
        }
        return rootView;
    }
    private void init(){
        //初始化下拉筛选
        getMenusData();
        initMenus();
        initListener();
//        getData(getResources().getString(R.string.serverBasePath) +
//                getResources().getString(R.string.queryRecruitReport)
//                + "/?pageNum=1&pageSize=" + PAGE_SIZE );
        getData(getResources().getString(R.string.serverBasePath) +
                getResources().getString(R.string.queryCommentReport)
                + "/?pageNum=1&pageSize=" + PAGE_SIZE);
        //获取列表数据
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
    private void initMenus() {

        dropDownMenu = rootView.findViewById(R.id.dropDownMenu_student_group);

        //菜单头部选项
        String[] headers = new String[]{"所属院校"};

        multiMenusView = new MultiMenusView(this.getContext(),levelOneMenu,levelTwoMenu);
        popupViews.add(multiMenusView);
        //初始化内容视图
        RelativeLayout contentView = rootContentView.findViewById(R.id.rl_student_recruit_recruit);
        //装载
        dropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,contentView);

    }
    private void initListener() {

//下拉菜单
        multiMenusView.setOnSelectListener(new MultiMenusView.OnSelectListener() {
            @Override
            public void getMenuOne(String var1, int position) {
                Toast.makeText(rootContentView.getContext(),var1,Toast.LENGTH_SHORT).show();
                selectedUniversity = position + 1;

            }

            @Override
            public void getMenuTwo(String var1, int position) {
                selectedInstitute = position + 1;

                if(selectedUniversity <= 0 )
                    selectedUniversity = 1;
                System.out.println("university:"+selectedUniversity+"  institute:"+selectedInstitute);

                getData(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryCommentByUniAndInsReport)
                        + "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE  + "&universityId=" + selectedUniversity + "&instituteId=" + selectedInstitute);


                dropDownMenu.setTabText(var1);
                dropDownMenu.closeMenu();
            }
        });

    }

    private void getData(String requestUrl){
        //courses = new ArrayList<>();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message){
                switch (message.what){
                    case SUCCESS:
                        Log.i("获取: ", "成功");
                        //初始化列表
                        initRecyclerView();
                        tv_blank.setVisibility(View.GONE);
                        break;

                    case FAIL:
                        Log.i("获取: ", "失败");
                        tv_blank.setText("获取信息失败");
                        tv_blank.setVisibility(View.VISIBLE);
                        break;

                    case ZERO:
                        Log.i("获取: ", "0");
                        //Toast.makeText(recyclerView.getContext(),"暂时没有新的信息！",Toast.LENGTH_SHORT).show();
                        //initRecyclerView();
                        tv_blank.setText("无相关评论举报信息");
                        tv_blank.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        //查询评论数据
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
                    //System.out.println("课程评论请求返回数据:"+data);

                    //日期格式化
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();
                    ResponseCourseComment responsecomment = gson.fromJson(data,
                            ResponseCourseComment.class);

                    if(responsecomment==null) {
                        System.out.println("response获取失败");
                    }

                    //获取新的comments --更新列表
                    comments = responsecomment.getCourseEvaluationInfoList();


                    if (comments == null){
                        System.out.println("没有评论数据");
                    }
                    else {
                        if (comments.size() == 0) { //获取的数量为0
                            msg.what = ZERO;
                            System.out.println("评论列表为空\n");
                        }else {
                            msg.what = SUCCESS;

                            //System.out.println("评论列表为:"+comments.toString());
                        }
                        handler.sendMessage(msg);
                        Log.i("获取: ", String.valueOf(comments.size()));
                    }
                }
            });
        }).start();


    }



    private void initRecyclerView() {

        recyclerView = rootContentView.findViewById(R.id.rv_student_group);
        //recyclerView=rootContentView.findViewById(R.id.rv_student_comment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentCourseRecyclerViewAdapter = new AdminReportCourseCommentAdapter(this.getContext(),comments);
        recyclerView.setAdapter(studentCourseRecyclerViewAdapter);

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
                                    tv_blank.setVisibility(View.GONE);
                                    studentCourseRecyclerViewAdapter.setList(comments);
                                    studentCourseRecyclerViewAdapter.notifyDataSetChanged();
                                    break;
                                case FAIL:
                                    tv_blank.setText("获取信息失败");
                                    tv_blank.setVisibility(View.VISIBLE);
                                    Log.i("刷新", "失败");
                                    break;
                                case ZERO:
                                    Log.i("刷新", "0");
                                    tv_blank.setText("无相关评论举报信息");
                                    tv_blank.setVisibility(View.VISIBLE);
                                    break;
                            }
                            recyclerView.refreshComplete();
                        }
                    };

                    new Thread(()->{
                        CUR_PAGE_NUM = 1;
                        Request request = new Request.Builder()
                                .url(getResources().getString(R.string.serverBasePath) +
                                        getResources().getString(R.string.queryCommentReport)
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

                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();
                                ResponseCourseComment responseComment = gson.fromJson(response.body().string(),
                                        ResponseCourseComment.class);
                                comments = responseComment.getCourseEvaluationInfoList();
                                if(comments.size() == 0) {
                                    msg.what = ZERO;
                                } else {
                                    msg.what = SUCCESS;
                                }
                                handler.sendMessage(msg);
                                Log.i("获取: ", String.valueOf(comments.size()));
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
                                        getResources().getString(R.string.queryCommentReport)
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

                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();
                                ResponseCourseComment responseComment= gson.fromJson(response.body().string(),
                                        ResponseCourseComment.class);
                                comments.addAll(responseComment.getCourseEvaluationInfoList());
                                if((CUR_PAGE_NUM - 2) * PAGE_SIZE + responseComment.getPageSize() <
                                        responseComment.getTotal() ){
                                    msg.what = ZERO;
                                } else {
                                    msg.what = SUCCESS;
                                }
                                handler.sendMessage(msg);
                                Log.i("获取: ", String.valueOf(comments.size()));
                            }
                        });
                    }).start();
                }, 1500);
                recyclerView.setNoMore(true);
            }
        });

        studentCourseRecyclerViewAdapter.setCommentPassClickListener(new AdminReportCourseCommentAdapter.commentPassClickListener() {
            @Override
            public void onPassClick(int position) {
                Toast.makeText(rootView.getContext(), "已通过审核，举报数清0", Toast.LENGTH_SHORT).show();
                reportNumberToZero(comments.get(position).getInfoId(),position);
            }
        });
        studentCourseRecyclerViewAdapter.setCommentUnPassClickListener(new AdminReportCourseCommentAdapter.commentUnPassClickListener() {
            @Override
            public void onUnPassClick(int position) {
                Toast.makeText(rootView.getContext(), "审核不通过，评论删除", Toast.LENGTH_SHORT).show();
                deleteComment(comments.get(position).getInfoId(),position);
            }
        });

    }

    private void reportNumberToZero(Long id,int pos){
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
                        Log.i("组队举报数清0: ", "失败");
                        break;
                    case SUCCESS:
                        Log.i("组队举报数清0: ", "成功");
                        comments.remove(pos);
                        studentCourseRecyclerViewAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(()->{
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();

            FormBody.Builder builder = new FormBody.Builder();
            builder.add("infoId", String.valueOf(id));
            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.modifyCommentReportNumber))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("修改举报数: ", e.getMessage());
                    msg.what = FAIL;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.i("修改举报数：", response.body().string());
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            });

        }).start();
    }

    private void deleteComment(Long id,int pos){
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
                        Log.i("删除评论: ", "失败");
                        break;
                    case SUCCESS:
                        Log.i("删除评论: ", "成功");
                        comments.remove(pos);
                        studentCourseRecyclerViewAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(()->{
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();

            FormBody.Builder builder = new FormBody.Builder();
            builder.add("infoId", String.valueOf(id));
            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.AdmindeleteComment))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("删除评论: ", e.getMessage());
                    msg.what = FAIL;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Log.i("删除评论：", response.body().string());
                    msg.what = SUCCESS;
                    handler.sendMessage(msg);
                }
            });

        }).start();
    }

}
