package com.example.uipfrontend.CommonUser.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uipfrontend.CommonUser.Activity.PostDetailActivity;
import com.example.uipfrontend.CommonUser.Activity.WritePostActivity;
import com.example.uipfrontend.Entity.ForumPosts;
import com.example.uipfrontend.R;

import java.util.List;

public class MyReleasePostAdapter extends RecyclerView.Adapter   {

    private Context context;
    private List<ForumPosts> list;
    
    public MyReleasePostAdapter(Context context, List<ForumPosts> list) {
        this.context = context;
        this.list = list;
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View view) {
            super(view);
        }
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        
        LinearLayout ll_operation;   // 三个功能按钮的可见性
        LinearLayout ll_like;        // 点赞数：设置为不可见
        TextView tv_title;     // 帖子标题
        TextView tv_poster;    // 发布者
        TextView tv_time;      // 发布时间
        TextView tv_detail;    // 详情按钮
        TextView tv_modify;    // 修改按钮
        TextView tv_delete;    // 删除按钮

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_operation = itemView.findViewById(R.id.ll_cu_forum_record_operation);
            ll_like = itemView.findViewById(R.id.ll_cu_forum_like);
            tv_title = itemView.findViewById(R.id.tv_cu_forum_title);
            tv_poster = itemView.findViewById(R.id.tv_cu_forum_poster);
            tv_time = itemView.findViewById(R.id.tv_cu_forum_time);
            tv_detail = itemView.findViewById(R.id.tv_cu_forum_record_detail);
            tv_modify = itemView.findViewById(R.id.tv_cu_forum_record_modify);
            tv_delete = itemView.findViewById(R.id.tv_cu_forum_record_delete);
        }
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == -1) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_cu_res_empty, parent, false);
            return new EmptyViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum_post_brief, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {

            ViewHolder viewHolder = new ViewHolder(holder.itemView);

            viewHolder.ll_like.setVisibility(View.GONE);
            viewHolder.ll_operation.setVisibility(View.VISIBLE);

            viewHolder.tv_title.setText(list.get(position).getTitle());
            viewHolder.tv_poster.setText(list.get(position).getPoster());
            viewHolder.tv_time.setText(list.get(position).getPostTime());

            // 跳转到帖子详情
            viewHolder.tv_detail.setOnClickListener(view -> {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("detail", list.get(position));
                context.startActivity(intent);
            });

            // 删除
            viewHolder.tv_delete.setOnClickListener(view -> {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("是否确定删除该条记录？")
                        .setPositiveButton("确定", (dialog1, which) -> {
                            list.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.blue));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.blue));
            });

            // 跳转到帖子编辑
            viewHolder.tv_modify.setOnClickListener(view -> {
                Intent intent = new Intent(context, WritePostActivity.class);
                intent.putExtra("post", list.get(position));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if(list != null) return list.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if(list == null) return -1;
        return list.size() == 0 ? -1 : super.getItemViewType(position);
    }
    
}
