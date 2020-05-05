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
import com.example.uipfrontend.R;
import com.sunbinqiang.iconcountview.IconCountView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;

public class AdminReportCourseCommentAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<CourseComment> mTags = new ArrayList<>();
    ;

    public void setList(List<CourseComment> list) {
        this.mTags = list;
    }

    public AdminReportCourseCommentAdapter(Context context, List<CourseComment> list) {
        this.context = context;
        this.mTags = list;
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
                .inflate(R.layout.item_admin_report_comment, parent, false);


        AdminReportCourseCommentAdapter.ViewHolder holder = new AdminReportCourseCommentAdapter.ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        AdminReportCourseCommentAdapter.ViewHolder viewHolder = new ViewHolder(holder.itemView);

        CourseComment comment = mTags.get(pos);

        //预设用户头像
        Glide.with(context).load("")
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.userimg);


        //viewHolder.userName.setText(comment.getUserName());
        //viewHolder.commentDate.setText(DateFormat.getInstance().format(comment.getCommentDate()));
        viewHolder.commentDate.setText(comment.getInfoDate().toString());
        //courseImage.setImageResource(course.getImageurl());
        viewHolder.commentContent.setText(comment.getContent());
        viewHolder.score.setRating((float)comment.getScore());

        //viewHolder.LikeCounts.setText(String.valueOf(mTags.get(pos).getLikeCount()));


        //点赞按钮与举报按钮
        /*
        viewHolder.BtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cur = mTags.get(pos).getLikeCount();
                mTags.get(pos).setLikeCount(cur+1);
                viewHolder.LikeCounts.setText(String.valueOf(cur+1));
                Log.i("当前点赞数:",String.valueOf(mTags.get(pos).getLikeCount()));

            }
        });

         */


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
        if (mTags != null) return mTags.size();
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

