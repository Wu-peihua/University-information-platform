package com.example.uipfrontend.Student;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import info.hoang8f.widget.FButton;

public class CourseDetailActivity extends AppCompatActivity {

    //private ListView CommentList;
    private FButton AddComment;//添加评论按钮
    private FButton SubmitBtn;//提交评论按钮
    private EditText CommentEidt ;//编辑评论框
    private RatingBar UserRating;//评分星星

    private TextView Name ;
    private TextView Teacher;
    private TextView Description;
    private TextView Score ;


    private XRecyclerView recyclerView;  //评论列表下拉刷新上拉加载
    private CourseCommentRecyclerViewAdapter commentAdapter;//用户评论适配器

    //private View rootView;
    private List<CourseComment> mTags=new ArrayList<>();//用户评论列表


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_course_details);

        //接受点击事件传参数
        Course course = (Course) Objects.requireNonNull(getIntent().getExtras()).get("coursedetail");

        //Bundle bundle = getIntent().getExtras();
        //String nameID = bundle.getString("name");

        //课程卡片详情
        initCardView(course);
        //评论列表初始化
        initCommentData();
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
                CourseComment newcomment = new CourseComment(commentid,UserName,new Date(),CommentEidt.getText().toString(),UserRating.getRating(),0);
                mTags.add(newcomment);
                commentAdapter.notifyDataSetChanged();

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

            mTags.add (new CourseComment(2001,"LinussPP", new Date(), "有趣", 4.50,10));
            mTags.add (new CourseComment(2002,"ZhouKK", new Date(), "学到了很多", 4.50,12));
            mTags.add (new CourseComment(2003,"MandyWong", new Date(), "没意思", 3.50,13));
            mTags.add (new CourseComment(3008,"LarryChen", new Date(), "课程难度大", 3.50,20));
            mTags.add (new CourseComment(4010,"LinYii", new Date(), "作业量惊人", 2.50,12));
            mTags.add (new CourseComment(2020,"Oliver", new Date(), "不推荐", 1.50,10));
            mTags.add (new CourseComment(2034,"Patric", new Date(), "推荐", 4.50,2));

        //count = mTags.size();
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