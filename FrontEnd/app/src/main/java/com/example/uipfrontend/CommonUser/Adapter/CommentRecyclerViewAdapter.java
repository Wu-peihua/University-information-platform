package com.example.uipfrontend.CommonUser.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.PostComment;
import com.example.uipfrontend.R;
import com.lzy.widget.CircleImageView;
import com.sunbinqiang.iconcountview.IconCountView;

import java.util.List;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<PostComment> list;
    private onItemClickListener itemClickListener;
    
    public CommentRecyclerViewAdapter(Context context, List<PostComment> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }
    
    private class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_item;            // 评论布局
        CircleImageView portrait;        // 评论者头像
        TextView tv_userName;            // 评论者
        TextView tv_content;             // 评论内容
        TextView tv_time;                // 评论时间
        IconCountView praise;   // 点赞按钮
        ImageView iv_report;             // 举报按钮

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            ll_item = itemView.findViewById(R.id.item_cu_forum_comment);
            portrait = itemView.findViewById(R.id.imgv_cu_forum_comment_portrait);
            tv_userName = itemView.findViewById(R.id.tv_cu_forum_comment_username);
            tv_content = itemView.findViewById(R.id.tv_cu_forum_comment_content);
            tv_time = itemView.findViewById(R.id.tv_cu_forum_comment_time);
            praise = itemView.findViewById(R.id.praise_view_cu_forum_comment_like);
            iv_report = itemView.findViewById(R.id.imgv_cu_forum_comment_more);
        }
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum_post_comment, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = new ViewHolder(holder.itemView);
        
        // 评论 点击监听
        viewHolder.ll_item.setOnClickListener(view -> itemClickListener.onClick(view, position));

        String uri = "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg";
//        String uri = list.get(position).getPortrait();
        Glide.with(context).load(Uri.parse(uri))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.portrait);
        viewHolder.portrait.setBorderWidth(0);
        
        viewHolder.tv_userName.setText(list.get(position).getFromName());
        viewHolder.tv_content.setText(list.get(position).getContent());
        viewHolder.tv_time.setText(list.get(position).getDate());
        viewHolder.praise.setCount(list.get(position).getLikeNum());
        
        viewHolder.iv_report.setOnClickListener(view -> {
            // 
            Toast.makeText(context, "弹出提示,询问是否举报", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    public interface onItemClickListener {
        void onClick(View view, int pos);
    }
    
}
