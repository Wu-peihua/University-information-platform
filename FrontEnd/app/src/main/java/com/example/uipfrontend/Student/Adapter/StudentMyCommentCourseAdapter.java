package com.example.uipfrontend.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.CourseComment;
import com.example.uipfrontend.R;
import com.sunbinqiang.iconcountview.IconCountView;

import java.text.DateFormat;
import java.util.List;

public class StudentMyCommentCourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<CourseComment> commentList;
    private Context context;

    public StudentMyCommentCourseAdapter(List<CourseComment> commentList, Context context) {
        super();
        this.commentList = commentList;
        this.context = context;
    }

    static class MyCommentCourseViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_item;

        ImageView iv_portrait;//加载用户头像
        TextView tv_coursename;
        TextView tv_username;
        TextView  tv_comment_date;
        TextView tv_content;
        RatingBar rb_score;
        IconCountView icv_like;
        TextView  tv_delete;
        TextView  tv_modify;

        public MyCommentCourseViewHolder(View view) {
            super(view);
            ll_item = view.findViewById(R.id.ll_item_my_comment);
            iv_portrait = view.findViewById(R.id.my_user_img);
            tv_username = view.findViewById(R.id.my_user_name);
            tv_coursename = view.findViewById(R.id.my_comment_course_name);
            tv_comment_date = view.findViewById(R.id.my_comment_date);
            tv_content  = view.findViewById(R.id.my_content);
            rb_score = view.findViewById(R.id.my_user_rating_bar);
            icv_like = view.findViewById(R.id.my_comment_btn_like);
            tv_delete = view.findViewById(R.id.my_comment_delete);
            tv_modify = view.findViewById(R.id.my_comment_modify);
        }
    }

    static class MyCourseCommentEmptyViewHolder extends RecyclerView.ViewHolder {
        public MyCourseCommentEmptyViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemDeleteClickListener {
        void onDeleteClick(int position);
    }

    private StudentMyCommentCourseAdapter.OnItemDeleteClickListener onItemDeleteClickListener;

    public void setOnItemDeleteClickListener(StudentMyCommentCourseAdapter.OnItemDeleteClickListener onItemDeleteClickListener) {
        this.onItemDeleteClickListener = onItemDeleteClickListener;
    }

    public interface OnItemModifyClickListener {
        void onModifyClick(int position);
    }

    private StudentMyCommentCourseAdapter.OnItemModifyClickListener onItemModifyClickListener;

    public void setOnItemModifyClickListener(StudentMyCommentCourseAdapter.OnItemModifyClickListener onItemModifyClickListener) {
        this.onItemModifyClickListener = onItemModifyClickListener;
    }

    public int getItemViewType(int position) {
        if (commentList == null)
            return -1;
        return commentList.size() == 0 ? -1 : super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == -1) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_comment_empty, viewGroup, false);
            return new StudentMyCommentCourseAdapter.MyCourseCommentEmptyViewHolder(view);
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_comment, null);
        return new StudentMyCommentCourseAdapter.MyCommentCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof StudentMyCommentCourseAdapter.MyCommentCourseViewHolder) {
            StudentMyCommentCourseAdapter.MyCommentCourseViewHolder commentViewHolder = (StudentMyCommentCourseAdapter.MyCommentCourseViewHolder) viewHolder;
            CourseComment courseComment = commentList.get(position);

            //预设用户头像
            Glide.with(context).load("")
                    .placeholder(R.drawable.portrait_default)
                    .error(R.drawable.portrait_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(commentViewHolder.iv_portrait);

            commentViewHolder.tv_coursename.setText(courseComment.getCourseName());
            commentViewHolder.tv_username.setText(courseComment.getUserName());
            commentViewHolder.tv_content.setText(courseComment.getContent());
            commentViewHolder.rb_score.setRating((float)courseComment.getScore());
            //格式化
            commentViewHolder.tv_comment_date.setText(DateFormat.getInstance().format(courseComment.getCommentDate()));
            commentViewHolder.icv_like.setCount(courseComment.getLikeCount());
            commentViewHolder.icv_like.setOnStateChangedListener(new IconCountView.OnSelectedStateChangedListener() {
                @Override
                public void select(boolean isSelected) {
                    if (isSelected)
                        courseComment.setLikeCount(courseComment.getLikeCount() + 1);
                    else
                        courseComment.setLikeCount(courseComment.getLikeCount() - 1);
                }
            });

            /*
            if (resInfo.isAnonymous()) {
                Glide.with(context).load("")
                        .placeholder(R.drawable.portrait_default)
                        .error(R.drawable.portrait_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(infoViewHolder.iv_portrait);
                infoViewHolder.tv_username.setText("匿名者");
            } else {
                Glide.with(context).load(resInfo.getPortraitUri())
                        .placeholder(R.drawable.portrait_default)
                        .error(R.drawable.portrait_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(infoViewHolder.iv_portrait);
                infoViewHolder.tv_username.setText(resInfo.getUsername());
            }
            infoViewHolder.tv_title.setText(resInfo.getTitle());
            if (resInfo.getDescription() == null || resInfo.getDescription().trim().length() == 0)
                infoViewHolder.etv_description.setContentText("(暂无相关描述信息)");
            else
                infoViewHolder.etv_description.setContentText(resInfo.getDescription());
            infoViewHolder.tv_link.setText(resInfo.getLink());
            infoViewHolder.tv_time.setText(resInfo.getTime());
            infoViewHolder.icv_like.setCount(resInfo.getLikeNum());
            infoViewHolder.icv_like.setOnStateChangedListener(new IconCountView.OnSelectedStateChangedListener() {
                @Override
                public void select(boolean isSelected) {
                    if (isSelected)
                        resInfo.setLikeNum(resInfo.getLikeNum() + 1);
                    else
                        resInfo.setLikeNum(resInfo.getLikeNum() - 1);
                }
            });

             */
            commentViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemDeleteClickListener != null)
                        onItemDeleteClickListener.onDeleteClick(position);
                }
            });
            commentViewHolder.tv_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemModifyClickListener != null)
                        onItemModifyClickListener.onModifyClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (commentList == null)
            return 1;
        return commentList.size() == 0 ? 1 : commentList.size();
    }

}