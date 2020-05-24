package com.example.uipfrontend.Admin.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.os.Handler;
import android.os.Message;
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

public class AdminCourseCommentRecyclerViewAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<CourseComment> courseComments = new ArrayList<>();

    private UserInfo user;//全局用户id

//    //用户数据测试
//    private Map<Long, UserInfo> userInfos = new HashMap<>() ;//<userid,Userinfo>


    //分页请求课程评论数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    //评论用户

    private onItemClickListener onClickListener;


    public void setList(List<CourseComment> list) {
        this.courseComments= list;

    }

    public void setonItemClickListener(onItemClickListener itemClickListener) {
        this.onClickListener = itemClickListener;
    }

    //    public void getAllCommentUser(){
//
//        for(CourseComment c:courseComments){
//            requestUserInfo(c.getCommentatorId());//请求评论列表下所有的用户
//        }
//    }


    public AdminCourseCommentRecyclerViewAdapter(Context context, List<CourseComment> list) {
        this.context = context;
        this.courseComments = list;
        user = (UserInfo) context.getApplicationContext();
//        getAllCommentUser();//根据评论列表获取
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView commentDate;
        TextView commentContent;
        RatingBar score;
        //Button BtnLike;
        Button BtnBadReport;
        //TextView LikeCounts;
        ImageView userimg;
//        private IconCountView BtnLike;//重写点赞按钮



        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.adminUserName);
            commentDate = (TextView) itemView.findViewById(R.id.adminCommentDate);
            commentContent = (TextView) itemView.findViewById(R.id.adminCommentContent);
            score = (RatingBar) itemView.findViewById(R.id.adminuserRatingBar);
            //BtnLike = (Button) itemView.findViewById(R.id.adminBtnLike);
            BtnBadReport = (Button) itemView.findViewById(R.id.adminBtnBadReport);
            //LikeCounts = (TextView) itemView.findViewById(R.id.adminLikeCounts);
            userimg = (ImageView) itemView.findViewById(R.id.adminUserImg);

//            BtnLike = itemView.findViewById(R.id.adminbtnlike_student_comment);

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_course_comment, parent, false);


        AdminCourseCommentRecyclerViewAdapter.ViewHolder holder = new AdminCourseCommentRecyclerViewAdapter.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        AdminCourseCommentRecyclerViewAdapter.ViewHolder viewHolder = new ViewHolder(holder.itemView);

        //CourseComment comment = courseComments.get(pos);

        //设置 commentator id
        //设置评论用户名字与头像
        //System.out.println("user id:"+user.getUserId());
        //System.out.println("commentator id:"+comment.getCommentatorId());
//        String isMe = user.getUserId().equals(comment.getCommentatorId()) ? "(我)" : "";
        String isMe = user.getUserId().equals(courseComments.get(pos).getCommentatorId()) ? "(我)" : "";


        viewHolder.userName.setText(courseComments.get(pos).getFromName()+isMe);
        setImage(context,viewHolder.userimg,courseComments.get(pos).getPortrait());;

        DateFormat datefomat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String commentDate = datefomat.format(courseComments.get(pos).getInfoDate());
        /// System.out.println("日期格式："+commentDate);
        viewHolder.commentDate.setText(commentDate);

        //courseImage.setImageResource(course.getImageurl());
        viewHolder.commentContent.setText(courseComments.get(pos).getContent());
        viewHolder.score.setStepSize((float) 0.5);
        viewHolder.score.setRating(courseComments.get(pos).getScore());

        viewHolder.BtnBadReport.setOnClickListener(view -> onClickListener.onClick(view,pos));


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
        void onClick(View view , int pos);
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

