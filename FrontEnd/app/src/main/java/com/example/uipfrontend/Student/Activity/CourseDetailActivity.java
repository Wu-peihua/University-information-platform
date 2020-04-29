package com.example.uipfrontend.Student.Activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Entity.ResponseCourseComment;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.CourseComment;

import com.example.uipfrontend.Student.Adapter.CourseCommentRecyclerViewAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import info.hoang8f.widget.FButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CourseDetailActivity extends AppCompatActivity {

    //private ListView CommentList;

    private TextView commentSum;//评论数目
    private FButton AddComment;//添加评论按钮
    private FButton SubmitBtn;//提交评论按钮
    private EditText CommentEidt ;//编辑评论框
    private RatingBar UserRating;//评分星星

    private TextView Name ;
    private TextView Teacher;
    private TextView Description;
    private TextView Score ;

    private TextView order_by_time; // 评论按时间排序
    private TextView order_by_like; // 评论按热度排序


    private XRecyclerView recyclerView;  //评论列表下拉刷新上拉加载
    private CourseCommentRecyclerViewAdapter commentAdapter;//用户评论适配器

    //private View rootView;
    private Long globalcourseid ;//课程id
    private List<CourseComment> comments =new ArrayList<>();//用户评论列表
    private List<CourseComment> list_order_by_asc= new ArrayList<>(); // ↓：按时间从远到近
    private List<CourseComment> list_order_by_des= new ArrayList<>(); // ↑：按时间从近到远

    //分页请求课程评论数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    private static final int PAGE_SIZE = 6;   //默认一次请求6条数据
    private static int CUR_PAGE_NUM = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            finish();
        }
        setContentView(R.layout.item_course_details);

        //接受点击事件传参数
        Course coursedetail = (Course) Objects.requireNonNull(getIntent().getExtras()).get("coursedetail");

        globalcourseid = coursedetail.getCourseID();
        //Bundle bundle = getIntent().getExtras();
        //String nameID = bundle.getString("name");
        init(coursedetail);
    }

    public void init(Course coursedetail){
        //评论列表初始化
        //initCommentData();
        //课程卡片详情
        initCardView(coursedetail);
        getCommentData();


        //initRecyclerView();
    }


    public  void initCardView(Course course) {

        Name = (TextView)this.findViewById(R.id.coursename);
        Teacher = (TextView)this.findViewById(R.id.TeacherName);
        Description = (TextView)this.findViewById(R.id.courseDescription);
        Score = (TextView)this.findViewById(R.id.RatingScore);

        Name.setText(course.getName());
        Teacher.setText(course.getTeacher());
        Description.setText(course.getDescription());
        Score.setText(String.valueOf(course.getScore()));

        //CommentList = (ListView) findViewById(R.id.CommentList);
        AddComment = (FButton) findViewById(R.id.fbtn_Addcomment);
        commentSum = (TextView) findViewById(R.id.course_comment_sum);


        order_by_time = findViewById(R.id.tv_course_comment_order_by_time);
        order_by_like = findViewById(R.id.tv_course_comment_order_by_like);

        // 按热度从高到低排序
        order_by_like.setOnClickListener(view -> {
            if(!order_by_time.getText().toString().equals("时间")) {
                order_by_like.setTextColor(getResources().getColor(R.color.blue));
                order_by_time.setTextColor(getResources().getColor(R.color.gray));
                order_by_time.setText("时间");
                commentAdapter.setList(comments);
                commentAdapter.notifyDataSetChanged();
            }
        });

        // 按时间排序
        order_by_time.setOnClickListener(view -> {
            order_by_time.setTextColor(getResources().getColor(R.color.blue));
            order_by_like.setTextColor(getResources().getColor(R.color.gray));
            String text = order_by_time.getText().toString();
            if(text.contains("↑")){
                order_by_time.setText("时间↓");
                commentAdapter.setList(list_order_by_asc);
            } else if(text.contains("↓")) {
                order_by_time.setText("时间↑");
                commentAdapter.setList(list_order_by_des);
            } else {
                order_by_time.setText("时间↓");
                commentAdapter.setList(list_order_by_asc);
            }
            commentAdapter.notifyDataSetChanged();
        });


        /***************************添加评论**********************************************/

        View DialogView = getLayoutInflater().inflate(R.layout.dialog_course_rating, null);

        BottomSheetDialog Commentdialog = new BottomSheetDialog(this);
        SubmitBtn = (FButton) DialogView.findViewById(R.id.fbtn_submitComment);
        CommentEidt = (EditText) DialogView.findViewById(R.id.UserEidtComment);
        UserRating = (RatingBar) DialogView.findViewById(R.id.SetRating);


        AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogView.getParent()!=null){
                    ((ViewGroup)DialogView.getParent()).removeView(DialogView);
                }
                Commentdialog.setContentView(DialogView);
                Commentdialog.show();
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
                final long testuserid = 4;//默认用户评论id
                String commentContent = CommentEidt.getText().toString();
                Integer commentRating = (int)UserRating.getRating();
                Date commentDate = new Date();


                //更新数据库记录
                CourseComment newcomment = new CourseComment(testuserid,globalcourseid,commentContent,commentRating,commentDate);
                insertComment(newcomment);

                //更新几个列表
                comments.add(newcomment);
                list_order_by_asc.add(newcomment);
                list_order_by_des.add(0, newcomment);

                commentAdapter.notifyDataSetChanged();
                commentSum.setText(comments.size() + "条评论");

                /*System.out.println("用户评分"+UserRating.getRating());
                System.out.println("用户描述内容"+CommentEidt.getText().toString());
                Log.i("submit","成功添加评分");
                 */
                Commentdialog.dismiss();
                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();

            }
        });


    }

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
                    System.out.println("课程评论请求返回数据:"+data);

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
                    //两个排序列表同步更新
                            //升序
                            list_order_by_asc = new ArrayList<>();
                            list_order_by_asc.addAll(comments);
                            Collections.sort(list_order_by_asc, new Comparator<CourseComment>() {
                                DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                @Override
                                public int compare(CourseComment p1, CourseComment p2) {
                                    //try {
                                    return  p1.getInfoDate().compareTo(p2.getInfoDate());
                                    //return f.parse(p1.getInfoDate().toString()).compareTo(f.parse(p2.getInfoDate().toString()));
                                    //} catch (ParseException e) {
                                    //    throw new IllegalArgumentException(e);
                                    // }
                                }
                            });

                            //降序排列
                            Collections.sort(comments, new Comparator<CourseComment>() {
                                @Override
                                public int compare(CourseComment c1, CourseComment c2) {
                                    int s1 = c1.getLikeCount();
                                    int s2 = c2.getLikeCount();
                                    return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
                                }
                            });

                            //count = mTags.size();
                            list_order_by_des = new ArrayList<>();
                            list_order_by_des.addAll(comments);
                            Collections.sort(list_order_by_des, new Comparator<CourseComment>() {
                                DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                @Override
                                public int compare(CourseComment p1, CourseComment p2) {
                                    //try {
                                    //    return f.parse(p2.getInfoDate().toString()).compareTo(f.parse(p1.getInfoDate().toString()));
                                    // } catch (ParseException e) {
                                    //     throw new IllegalArgumentException(e);
                                    // }
                                    return p2.getInfoDate().compareTo(p1.getInfoDate());
                                }
                            });

                            Collections.sort(comments, new Comparator<CourseComment>() {
                                @Override
                                public int compare(CourseComment p1, CourseComment p2) {
                                    int s1 = p1.getLikeCount();
                                    int s2 = p2.getLikeCount();
                                    return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
                                }
                            });


                            System.out.println("评论列表为:"+comments.toString());
                        }
                        handler.sendMessage(msg);
                        Log.i("获取: ", String.valueOf(comments.size()));
                    }
                }
            });
        }).start();


    }

    public void initCommentData() {

        /*
            mTags.add (new CourseComment(2001,"LinussPP", "2020-4-20 22:44", "有趣", 4.50,10));
            mTags.add (new CourseComment(2002,"ZhouKK", "2020-4-21 22:44", "学到了很多", 4.50,12));
            mTags.add (new CourseComment(2003,"MandyWong", "2020-4-22 22:44", "没意思", 3.50,13));
            mTags.add (new CourseComment(3008,"LarryChen", "2020-4-23 22:44", "课程难度大", 3.50,20));
            mTags.add (new CourseComment(4010,"LinYii", "2020-4-24 22:44", "作业量惊人", 2.50,12));
            mTags.add (new CourseComment(2020,"Oliver", "2020-4-25 11:44", "不推荐", 1.50,10));
            mTags.add (new CourseComment(2034,"Patric", "2020-4-25 10:44", "推荐", 4.50,2));
*/
        DateFormat datefomat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        String datestring = "2020-4-1 20:35";
        Date commentDate = null;
        try {
            commentDate = datefomat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i=0;i<10;i++) {
            comments.add(new CourseComment((long) 1, (long) 3, "interesting", 4, commentDate));

        }
        //升序
        list_order_by_asc = new ArrayList<>();
        list_order_by_asc.addAll(comments);
        Collections.sort(list_order_by_asc, new Comparator<CourseComment>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @Override
            public int compare(CourseComment p1, CourseComment p2) {
                //try {
                   return  p1.getInfoDate().compareTo(p2.getInfoDate());
                    //return f.parse(p1.getInfoDate().toString()).compareTo(f.parse(p2.getInfoDate().toString()));
                //} catch (ParseException e) {
                //    throw new IllegalArgumentException(e);
               // }
            }
        });

        //降序排列
        Collections.sort(comments, new Comparator<CourseComment>() {
            @Override
            public int compare(CourseComment c1, CourseComment c2) {
                int s1 = c1.getLikeCount();
                int s2 = c2.getLikeCount();
                return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
            }
        });

        //count = mTags.size();
        list_order_by_des = new ArrayList<>();
        list_order_by_des.addAll(comments);
        Collections.sort(list_order_by_des, new Comparator<CourseComment>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @Override
            public int compare(CourseComment p1, CourseComment p2) {
                //try {
                //    return f.parse(p2.getInfoDate().toString()).compareTo(f.parse(p1.getInfoDate().toString()));
               // } catch (ParseException e) {
               //     throw new IllegalArgumentException(e);
               // }
                return p2.getInfoDate().compareTo(p1.getInfoDate());
            }
        });

        Collections.sort(comments, new Comparator<CourseComment>() {
            @Override
            public int compare(CourseComment p1, CourseComment p2) {
                int s1 = p1.getLikeCount();
                int s2 = p2.getLikeCount();
                return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
            }
        });

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

    public void insertComment(CourseComment courseComment){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //建立client
                final OkHttpClient[] client = {new OkHttpClient()};

                //将传送实体类转为string类型的键值对，设置日期格式，与后端实体类的Date对应
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm").create();
                String json = gson.toJson(courseComment);

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
                        System.out.println("请求失败："+e.getMessage());
                        //Log.i("RESPONSE:","fail"+e.getMessage());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("请求成功："+response.body().string());
                        //Log.i("RESPONSE:",response.body().string());
                    }

                });
            }
        }).start();
    }




}