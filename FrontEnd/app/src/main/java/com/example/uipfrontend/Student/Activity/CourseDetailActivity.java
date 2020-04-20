package com.example.uipfrontend.Student.Activity;


import android.os.Bundle;
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
import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.CourseComment;

import com.example.uipfrontend.Student.Adapter.CourseCommentRecyclerViewAdapter;
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
    private List<CourseComment> mTags=new ArrayList<>();//用户评论列表
    private List<CourseComment> list_order_by_asc; // ↓：按时间从远到近
    private List<CourseComment> list_order_by_des; // ↑：按时间从近到远


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_course_details);

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

        commentSum.setText(mTags.size() + "条评论");
        order_by_time = findViewById(R.id.tv_course_comment_order_by_time);
        order_by_like = findViewById(R.id.tv_course_comment_order_by_like);

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
                Log.i("click","点击了我要评论按钮");
            }
        });


        UserRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                UserRating.setRating(rating);
                Log.i("set","修改ratingbar");
            }
        }
        );

        /*************提交评论************************************************************/
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //添加到列表中
                Integer commentid = (int) Math.random()*1000;
                String UserName = "LIZZ";
                CourseComment newcomment = new CourseComment(commentid,UserName,"2020-4-25 22:44",CommentEidt.getText().toString(),UserRating.getRating(),0);
                //更新几个列表
                mTags.add(newcomment);
                list_order_by_asc.add(newcomment);
                list_order_by_des.add(0, newcomment);

                commentAdapter.notifyDataSetChanged();
                commentSum.setText(mTags.size() + "条评论");

                System.out.println("用户评分"+UserRating.getRating());
                System.out.println("用户描述内容"+CommentEidt.getText().toString());
                Log.i("submit","成功添加评分");


                //mTags.add(userid,rating)
                Commentdialog.dismiss();
                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();

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

        recyclerView = (XRecyclerView) findViewById(R.id.rv_student_comment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        commentAdapter = new CourseCommentRecyclerViewAdapter(this,mTags);
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


    /*
    public void initCommmentLists() {



        ArrayList<CourseComment> commentsList = new ArrayList<>();


        //添加list数据项
        for(int i = 1;i<=3;i++) {
            commentsList.add(new CourseComment("Yuzz", "2019-12-31", "学到了好多", 2.50));
            commentsList.add(new CourseComment("LinussLu", "2020-01-03", "Linux有趣", 3.50));
            commentsList.add(new CourseComment("NetworkGo", "2020-02-02", "噢秃头！", 4.50));
        }


        CommentAdapter adapter = new CommentAdapter(this, R.layout.item_course_comment, commentsList);

        CommentList.setAdapter(adapter);

    }
    */

}