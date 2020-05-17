package com.example.uipfrontend.Admin.Adapter;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.sunbinqiang.iconcountview.IconCountView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;

public class AdminReportCourseCommentAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<CourseComment> courseComments = new ArrayList<>();

    private UserInfo user;//全局用户id

    //分页请求课程评论数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    //评论用户

    public void setList(List<CourseComment> list) {
        this.courseComments= list;

    }

    public AdminReportCourseCommentAdapter(Context context, List<CourseComment> list) {
        this.context = context;
        this.courseComments=list;
        user = (UserInfo) context.getApplicationContext();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView commentDate;
        TextView commentContent;
        RatingBar score;
        FButton Pass;
        FButton UnPass;
        ImageView userimg;


        ViewHolder(@NonNull View itemView) {
            super(itemView);


            userName = (TextView) itemView.findViewById(R.id.adminUserName);
            commentDate = (TextView) itemView.findViewById(R.id.adminCommentDate);
            commentContent = (TextView) itemView.findViewById(R.id.adminCommentContent);
            score = (RatingBar) itemView.findViewById(R.id.adminuserRatingBar);
            //BtnLike = (Button) itemView.findViewById(R.id.adminBtnLike);
            Pass = (FButton) itemView.findViewById(R.id.admin_btn_pass);
            UnPass = (FButton) itemView.findViewById(R.id.admin_btn_unpass);
            //LikeCounts = (TextView) itemView.findViewById(R.id.adminLikeCounts);
            userimg = (ImageView) itemView.findViewById(R.id.adminUserImg);


        }
    }
    public interface commentPassClickListener {
        void onPassClick(int position);
    }

    private commentPassClickListener commentPassClickListener;

    public void setCommentPassClickListener(commentPassClickListener commentPassClickListener) {
        this.commentPassClickListener = commentPassClickListener;
    }
    public interface commentUnPassClickListener {
        void onUnPassClick(int position);
    }

    private commentUnPassClickListener commentUnPassClickListener;

    public void setCommentUnPassClickListener(commentUnPassClickListener commentUnPassClickListener) {
        this.commentUnPassClickListener = commentUnPassClickListener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_report_comment, parent, false);


        AdminReportCourseCommentAdapter.ViewHolder holder = new AdminReportCourseCommentAdapter.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        AdminReportCourseCommentAdapter.ViewHolder viewHolder = new ViewHolder(holder.itemView);

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

        Long curId = courseComments.get(pos).getInfoId();

        viewHolder.Pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentPassClickListener != null)
                    commentPassClickListener.onPassClick(pos);
            }
        });
        viewHolder.UnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentUnPassClickListener != null)
                    commentUnPassClickListener.onUnPassClick(pos);
            }
        });

    }
    //设置用户头像
    private void setImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
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

