package com.example.uipfrontend.CommonUser.Adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uipfrontend.Entity.ForumPosts;
import com.example.uipfrontend.R;

import java.util.List;

public class ForumListRecyclerViewAdapter extends RecyclerView.Adapter  {

    private Context context;
    private List<ForumPosts> list;
    private onItemClickListener itemClickListener;

    private String keyWord;           // 搜索框关键字
    private ForegroundColorSpan span; // 关键字颜色

    public ForumListRecyclerViewAdapter(Context context, List<ForumPosts> list) {
        this.context = context;
        this.list = list;
    }

    public void setList(List<ForumPosts> list) { this.list = list; }
    
    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    public void setKeyWordColor(String keyWord, ForegroundColorSpan span) {
        this.keyWord = keyWord;
        this.span = span;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        
        LinearLayout ll_item;  // 对帖子设置监听
        TextView tv_title;     // 帖子标题
        TextView tv_poster;    // 发布者
        TextView tv_time;      // 发布时间
        TextView tv_like;      // 点赞数
        TextView tv_reply;     // 评论数

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_item = itemView.findViewById(R.id.item_cu_forum);
            tv_title = itemView.findViewById(R.id.tv_cu_forum_title);
            tv_poster = itemView.findViewById(R.id.tv_cu_forum_poster);
            tv_time = itemView.findViewById(R.id.tv_cu_forum_time);
            tv_like = itemView.findViewById(R.id.tv_cu_forum_like);
            tv_reply = itemView.findViewById(R.id.tv_cu_forum_reply);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum_post_brief, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = new ViewHolder(holder.itemView);
        if(keyWord == null){
            viewHolder.tv_title.setText(list.get(position).getTitle());
        } else {
            // 关键字开始位置
            int beginPos = list.get(position).getTitle().indexOf(keyWord);
            if(beginPos != -1){
                SpannableStringBuilder builder = new SpannableStringBuilder(list.get(position).getTitle());
                builder.setSpan(span, beginPos, beginPos + keyWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.tv_title.setText(builder);
            }
        }
        viewHolder.tv_poster.setText(list.get(position).getUserName());
        viewHolder.tv_time.setText(list.get(position).getCreated());
        viewHolder.tv_like.setText(String.valueOf(list.get(position).getLikeNumber()));
        viewHolder.tv_reply.setText(String.valueOf(list.get(position).getReplyNumber()));
        viewHolder.ll_item.setOnClickListener(view -> itemClickListener.onClick(view, position));
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
