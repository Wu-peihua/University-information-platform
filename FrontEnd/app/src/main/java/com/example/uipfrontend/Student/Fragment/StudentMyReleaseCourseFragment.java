package com.example.uipfrontend.Student.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Activity.AddResActivity;
import com.example.uipfrontend.CommonUser.Adapter.MyReleaseResInfoAdapter;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Adapter.StudentMyCommentCourseAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;

public class StudentMyReleaseCourseFragment extends Fragment {

    XRecyclerView xrv_studentReleaseCourse;

    private View rootView;
    private XRecyclerView xRecyclerView;
    private StudentMyCommentCourseAdapter adapter;
    private List<CourseComment> list;
    private int commentPos;
    private double commentScore;
    private  String commentContent;

    private FButton SubmitBtn;//提交评论按钮
    private EditText CommentEidt ;//编辑评论框
    private RatingBar UserRating;//评分星星

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_my_release_course, null);
            initData();
            initXRecyclerView();//我的评论列表
            //修改评论对话框
        }
        return rootView;
    }

    public void initData() {
        list = new ArrayList<>();
        list.add(new CourseComment("数据库原理",2001,"ChilamZ", "2020-4-25 22:44", "作业多", 2.50,10));
        list.add(new CourseComment("计算机网络",2002,"ChilamZ", "2020-4-25 22:44", "课程有趣", 4.50,6));
        list.add(new CourseComment("操作系统",2003,"ChilamZ", "2020-4-25 22:44", "课程难度大", 3.50,5));

    }

    public void initXRecyclerView() {
        xRecyclerView = rootView.findViewById(R.id.xrv_studentReleaseCourse);
        adapter = new StudentMyCommentCourseAdapter(list, rootView.getContext());
        xRecyclerView.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext()) {
            @Override
            public boolean canScrollVertically() {
                if (list == null || list.size() == 0)
                    return false;
                return super.canScrollVertically();
            }
        };
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                xRecyclerView.setNoMore(true);
            }
        });

        //编辑评论
        View DialogView = getLayoutInflater().inflate(R.layout.dialog_course_comment_edit, null);

        BottomSheetDialog Editdialog = new BottomSheetDialog(getContext());
        SubmitBtn = (FButton) DialogView.findViewById(R.id.my_comment_fbtn_submit);
        CommentEidt = (EditText) DialogView.findViewById(R.id.my_comment_edit_content);
        UserRating = (RatingBar) DialogView.findViewById(R.id.my_comment_set_rating);


        adapter.setOnItemDeleteClickListener(new StudentMyCommentCourseAdapter.OnItemDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(rootView.getContext())
                        .setTitle("提示")
                        .setMessage("是否确定删除该记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(rootView.getContext(), "记录删除成功", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
            }
        });

        adapter.setOnItemModifyClickListener(new StudentMyCommentCourseAdapter.OnItemModifyClickListener() {
            @Override
            public void onModifyClick(int position) {
                commentPos = position;//获取修改的位置
                if(DialogView.getParent()!=null){
                    ((ViewGroup)DialogView.getParent()).removeView(DialogView);
                }
                //预设原始评分数据
                CommentEidt.setText(list.get(commentPos).getContent());
                UserRating.setRating((float)list.get(commentPos).getScore());
                Editdialog.setContentView(DialogView);
                Editdialog.show();

                Log.i("click","点击修改评论按钮");
            }

        });

        UserRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
                                                {
                                                    @Override
                                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                        UserRating.setRating(rating);
                                                        commentScore = rating;
                                                        Log.i("set","修改ratingbar");
                                                    }
                                                }
        );

        /*************提交评论************************************************************/
        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CourseComment newComment = list.get(commentPos);
                newComment.setContent(CommentEidt.getText().toString());
                newComment.setScore(commentScore);
                adapter.notifyDataSetChanged();//更新数据

                //传送至数据库更新课程详情数据
                Editdialog.dismiss();
                Toast.makeText(getContext(), "修改成功！", Toast.LENGTH_SHORT).show();
                //添加到列表中
                /*
                Integer commentid = (int) Math.random()*1000;
                String UserName = "LIZZ";
                CourseComment newcomment = new CourseComment(commentid,UserName,new Date(),CommentEidt.getText().toString(),UserRating.getRating(),0);
                mTags.add(newcomment);
                commentAdapter.notifyDataSetChanged();
                commentSum.setText(mTags.size() + "条评论");

                System.out.println("用户评分"+UserRating.getRating());
                System.out.println("用户描述内容"+CommentEidt.getText().toString());
                Log.i("submit","成功添加评分");


                //mTags.add(userid,rating)
                Commentdialog.dismiss();
                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
*/
            }
        });

    }
       /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 1)
                if (resultCode == 1) {
                    list.remove(commentPos);
                    CourseComment receiveData = (CourseComment) data.getSerializableExtra("resInfo");
                    list.add(0, receiveData);
                    adapter.notifyDataSetChanged();
                }
        }

        */


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == 1) {
                list.remove(commentPos);
                CourseComment receiveData = (CourseComment) data.getSerializableExtra("resInfo");
                list.add(0, receiveData);
                adapter.notifyDataSetChanged();
            }
    }
}
