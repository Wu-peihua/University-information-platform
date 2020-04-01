package com.example.uipfrontend.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.Admin.Adapter.CommentAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class AdminCourseDetailActivity extends AppCompatActivity {

    private ListView CommentList;
    private Button AddComment;
    private Button SubmitBtn;
    private EditText CommentEidt ;
    private RatingBar UserRating;

    private TextView Name ;
    private TextView Teacher;
    private TextView Description;
    private TextView Score ;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_course_details);

        /****************************课程详情与评论列表初始化************************************/
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");

        Name = (TextView)this.findViewById(R.id.admin_coursename);
        Teacher = (TextView)this.findViewById(R.id.admin_Teacher);
        Description = (TextView)this.findViewById(R.id.admin_courseDescription);
        Score = (TextView)this.findViewById(R.id.admin_RatingScore);

        Name.setText(name);
        Teacher.setText("ZHU");
        Description.setText("Good Job!");
        Score.setText("5.0");

        CommentList = (ListView) findViewById(R.id.admin_CommentList);
        AddComment = (Button) findViewById(R.id.admin_Editcourse);

        initCommmentLists();

        /***************************添加评论**********************************************/

        View DialogView = getLayoutInflater().inflate(R.layout.dialog_course_rating, null);

        BottomSheetDialog Commentdialog = new BottomSheetDialog(this);
        SubmitBtn = (Button) DialogView.findViewById(R.id.submitComment);
        CommentEidt = (EditText) DialogView.findViewById(R.id.UserEidtComment);
        UserRating = (RatingBar) DialogView.findViewById(R.id.SetRating);


        AddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Commentdialog.setContentView(DialogView);
//                Commentdialog.show();
                Log.i("click","点击了编辑按钮");
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
                System.out.println("用户评分"+UserRating.getRating());

                System.out.println("用户描述内容"+CommentEidt.getText().toString());
                Log.i("submit","成功添加评分");

                Commentdialog.dismiss();
                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void initCommmentLists() {

        /****************************评论列表********************************/

        ArrayList<CourseComment> commentsList = new ArrayList<>();


        //添加list数据项
        for(int i = 1;i<=3;i++) {
            commentsList.add(new CourseComment("Yuzz", "2019-12-31", "学到了好多", 2.50));
            commentsList.add(new CourseComment("LinussLu", "2020-01-03", "Linux有趣", 3.50));
            commentsList.add(new CourseComment("NetworkGo", "2020-02-02", "噢秃头！", 4.50));
        }

        /* 创建设置课程数据适配器 */
        CommentAdapter adapter = new CommentAdapter(this, R.layout.item_course_comment, commentsList);

        CommentList.setAdapter(adapter);

    }
}