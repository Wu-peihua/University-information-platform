package com.example.uipfrontend.Admin.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Admin.Adapter.AdminCourseCommentRecyclerViewAdapter;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.Entity.ResponseCourseComment;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import com.example.uipfrontend.Student.Adapter.CourseCommentRecyclerViewAdapter;

public class AdminCourseDetailActivity extends AppCompatActivity {

    //private ListView CommentList;
    private TextView commentSum;//评论数目
    private FButton EditCourse;//编辑课程
    private EditText CommentEidt ;//编辑评论框
    private RatingBar UserRating;//评分星星

    private TextView Name ;
    private TextView Teacher;
    private TextView Description;
    private TextView Score ;
    private  RatingBar BarScore;//评分平均分
    private ImageView CourseImage;

    private TextView order_by_time; // 评论按时间排序
    private TextView order_by_like; // 评论按热度排序


    private XRecyclerView recyclerView;  //评论列表下拉刷新上拉加载
    private AdminCourseCommentRecyclerViewAdapter commentAdapter;//用户评论适配器

    //private View rootView;
    private Long globalcourseid ;//课程id
    private Integer globalcourseschoolId;//课程所属学校id

    private List<CourseComment> comments =new ArrayList<>();//用户评论列表


    //分页请求课程评论数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    private static final int PAGE_SIZE = 20;   //默认一次请求6条数据
    private static int CUR_PAGE_NUM = 1;

    private UserInfo user;//全局用户信息
    private static final DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static boolean flag = true;

    private String     beginFrom; // 标识从哪里启动这个Activity
    private int        coursePos;   // 该条课程在list的位置


