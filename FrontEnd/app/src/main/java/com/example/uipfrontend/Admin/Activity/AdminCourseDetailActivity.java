package com.example.uipfrontend.Admin.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.Admin.Adapter.AdminCourseCommentRecyclerViewAdapter;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.R;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

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

//import com.example.uipfrontend.Student.Adapter.CourseCommentRecyclerViewAdapter;

public class AdminCourseDetailActivity extends AppCompatActivity {

    //private ListView CommentList;
    private TextView commentSum;//评论数目
    private FButton EditCourse;//编辑课程


    private TextView Name ;
    private TextView Teacher;
    private TextView Description;
    private TextView Score ;

    private TextView order_by_time; // 评论按时间排序
    private TextView order_by_like; // 评论按热度排序


    private XRecyclerView recyclerView;  //评论列表下拉刷新上拉加载
    private AdminCourseCommentRecyclerViewAdapter commentAdapter;//用户评论适配器

    //private View rootView;
    private List<CourseComment> mTags=new ArrayList<>();//用户评论列表
    private List<CourseComment> list_order_by_asc; // ↓：按时间从远到近
    private List<CourseComment> list_order_by_des; // ↑：按时间从近到远


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_admin_course_details);

        //接受点击事件传参数
        Course course = (Course) Objects.requireNonNull(getIntent().getExtras()).get("coursedetail");

        //Bundle bundle = getIntent().getExtras();
        //String nameID = bundle.getString("name");

        //评论列表初始化
        initCommentData();
        //课程卡片详情
        initCardView(course);

        initRecyclerView();

        //initCommmentLists();


    }

    private  void initCardView(Course course) {

        Name = (TextView)this.findViewById(R.id.admincoursename);
        Teacher = (TextView)this.findViewById(R.id.adminTeacherName);
        Description = (TextView)this.findViewById(R.id.admincourseDescription);
        Score = (TextView)this.findViewById(R.id.adminRatingScore);

        Name.setText(course.getName());
        Teacher.setText(course.getTeacher());
        Description.setText(course.getDescription());
        Score.setText(String.valueOf(course.getScore()));

        EditCourse = (FButton) findViewById(R.id.admin_fbtn_Editcourse) ;
        commentSum = (TextView) findViewById(R.id.admin_course_comment_sum);

        commentSum.setText(mTags.size() + "条评论");
        order_by_time = findViewById(R.id.admin_tv_course_comment_order_by_time);
        order_by_like = findViewById(R.id.admin_tv_course_comment_order_by_like);

        // 按热度从高到低排序
        order_by_like.setOnClickListener(view -> {
            if(!order_by_time.getText().toString().equals("时间")) {
                order_by_like.setTextColor(getResources().getColor(R.color.blue));
                order_by_time.setTextColor(getResources().getColor(R.color.gray));
                order_by_time.setText("时间");
                commentAdapter.setList(mTags);
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
                startActivity(intent);
                Log.i("click","点击了编辑课程按钮");
            }
        });



    }



    public void initCommentData() {

        mTags.add (new CourseComment(2001,"LinussPP", "2020-4-20 22:44", "有趣", 4.50,10));
        mTags.add (new CourseComment(2002,"ZhouKK", "2020-4-21 22:44", "学到了很多", 4.50,12));
        mTags.add (new CourseComment(2003,"MandyWong", "2020-4-22 22:44", "没意思", 3.50,13));
        mTags.add (new CourseComment(3008,"LarryChen", "2020-4-23 22:44", "课程难度大", 3.50,20));
        mTags.add (new CourseComment(4010,"LinYii", "2020-4-24 22:44", "作业量惊人", 2.50,12));
        mTags.add (new CourseComment(2020,"Oliver", "2020-4-25 11:44", "不推荐", 1.50,10));
        mTags.add (new CourseComment(2034,"Patric", "2020-4-25 10:44", "推荐", 4.50,2));


        //升序
        list_order_by_asc = new ArrayList<>();
        list_order_by_asc.addAll(mTags);
        Collections.sort(list_order_by_asc, new Comparator<CourseComment>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @Override
            public int compare(CourseComment p1, CourseComment p2) {
                try {
                    return f.parse(p1.getCommentDate()).compareTo(f.parse(p2.getCommentDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        //降序排列
        Collections.sort(mTags, new Comparator<CourseComment>() {
            @Override
            public int compare(CourseComment c1, CourseComment c2) {
                int s1 = c1.getLikeCount();
                int s2 = c2.getLikeCount();
                return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
            }
        });

        //count = mTags.size();
        list_order_by_des = new ArrayList<>();
        list_order_by_des.addAll(mTags);
        Collections.sort(list_order_by_des, new Comparator<CourseComment>() {
            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @Override
            public int compare(CourseComment p1, CourseComment p2) {
                try {
                    return f.parse(p2.getCommentDate()).compareTo(f.parse(p1.getCommentDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });

        Collections.sort(mTags, new Comparator<CourseComment>() {
            @Override
            public int compare(CourseComment p1, CourseComment p2) {
                int s1 = p1.getLikeCount();
                int s2 = p2.getLikeCount();
                return s1 < s2 ? s1 : (s1 == s2) ? 0 : -1;
            }
        });

    }

    private void initRecyclerView() {

        recyclerView = (XRecyclerView) findViewById(R.id.rv_admin_comment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new AdminCourseCommentRecyclerViewAdapter(this,mTags);
        recyclerView.setAdapter(commentAdapter);


        //Log.i("执行","recyclerView！");
        recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                Log.i("当前Item数目",String.valueOf(mTags.size()));
                recyclerView.refreshComplete();

            }

            @Override
            public void onLoadMore() {

                recyclerView.setNoMore(true);
            }
        });

    }
}
