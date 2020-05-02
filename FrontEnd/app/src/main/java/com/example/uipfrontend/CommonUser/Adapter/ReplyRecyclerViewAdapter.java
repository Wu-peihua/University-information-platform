package com.example.uipfrontend.CommonUser.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.PostComment;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.lzy.widget.CircleImageView;
import com.parfoismeng.expandabletextviewlib.weiget.ExpandableTextView;
import com.sunbinqiang.iconcountview.IconCountView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
 * 这是评论详情下的回复列表的adapter
 */

public class ReplyRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int FAILURE = -1;
    private static final int SUCCESS = 1;
    
    private UserInfo user;

    private Context             context;
    private List<PostComment>   list;
    private OnReplyClickListener onReplyClickListener;
    private OnMoreClickListener onMoreClickListener;
    private OnLikeClickListener onLikeClickListener;

    public ReplyRecyclerViewAdapter(Context context, List<PostComment> list) {
        this.context = context;
        this.list = list;
        user = (UserInfo) context.getApplicationContext();
    }
    
    public void setList(List<PostComment> list) { this.list = list; }

    public void setReplyClickListener(OnReplyClickListener clickListener) {
        this.onReplyClickListener = clickListener;
    }
    public void setOnMoreClickListener(OnMoreClickListener clickListener) {
        this.onMoreClickListener = clickListener;
    }
    public void setOnLikeClickListener(OnLikeClickListener clickListener) {
        this.onLikeClickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView    portrait;          // 评论者头像
        TextView           tv_fromUserName;   // 评论者
        LinearLayout       ll_reply_to;       // 当是'评论给'时显示，否则隐藏
        TextView           tv_toUserName;     // 评论给
        TextView           tv_reference;      // 原内容
        ExpandableTextView tv_content;        // 评论内容
        TextView           tv_time;           // 评论时间
        IconCountView      praise;            // 点赞按钮
        LinearLayout       ll_click_write;    // 回复按钮
        ImageView          iv_report;         // 举报按钮

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            portrait = itemView.findViewById(R.id.imgv_cu_forum_comment_portrait);
            tv_fromUserName = itemView.findViewById(R.id.tv_cu_forum_comment_from);
            ll_reply_to = itemView.findViewById(R.id.ll_cu_forum_reply_to);
            tv_toUserName = itemView.findViewById(R.id.tv_cu_forum_comment_to);
            tv_reference = itemView.findViewById(R.id.tv_cu_forum_comment_reference);
            tv_content = itemView.findViewById(R.id.etv_cu_forum_comment_content);
            tv_time = itemView.findViewById(R.id.tv_cu_forum_comment_time);
            praise = itemView.findViewById(R.id.praise_view_cu_forum_comment_like);
            ll_click_write = itemView.findViewById(R.id.ll_cu_forum_comment_write);
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

        // 回复按钮监听
        viewHolder.ll_click_write.setOnClickListener(view -> onReplyClickListener.onClick(view, position));

        // String uri = "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg";
        String uri = list.get(position).getPortrait();
        Glide.with(context).load(Uri.parse(uri))
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.portrait);
        viewHolder.portrait.setBorderWidth(0);

        String isMe = user.getUserId().equals(list.get(position).getFromId()) ? "(我)" : "";
        viewHolder.tv_fromUserName.setText(list.get(position).getFromName() + isMe);

        String toName = list.get(position).getToName();
        if (toName != null) {
            viewHolder.ll_reply_to.setVisibility(View.VISIBLE);
            viewHolder.tv_toUserName.setText(toName);
            viewHolder.tv_reference.setText(list.get(position).getReference());
        } else {
            viewHolder.ll_reply_to.setVisibility(View.GONE);
        }

        viewHolder.tv_content.setContentText(list.get(position).getContent());
        viewHolder.tv_time.setText(list.get(position).getCreated());
        
        viewHolder.praise.setCount(list.get(position).getLikeNumber());
        // 如果是本人查看自己发的帖子，则需查找否点过赞
        
        // 点赞监听
        viewHolder.praise.setOnStateChangedListener(isSelected -> {
            if (isSelected) {
                list.get(position).setLikeNumber(list.get(position).getLikeNumber() + 1);
            } else {
                list.get(position).setLikeNumber(list.get(position).getLikeNumber() - 1);
            }
            notifyDataSetChanged();
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

    public interface OnReplyClickListener {
        void onClick(View view, int pos);
    }
    
    public interface OnMoreClickListener {
        void onClick(View view, int pos);
    }
    
    public interface OnLikeClickListener {
        void onClick(View view, int pos);
    }

}
