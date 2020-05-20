package com.example.uipfrontend.Student.Activity;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.CommonUser.Activity.PostDetailActivity;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Entity.ResponseCourse;
import com.example.uipfrontend.Entity.ResponseCourseComment;
import com.example.uipfrontend.Entity.ResponseUserInfo;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.Entity.UserRecord;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.CourseComment;

import com.example.uipfrontend.Student.Adapter.CourseCommentRecyclerViewAdapter;
import com.example.uipfrontend.Utils.UserOperationRecord;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.uipfrontend.Utils.UserOperationRecord.isFastClick;
import static com.example.uipfrontend.Utils.UserOperationRecord.isNumber;

public class CourseDetailActivity extends AppCompatActivity {


    private TextView commentSum;//评论数目
    private FButton AddComment;//添加评论按钮
    private FButton SubmitBtn;//提交评论按钮
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
    private CourseCommentRecyclerViewAdapter commentAdapter;//用户评论适配器

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

    // 课程评分变化
    private static final String s1 = "insertCoursecomment";
    private static final String s2 = "deleteCourseComment";
    private static final String s3 = "increaseLikeInCommentDetail";
    private static final String s4 = "decreaseLikeInCommentDetail";


    private  Course coursedetail;//全局的课程


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            finish();
        }
        setContentView(R.layout.item_course_details);

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
        //user.setUniversityId(1);//设置默认学校测试添加评论
        initCardView(coursedetail);
        getCommentData();

    }

    public void getUserData(){
        //courses = new ArrayList<>();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message){
                switch (message.what){
                    case SUCCESS:
                        Log.i("获取: ", "成功");
                        //初始化列表

                        break;

                    case FAIL:
                        Log.i("获取: ", "失败");
                        break;

                }
            }
        };

        //查询评论数据
        new Thread(()->{
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.getUser)
                            + "/?userId="+4)
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
                    System.out.println("请求返回数据:"+data);

                    //日期格式化
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();
                    ResponseUserInfo responseUserInfo = gson.fromJson(data,
                            ResponseUserInfo.class);

                    if(responseUserInfo==null) {
                        System.out.println("response user info获取失败");
                    }

                    UserInfo tempuser = responseUserInfo.getUserInfo();
                    System.out.println("user university id:"+user.getUniversityId());
                    //获取新的comments --更新列表


                    if (tempuser == null){
                        System.out.println("没有数据");
                    }
                    else {

                            msg.what = SUCCESS;

                            //System.out.println("评论列表为:"+comments.toString());
                        }
                        handler.sendMessage(msg);

                    }

            });
        }).start();


    }


    public  void initCardView(Course course) {

        Name = (TextView)this.findViewById(R.id.coursename);
        Teacher = (TextView)this.findViewById(R.id.TeacherName);
        Description = (TextView)this.findViewById(R.id.courseDescription);
        Score = (TextView)this.findViewById(R.id.RatingScore);
        BarScore = (RatingBar)this.findViewById(R.id.courseRatingBar);
        CourseImage = (ImageView)this.findViewById(R.id.courseImage);

        //CommentList = (ListView) findViewById(R.id.CommentList);
        AddComment = (FButton) findViewById(R.id.fbtn_Addcomment);
        commentSum = (TextView) findViewById(R.id.course_comment_sum);


        order_by_time = findViewById(R.id.tv_course_comment_order_by_time);
        order_by_like = findViewById(R.id.tv_course_comment_order_by_like);

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


        /***************************添加评论**********************************************/

        View DialogView = getLayoutInflater().inflate(R.layout.dialog_course_rating, null);

        BottomSheetDialog Commentdialog = new BottomSheetDialog(this);
        SubmitBtn = (FButton) DialogView.findViewById(R.id.fbtn_submitComment);
        CommentEidt = (EditText) DialogView.findViewById(R.id.UserEidtComment);
        UserRating = (RatingBar) DialogView.findViewById(R.id.SetRating);


        AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //评分判断
                //System.out.println("user university id:"+user.getUniversityId());
                //System.out.println("global course id:"+globalcourseschoolId);

                if(user.getUserId()==null||!user.getUniversityId().equals(globalcourseschoolId)){

                     Toast.makeText(getApplicationContext(), "非本校学生不能参与课程评分噢！", Toast.LENGTH_SHORT).show();
               }
                else {
                    if (DialogView.getParent() != null) {
                        ((ViewGroup) DialogView.getParent()).removeView(DialogView);
                    }
                    Commentdialog.setContentView(DialogView);
                    Commentdialog.show();
                }
                //Log.i("click","点击了我要评论按钮");
            }
        });


        UserRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
                                                {
                                                    @Override
                                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                        UserRating.setRating(rating);
                                                        //Log.i("set","修改ratingbar");
                                                    }
                                                }
        );

        /*************提交评论************************************************************/
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加到列表中
                //CourseComment newcomment = new CourseComment(commentid,UserName,"2020-4-25 22:44",CommentEidt.getText().toString(),UserRating.getRating(),0);
                //commentatorid + courseid + content+ score+ date
                //final long testuserid = 4;//默认用户评论id
                long globalUserid = user.getUserId();
                String commentContent = CommentEidt.getText().toString();
                Float commentRating = UserRating.getRating();
                Date commentDate = new Date();


                //更新数据库记录
                CourseComment newcomment = new CourseComment(globalUserid,globalcourseid,commentContent,commentRating,commentDate);

                newcomment.setLikeCount(0);
                newcomment.setBadReportCount(0);
                newcomment.setFromName(user.getUserName());
                newcomment.setPortrait(user.getPortrait());

                //更新几个列表
                //comments.add(newcomment);
                insertComment(newcomment);
                commentAdapter.notifyDataSetChanged();
                //commentSum.setText(comments.size() + "条评论");
                Commentdialog.dismiss();
                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();

            }
        });


    }

    /**
     * 评论的点赞删除监听
     */
    private void setListListener() {

        commentAdapter.setOnLikeSelectListener((isSelected, pos) -> {

            if(isSelected){
                comments.get(pos).setLikeCount(comments.get(pos).getLikeCount() + 1);
                UserRecord record = new UserRecord();
                record.setUserId(user.getUserId());
                //System.out.println("插入评论点赞id："+comments.get(pos).getInfoId());
                record.setToId(comments.get(pos).getInfoId());
                record.setTag(1);
                record.setType(5);
                UserOperationRecord.insertRecord(this, record, user);
            }
            else {
                comments.get(pos).setLikeCount(comments.get(pos).getLikeCount() - 1);
                //System.out.println("当前user like record:"+user.getLikeRecord());
                Long infoId = user.getLikeRecord().get("course_comment"+comments.get(pos).getInfoId());
                //System.out.println("当前删除info:"+infoId);
                UserOperationRecord.deleteRecord(this, infoId);
                user.getLikeRecord().remove("course_comment"+comments.get(pos).getInfoId());

            }

        });

        //adpter删除用户评论/举报用户评论
        commentAdapter.setOnMoreClickListener((view, pos) -> {
            //System.out.println("new course comment id"+comments.get(pos).getInfoId());
            if (user.getUserId().equals(comments.get(pos).getCommentatorId())) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("确定要删除这条评论吗？")
                        .setConfirmText("确定")
                        .setCancelText("点错了")
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
            }else {

                if (user.getReportRecord().containsKey("course_comment"+comments.get(pos).getInfoId())) {
                    Toast.makeText(this, "您已举报过，请等待处理", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("如果该条评论含有不恰当的内容，请点击确定")
                            .setPositiveButton("确定", (dialog1, which) -> {
                                comments.get(pos).setBadReportCount(comments.get(pos).getBadReportCount() + 1);

                                UserRecord record = new UserRecord();
                                record.setUserId(user.getUserId());
                                record.setToId(comments.get(pos).getInfoId());//被举报人
                                record.setTag(2);
                                record.setType(5);
                                UserOperationRecord.insertRecord(this, record, user);

                                Toast.makeText(this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("取消", null)
                            .setCancelable(false)
                            .create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
                }
            }
        });

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
        };

        //查询评论数据
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


    public void initRecyclerView() {

        recyclerView = (XRecyclerView) findViewById(R.id.rv_student_comment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CourseCommentRecyclerViewAdapter(this,comments);
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

    /**用户添加新评分 数据库更新
     * */
    private void insertComment(CourseComment comment) {

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
                        Log.i("插入评论: ", "失败");
                        Toast.makeText(CourseDetailActivity.this, "网络出了点问题，请稍候再试",
                                Toast.LENGTH_LONG).show();
                        break;
                    case SUCCESS:
                        Log.i("插入评论: ", "成功");
                        if (order_by_time.getText().toString().equals("时间↑")) {
                            comments.add(0, comment);
                        } else {
                            comments.add(comment);
                        }
                        commentAdapter.setList(comments);
                        commentAdapter.notifyDataSetChanged();
                        queryUpdatedAverageScore(globalcourseid);
                        setCommentSum();
                        getCommentData();
                        mySendBroadCast(s1);
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(()->{
            Message msg = new Message();
            //建立client
            final OkHttpClient[] client = {new OkHttpClient()};

            //将传送实体类转为string类型的键值对，设置日期格式，与后端实体类的Date对应
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();
            String json = gson.toJson(comment);

            System.out.println("json:"+json);
            //设置请求体并设置contentType
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"),json);
            //请求
            Request request=new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.insertCourseComment))
                    .post(requestBody)
                    .build();
            //新建call联结client和request
            Call call= client[0].newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //请求失败的处理
                    msg.what = FAIL;
                    handler.sendMessage(msg);
                    Log.i("RESPONSE:","fail"+e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("请求成功："+response.body().string());
                    msg.what=SUCCESS;
                    handler.sendMessage(msg);
                }

            });

        }).start();
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
                        mySendBroadCast(s2);//course列表view更新
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
     * 描述：自定义广播
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {

        private void solve(Intent intent, boolean isIncrease, int where) {
            int pos = intent.getIntExtra("pos", -1);
            if (pos != -1) {
                /*switch (where) {
                    case 1:
                        int n = list.get(pos).getReplyNumber();
                        n = isIncrease ? n+1 : n-1;
                        list.get(pos).setReplyNumber(n);
                        break;
                    case 2:
                        break;
                }

                 */
                //adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (Objects.requireNonNull(intent.getAction())) {
                // case s3:
                //     updateAverageScore(coursedetail);
                //     break;
                /*case s5: solve(intent, true, 1); break;
                case s6: solve(intent, false, 1); break;
                case s7: solve(intent, true, 2); break;
                case s8: solve(intent, false, 2); break;

                 */
            }
        }
    }

    /**
     * 描述：注册广播
     */
    private void registerBroadCast() {
        //receiver1 = new MyBroadcastReceiver();
        //registerReceiver(receiver1, new IntentFilter(s5));

        //receiver3 = new MyBroadcastReceiver();
        //registerReceiver(receiver3, new IntentFilter(s3));

    }

    /**
     * 描述：取消广播注册
     */
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver3);
        //unregisterReceiver(receiver2);
        //unregisterReceiver(receiver3);
        //unregisterReceiver(receiver4);
    }

    /**
     * 描述：评论、点赞后发送广播(给StudentCommentFragment)
     */
    private void mySendBroadCast(String s) {
        Intent intent = new Intent();
        intent.putExtra("pos", coursePos);
        intent.setAction(s);
        sendBroadcast(intent);
    }
}