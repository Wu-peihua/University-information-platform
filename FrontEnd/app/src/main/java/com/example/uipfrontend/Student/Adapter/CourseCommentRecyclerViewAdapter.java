package com.example.uipfrontend.Student.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
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


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.Entity.ResponseUserInfo;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.google.gson.Gson;
import com.sunbinqiang.iconcountview.IconCountView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CourseCommentRecyclerViewAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<CourseComment> courseComments = new ArrayList<>();

    private UserInfo user;//全局用户id


    private onItemClickListener itemClickListener;//设置点击监听器
    private OnMoreClickListener onMoreClickListener;//设置list删除监听器


    //分页请求课程评论数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    //评论用户

    public void setOnMoreClickListener(OnMoreClickListener clickListener) {
        this.onMoreClickListener = clickListener;
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    public interface OnMoreClickListener {
        void onClick(View view, int pos);
    }
    public void setList(List<CourseComment> list) {
        this.courseComments= list;

    }



    public CourseCommentRecyclerViewAdapter(Context context, List<CourseComment> list) {
        this.context = context;
        this.courseComments = list;
        user = (UserInfo) context.getApplicationContext();
        //getAllCommentUser();//根据评论列表获取
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        //LinearLayout card_item;  // 对cardview设置监听
        TextView userName;
        TextView commentDate;
        TextView commentContent;
        RatingBar score;
        //Button BtnLike;
        Button BtnBadReport;
        //TextView LikeCounts;
        ImageView userimg;
        private IconCountView BtnLike;//重写点赞按钮

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //card_item = itemView.findViewById(R.id.item_course);
            userName = (TextView) itemView.findViewById(R.id.UserName);
            commentDate = (TextView) itemView.findViewById(R.id.CommentDate);
            commentContent = (TextView) itemView.findViewById(R.id.CommentContent);
            score = (RatingBar) itemView.findViewById(R.id.userRatingBar);
            //BtnLike = (Button) itemView.findViewById(R.id.BtnLike);
            BtnBadReport = (Button) itemView.findViewById(R.id.BtnBadReport);
            //LikeCounts = (TextView) itemView.findViewById(R.id.LikeCounts);
            userimg = (ImageView) itemView.findViewById(R.id.UserImg);

            BtnLike = itemView.findViewById(R.id.btnlike_student_comment);

        }
    }

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

        CourseComment comment = courseComments.get(pos);

        //设置 commentator id
        //设置评论用户名字与头像
        //System.out.println("user id:"+user.getUserId());
        //System.out.println("commentator id:"+comment.getCommentatorId());
        String isMe = user.getUserId().equals(comment.getCommentatorId()) ? "(我)" : "";

        viewHolder.userName.setText(comment.getFromName()+isMe);
        setImage(context,viewHolder.userimg,comment.getPortrait());;

        DateFormat datefomat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String commentDate = datefomat.format(comment.getInfoDate());
       /// System.out.println("日期格式："+commentDate);
        viewHolder.commentDate.setText(commentDate);

        //courseImage.setImageResource(course.getImageurl());
        viewHolder.commentContent.setText(comment.getContent());
        viewHolder.score.setStepSize((float) 0.5);
        viewHolder.score.setRating(comment.getScore());

        //System.out.println("adapter 评分为："+comment.getScore());

        //viewHolder.LikeCounts.setText(String.valueOf(mTags.get(pos).getLikeCount()));

        viewHolder.BtnLike.setCount(comment.getLikeCount());
        // 点赞监听
        viewHolder.BtnLike.setOnStateChangedListener(isSelected -> {
            if (isSelected) {
                comment.setLikeCount(comment.getLikeCount() + 1);
            }
            else {
                comment.setLikeCount(comment.getLikeCount() - 1);
            }
            notifyDataSetChanged();
        });
        //Log.i("当前点赞pos",String.valueOf(courseComments.get(pos)));


        // 举报or删除监听
        viewHolder.BtnBadReport.setOnClickListener(view -> onMoreClickListener.onClick(view, pos));

        /*viewHolder.BtnBadReport.setOnClickListener(new View.OnClickListener() {
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

         */


    }

    //设置用户头像
    private void setImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }
    //点击
    public interface onItemClickListener {
        void onClick(int pos);
    }

    @Override
    public int getItemCount() {

//        return list.size();
        if (courseComments != null) return courseComments.size();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
