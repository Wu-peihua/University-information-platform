package com.example.uipfrontend.CommonUser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.CommonUser.Activity.CommentDetailActivity;
import com.example.uipfrontend.Entity.PostComment;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Utils.DateUtil;
import com.lzy.widget.CircleImageView;
import com.parfoismeng.expandabletextviewlib.weiget.ExpandableTextView;
import com.sunbinqiang.iconcountview.IconCountView;

import java.util.List;

/**
 * 这是帖子详情页的评论列表的adapter
 */

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter {
    
    private UserInfo user;

    private Context              context;
    private List<PostComment>    list;
    private OnMoreClickListener  onMoreClickListener;
    private OnLikeSelectListener onLikeSelectListener;

    public CommentRecyclerViewAdapter(Context context, List<PostComment> list) {
        this.context = context;
        this.list = list;
        user = (UserInfo) context.getApplicationContext();
    }

    public void setList(List<PostComment> list) { this.list = list; }
    
    public void setOnMoreClickListener(OnMoreClickListener clickListener) {
        this.onMoreClickListener = clickListener;
    }
    
    public void setOnLikeSelectListener(OnLikeSelectListener selectListener) { 
        this.onLikeSelectListener = selectListener; 
    }
    
    private static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView    portrait;        // 评论者头像
        TextView           tv_userName;     // 评论者
        ExpandableTextView tv_content;      // 评论内容
        TextView           tv_time;         // 评论时间
        IconCountView      praise;          // 点赞按钮
        LinearLayout       ll_click_write;  // 评论详情按钮
        TextView           tv_replySum;     // 回复数
        ImageView          iv_report;       // 举报按钮

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            portrait = itemView.findViewById(R.id.imgv_cu_forum_comment_portrait);
            tv_userName = itemView.findViewById(R.id.tv_cu_forum_comment_from);
            tv_content = itemView.findViewById(R.id.etv_cu_forum_comment_content);
            tv_time = itemView.findViewById(R.id.tv_cu_forum_comment_time);
            praise = itemView.findViewById(R.id.praise_view_cu_forum_comment_like);
            ll_click_write = itemView.findViewById(R.id.ll_cu_forum_comment_write);
            tv_replySum = itemView.findViewById(R.id.tv_cu_forum_comment_sum);
            iv_report = itemView.findViewById(R.id.imgv_cu_forum_comment_more);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum_post_comment, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = new ViewHolder(holder.itemView);
        
        String uri = list.get(position).getPortrait();
        Glide.with(context).load(Uri.parse(uri))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.portrait);
        viewHolder.portrait.setBorderWidth(0);
        
        String isMe = user.getUserId().equals(list.get(position).getFromId()) ? "(我)" : "";
        viewHolder.tv_userName.setText(String.format("%s%s", list.get(position).getFromName(), isMe));
        viewHolder.tv_content.setContentText(list.get(position).getContent());
        viewHolder.tv_time.setText(DateUtil.fromToday(list.get(position).getCreated()));
        
        viewHolder.praise.setCount(list.get(position).getLikeNumber());
        if (user.getLikeRecord().containsKey("comment" + list.get(position).getInfoId())) {
            viewHolder.praise.setState(true);
        } else {
            viewHolder.praise.setState(false);
        }

        viewHolder.tv_replySum.setText(String.valueOf(list.get(position).getReplyNumber()));
        viewHolder.tv_replySum.setVisibility(View.VISIBLE);
        
        // 点赞监听
        viewHolder.praise.setOnStateChangedListener(isSelected -> onLikeSelectListener.select(isSelected, position));

        // 跳转到评论详情
        viewHolder.ll_click_write.setOnClickListener(view -> {
            Intent intent = new Intent(context, CommentDetailActivity.class);
            intent.putExtra("pos", position);
            intent.putExtra("comment", list.get(position));
            context.startActivity(intent);
        });
        
        // 举报or删除监听
        viewHolder.iv_report.setOnClickListener(view -> onMoreClickListener.onClick(view, position));

    }

    @Override
    public int getItemCount() {
        if (list != null) return list.size();
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
    
    public interface OnMoreClickListener {
        void onClick(View view, int pos);
    }
    
    public interface OnLikeSelectListener {
        void select(boolean isSelected, int pos);
    }
}
