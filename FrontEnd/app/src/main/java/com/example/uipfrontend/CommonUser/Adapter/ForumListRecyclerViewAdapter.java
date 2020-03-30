package com.example.uipfrontend.CommonUser.Adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    
    private int beginPos;
    private String text;
    private ForegroundColorSpan span;

    public ForumListRecyclerViewAdapter(Context context, List<ForumPosts> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(onItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }

    public void setText(String text, ForegroundColorSpan span) {
        this.text = text;
        this.span = span;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout ll_item;
        TextView tv_title;
        TextView tv_poster;
        TextView tv_time;
        TextView tv_like;
        TextView tv_report;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_item = itemView.findViewById(R.id.item_cu_forum);
            tv_title = itemView.findViewById(R.id.tv_cu_forum_title);
            tv_poster = itemView.findViewById(R.id.tv_cu_forum_poster);
            tv_time = itemView.findViewById(R.id.tv_cu_forum_time);
            tv_like = itemView.findViewById(R.id.tv_cu_forum_like);
            tv_report = itemView.findViewById(R.id.tv_cu_forum_report);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cu_forum, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = new ViewHolder(holder.itemView);
        if(text == null){
            viewHolder.tv_title.setText(list.get(position).getTitle());
        } else {
            beginPos = list.get(position).getTitle().indexOf(text);
            if(beginPos != -1){
                SpannableStringBuilder builder = new SpannableStringBuilder(list.get(position).getTitle());
                builder.setSpan(span, beginPos, beginPos + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.tv_title.setText(builder);
            }
        }
        viewHolder.tv_poster.setText(list.get(position).getPoster());
        viewHolder.tv_time.setText(list.get(position).getPostTime());
        viewHolder.tv_like.setText(String.valueOf(list.get(position).getLikeNum()));
        viewHolder.tv_report.setText(String.valueOf(list.get(position).getReportNum()));
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