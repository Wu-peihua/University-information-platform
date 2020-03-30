package com.example.uipfrontend.Admin.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CommentAdapter extends ArrayAdapter<CourseComment> {
    private int resourceId;


    public CommentAdapter(Context context, int textViewResourceId, List<CourseComment> commentList){
        super(context, textViewResourceId, commentList);
        this.resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CourseComment comment = getItem(position);
        View view = LayoutInflater.from(this.getContext()).inflate(resourceId, null);

        //ImageView courseImage = (ImageView)view.findViewById(R.id.courseImage);


        TextView userName = (TextView)view.findViewById(R.id.UserName);
        TextView commentDate = (TextView)view.findViewById(R.id.CommentDate);
        TextView commentContent = (TextView)view.findViewById(R.id.CommentContent);
        RatingBar score = (RatingBar) view.findViewById(R.id.userRatingBar);
        Button BtnLike = (Button) view.findViewById(R.id.BtnLike);
        Button BtnBadReport = (Button) view.findViewById(R.id.BtnBadReport);
        TextView LikeCounts = (TextView) view.findViewById(R.id.LikeCounts);

        //courseImage.setImageResource(course.getImageurl());
        userName.setText(comment.getUserName());
        commentDate.setText(comment.getCommentDate());
        commentContent.setText(comment.getContent());
        score.setRating((float) comment.getScore());


        //点赞按钮与举报按钮
        BtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = comment.getLikeCount();
                comment.setLikeCount(cur+1);
                LikeCounts.setText(String.valueOf(cur+1));
                Log.i("当前点赞数:",String.valueOf(comment.getLikeCount()));

            }
        });

        //举报提示框
        BtnBadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("您确定举报这条评论吗？")
                        .setContentText("举报后不能取消！")
                        .setConfirmText("确认")
                        .setCancelText("取消")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("举报成功！")
                                        .showCancelButton(false)
                                        .setContentText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .show();

                Log.i("click","举报一次！");
            }
        });

        return view;
    }
}