    private  Course coursedetail;//全局的课程

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            finish();
        }
        setContentView(R.layout.item_admin_course_details);

        //接受点击事件传参数
        beginFrom = getIntent().getStringExtra("beginFrom");
        coursePos = getIntent().getIntExtra("pos", -1);

        coursedetail = (Course) Objects.requireNonNull(getIntent().getExtras()).get("coursedetail");
        globalcourseid = coursedetail.getCourseID();
        globalcourseschoolId = coursedetail.getSchoolId();//非本校用户禁止评分

        init(coursedetail);
    }

    public void init(Course coursedetail){
        //课程卡片详情
        user = (UserInfo) getApplication();
        initCardView(coursedetail);
        getCommentData();


        //initRecyclerView();
    }

    private  void initCardView(Course course) {

        Name = (TextView)this.findViewById(R.id.admincoursename);
        Teacher = (TextView)this.findViewById(R.id.adminTeacherName);
        Description = (TextView)this.findViewById(R.id.admincourseDescription);
        Score = (TextView)this.findViewById(R.id.adminRatingScore);
        BarScore = (RatingBar)this.findViewById(R.id.admincourseRatingBar);
        CourseImage = (ImageView)this.findViewById(R.id.admincourseImage);



        EditCourse = (FButton) findViewById(R.id.admin_fbtn_Editcourse) ;
        commentSum = (TextView) findViewById(R.id.admin_course_comment_sum);

        order_by_time = findViewById(R.id.admin_tv_course_comment_order_by_time);
        order_by_like = findViewById(R.id.admin_tv_course_comment_order_by_like);

        Name.setText(course.getName());
        Teacher.setText(course.getTeacher());
        Description.setText(course.getDescription());
        Glide.with(this).load(course.getCoursePicture())
                .placeholder(R.drawable.mysql_logo)
                .error(R.drawable.mysql_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(CourseImage);

        //评分文本bar显示设置精确到0.1
        BarScore.setStepSize((float) 0.1);
        BigDecimal b   =   new   BigDecimal(course.getScore());
        float trimScore =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).floatValue();
        Score.setText(String.valueOf(trimScore));
        BarScore.setRating(trimScore);
        setCommentSum();



        /***************************编辑课程**********************************************/


//        AddComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(DialogView.getParent()!=null){
//                    ((ViewGroup)DialogView.getParent()).removeView(DialogView);
//                }
//                Commentdialog.setContentView(DialogView);
//                Commentdialog.show();
//                Log.i("click","点击了我要评论按钮");
//            }
//        });
        EditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(DialogView.getParent()!=null){
//                    ((ViewGroup)DialogView.getParent()).removeView(DialogView);
//                }
//                Commentdialog.setContentView(DialogView);
//                Commentdialog.show();
                Intent intent = new Intent(AdminCourseDetailActivity.this,AdminAddCourseActivity.class);
                intent.putExtra("course",coursedetail);
                startActivityForResult(intent, 1);
//                startActivity(intent);
                Log.i("click","点击了编辑课程按钮");
            }
        });

    }

    private void setListListener() {
        // 按热度从高到低排序
        order_by_like.setOnClickListener(view -> {
            if(!order_by_time.getText().toString().equals("时间")) {
                order_by_like.setTextColor(getResources().getColor(R.color.blue));
                order_by_time.setTextColor(getResources().getColor(R.color.gray));
                order_by_time.setText("时间");
                flag = true;
                sortByLikeNum();
                commentAdapter.setList(comments);
                commentAdapter.notifyDataSetChanged();
            }
        });

        // 按时间排序
        order_by_time.setOnClickListener(view -> {
            order_by_time.setTextColor(getResources().getColor(R.color.blue));
            order_by_like.setTextColor(getResources().getColor(R.color.gray));
            String text = order_by_time.getText().toString();
            if (flag) { sortByTimeAsc(); flag = false; }
            else { Collections.reverse(comments); }

            if (text.contains("↑")) {
                order_by_time.setText("时间↓");
            } else if (text.contains("↓")) {
                order_by_time.setText("时间↑");
            } else {
                order_by_time.setText("时间↓");
            }
            commentAdapter.setList(comments);
            commentAdapter.notifyDataSetChanged();
        });

        commentAdapter.setonItemClickListener((view, pos) -> {
            //System.out.println("new course comment id"+comments.get(pos).getInfoId());
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("提示")
                    .setContentText("确定要删除这条评论吗？")
                    .setConfirmText("确定")
                    .setCancelText("取消")
                    .showCancelButton(true)
                    .setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                        sweetAlertDialog.setTitle("请稍后");
                        sweetAlertDialog.setContentText("");
                        sweetAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.showCancelButton(false);
                        sweetAlertDialog.show();
                        deleteComment(comments.get(pos).getInfoId(), pos, sweetAlertDialog);
                    })
                    .setCancelClickListener(SweetAlertDialog::cancel)
                    .show();
        });

    }

    /**
     * 设置评论数目
     */
    private void setCommentSum() {
        if (comments.size() == 0) {
            commentSum.setText("还没有人评论，快来抢沙发吧。");
        } else {
            commentSum.setText(comments.size() + "条评论");
        }
    }
    /**获取评论数据
     * */
    public void getCommentData(){
        //courses = new ArrayList<>();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message){
                switch (message.what){
                    case SUCCESS:
                        Log.i("获取: ", "成功");
                        //初始化列表
                        initRecyclerView();
                        setListListener();
                        setCommentSum();
                        break;

                    case FAIL:
                        Log.i("获取: ", "失败");
                        break;

                    case ZERO:
                        Log.i("获取: ", "0");
                        //Toast.makeText(recyclerView.getContext(),"暂时没有新的信息！",Toast.LENGTH_SHORT).show();
                        initRecyclerView();
                        setListListener();
                        setCommentSum();
                        break;
                }
            }
        };//查询评论数据
        new Thread(()->{
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryCourseEvaluationByCourseId)
                            + "/?pageNum=1&pageSize=" + PAGE_SIZE+"&courseId="+globalcourseid)
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
//                    System.out.println("课程评论请求返回数据:"+data);

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
                        } else {
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
    public void initRecyclerView() {

        recyclerView = (XRecyclerView) findViewById(R.id.rv_admin_comment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new AdminCourseCommentRecyclerViewAdapter(this,comments);
        recyclerView.setAdapter(commentAdapter);
        commentAdapter.notifyDataSetChanged();
        commentSum.setText(comments.size() + "条评论");

        //Log.i("执行","recyclerView！");
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
                                    commentAdapter.setList(comments);
                                    commentAdapter.notifyDataSetChanged();
                                    setCommentSum();
                                    setListListener();
                                    break;
                                case FAIL:
                                    Log.i("刷新", "失败");
                                    break;
                                case ZERO:
                                    Log.i("刷新", "0");
                                    setCommentSum();
                                    setListListener();
                                    break;
                            }
                            recyclerView.refreshComplete();
                        }
                    };

                    new Thread(()->{
                        CUR_PAGE_NUM = 1;
                        Request request = new Request.Builder()
                                .url(getResources().getString(R.string.serverBasePath) +
                                        getResources().getString(R.string.queryCourseEvaluationByCourseId)
                                        + "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE +"&state=0"+"&courseId="+globalcourseid)
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
                                    commentAdapter.notifyDataSetChanged();
                                    setCommentSum();
                                    setListListener();
                                    break;
                                case FAIL:
                                    Log.i("加载", "失败");
                                    break;
                                case ZERO:
                                    Log.i("加载", "0");
                                    recyclerView.setNoMore(true);
                                    setCommentSum();
                                    setListListener();
                                    break;
                            }
                        }
                    };

                    new Thread(()->{
                        CUR_PAGE_NUM++;
                        Request request = new Request.Builder()
                                .url(getResources().getString(R.string.serverBasePath) +
                                        getResources().getString(R.string.queryCourseEvaluationByCourseId)
                                        + "/?pageNum="+ CUR_PAGE_NUM +"&pageSize=" + PAGE_SIZE + "&state=0"+"&courseId="+globalcourseid)
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


    }
    private void queryUpdatedAverageScore(Long courseId){


        //更新card view 中的Score UI
        final Handler UIhandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == 1){
                    //String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());//String类
                    //mText.setText(date);
                    BarScore.setStepSize((float) 0.1);
                    BigDecimal b   =   new   BigDecimal(coursedetail.getScore());
                    float trimScore =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).floatValue();
                    Score.setText(String.valueOf(trimScore));
                    BarScore.setRating(trimScore);

                }
            }
        };


        //mText.setText("更新前");
        final Thread UIthread = new Thread(new Runnable(){

            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                UIhandler.sendMessage(message);
            }

        });

        new Handler().postDelayed(() -> {

            @SuppressLint("HandlerLeak")
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case SUCCESS:
                            Log.i("获取", "成功");
                            UIthread.start();
                            break;
                        case FAIL:
                            Log.i("获取", "失败");
                            break;
                        case ZERO:
                            Log.i("获取", "0");
                            break;
                    }

                }
            };

            new Thread(()->{
                Request request = new Request.Builder()
                        .url(getResources().getString(R.string.serverBasePath) +
                                getResources().getString(R.string.queryCourseByInfoid)
                                + "/?infoId="+globalcourseid)
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
                        Course responseCourse = new Gson().fromJson(data,
                                Course.class);

                        coursedetail = responseCourse;
                        System.out.println("删除后更新分数："+coursedetail.getScore());
                        msg.what = SUCCESS;
                        handler.sendMessage(msg);


                    }
                });
            }).start();
        }, 1500);




    }


    private void sortByLikeNum() {
        Collections.sort(comments, (p1, p2) -> {
            int s1 = p1.getLikeCount();
            int s2 = p2.getLikeCount();
            return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
        });
    }

    private void sortByTimeAsc() {
        Collections.sort(comments, (p1, p2) -> {

            return Objects.requireNonNull(p1.getInfoDate()).compareTo(p2.getInfoDate());

        });
    }

    /**
     * 用户删除评论 数据库更新
     */
    private void deleteComment(Long id, int pos, SweetAlertDialog sDialog) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
                        Log.i("删除评论: ", "失败");
                        sDialog.setTitleText("删除失败")
                                .setContentText("出了点问题，请稍候再试")
                                .showCancelButton(false)
                                .setConfirmText("确定")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        break;
                    case SUCCESS:
                        Log.i("删除评论: ", "成功");
                        sDialog.setTitleText("删除成功")
                                .setContentText("")
                                .showCancelButton(false)
                                .setConfirmText("关闭")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        comments.remove(pos);
                        commentAdapter.notifyDataSetChanged();
                        setCommentSum();
                        queryUpdatedAverageScore(globalcourseid);
                        //mySendBroadCast(s2);//course列表view更新
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
                            + getResources().getString(R.string.deleteCourseEvaluation))
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
