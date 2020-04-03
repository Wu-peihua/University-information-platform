package com.example.uipfrontend.Student.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CourseCommentRecyclerViewAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<CourseComment> mTags = new ArrayList<>();
    ;

    public CourseCommentRecyclerViewAdapter(Context context, List<CourseComment> list) {
        this.context = context;
        this.mTags = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView commentDate;
        TextView commentContent;
        RatingBar score;
        Button BtnLike;
        Button BtnBadReport;
        TextView LikeCounts;
        ImageView userimg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);


            userName = (TextView) itemView.findViewById(R.id.UserName);
            commentDate = (TextView) itemView.findViewById(R.id.CommentDate);
            commentContent = (TextView) itemView.findViewById(R.id.CommentContent);
            score = (RatingBar) itemView.findViewById(R.id.userRatingBar);
            BtnLike = (Button) itemView.findViewById(R.id.BtnLike);
            BtnBadReport = (Button) itemView.findViewById(R.id.BtnBadReport);
            LikeCounts = (TextView) itemView.findViewById(R.id.LikeCounts);
            userimg = (ImageView) itemView.findViewById(R.id.UserImg);


        }
    }


//        @OnClick(R.id.tv_student_group_item_delete)
//        public void delete(View view) {
//            final GlobalDialog delDialog = new GlobalDialog(context);
//            delDialog.setCanceledOnTouchOutside(true);
//            delDialog.getTitle().setText("提示");
//            delDialog.getContent().setText("确定删除吗?");
//            delDialog.setLeftBtnText("取消");
//            delDialog.setRightBtnText("确定");
//            delDialog.setLeftOnclick(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
//                    delDialog.dismiss();
//                }
//            });
//            delDialog.setRightOnclick(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "确定", Toast.LENGTH_SHORT).show();
//                    delDialog.dismiss();
//                }
//            });
//            delDialog.show();
//        }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_comment, parent, false);


        CourseCommentRecyclerViewAdapter.ViewHolder holder = new CourseCommentRecyclerViewAdapter.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        CourseCommentRecyclerViewAdapter.ViewHolder viewHolder = new ViewHolder(holder.itemView);

        viewHolder.userName.setText(mTags.get(pos).getUserName());
        viewHolder.commentDate.setText(DateFormat.getInstance().format(mTags.get(pos).getCommentDate()));

        //courseImage.setImageResource(course.getImageurl());
        viewHolder.commentContent.setText(mTags.get(pos).getContent());
        viewHolder.score.setRating((float) mTags.get(pos).getScore());


        //点赞按钮与举报按钮
        viewHolder.BtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = mTags.get(pos).getLikeCount();
                mTags.get(pos).setLikeCount(cur+1);
                viewHolder.LikeCounts.setText(String.valueOf(cur+1));
                Log.i("当前点赞数:",String.valueOf(mTags.get(pos).getLikeCount()));

            }
        });

        //举报提示框
        viewHolder.BtnBadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
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


        /*
        * holder.tvName.setText(mTags.get(position).getName());
        holder.tvTeacher.setText(mTags.get(position).getTeacher());
        holder.tvScore.setText(String.valueOf(mTags.get(position).getScore()));
        holder.tvContent.setText(mTags.get(position).getDescription());


        //holder.bindData(mTags.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    Log.e("click", "!");
                    itemClickListener.onItemClick(position);
                }
            }
        });
        * */

    }
    @Override
    public int getItemCount() {

//        return list.size();

        return mTags.size();
    }


}

