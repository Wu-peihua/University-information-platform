package com.example.uipfrontend.CommonUser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.R;
import com.parfoismeng.expandabletextviewlib.weiget.ExpandableTextView;
import com.sunbinqiang.iconcountview.IconCountView;

import java.util.List;

public class MyReleaseResInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ResInfo> resInfoList;
    private Context context;

    public MyReleaseResInfoAdapter(List<ResInfo> resInfoList, Context context) {
        super();
        this.resInfoList = resInfoList;
        this.context = context;
    }

    static class MyReleaseResInfoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_item;
        ImageView iv_portrait;
        TextView tv_username;
        TextView tv_title;
        ExpandableTextView etv_description;
        TextView tv_link;
        TextView tv_time;
        IconCountView icv_like;
        TextView tv_delete;
        TextView tv_modify;

        public MyReleaseResInfoViewHolder(View view) {
            super(view);
            ll_item = view.findViewById(R.id.ll_mr_resItem);
            iv_portrait = view.findViewById(R.id.iv_mr_resItem_portrait);
            tv_username = view.findViewById(R.id.tv_mr_resItem_username);
            tv_title = view.findViewById(R.id.tv_mr_resItem_title);
            etv_description = view.findViewById(R.id.etv_mr_resItem_desc);
            tv_link = view.findViewById(R.id.tv_mr_resItem_link);
            tv_time = view.findViewById(R.id.tv_mr_resItem_time);
            icv_like = view.findViewById(R.id.icv_mr_resItem_like);
            tv_delete = view.findViewById(R.id.tv_mr_resItem_delete);
            tv_modify = view.findViewById(R.id.tv_mr_resItem_modify);
        }
    }

    static class MyReleaseResEmptyViewHolder extends RecyclerView.ViewHolder {
        public MyReleaseResEmptyViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemDeleteClickListener {
        void onDeleteClick(int position);
    }

    private OnItemDeleteClickListener onItemDeleteClickListener;

    public void setOnItemDeleteClickListener(OnItemDeleteClickListener onItemDeleteClickListener) {
        this.onItemDeleteClickListener = onItemDeleteClickListener;
    }

    public interface OnItemModifyClickListener {
        void onModifyClick(int position);
    }

    private OnItemModifyClickListener onItemModifyClickListener;

    public void setOnItemModifyClickListener(OnItemModifyClickListener onItemModifyClickListener) {
        this.onItemModifyClickListener = onItemModifyClickListener;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (resInfoList == null)
            return -1;
        return resInfoList.size() == 0 ? -1 : position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == -1) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mr_res_empty, viewGroup, false);
            return new MyReleaseResEmptyViewHolder(view);
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mr_res, null);
        return new MyReleaseResInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof MyReleaseResInfoViewHolder) {
            MyReleaseResInfoViewHolder infoViewHolder = (MyReleaseResInfoViewHolder) viewHolder;
            ResInfo resInfo = resInfoList.get(position);

            if (resInfo.isAnonymous()) {
                Glide.with(context).load("")
                        .placeholder(R.drawable.portrait_default)
                        .error(R.drawable.portrait_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(infoViewHolder.iv_portrait);
                infoViewHolder.tv_username.setText("匿名者");
            } else {
                Glide.with(context).load(resInfo.getPortrait())
                        .placeholder(R.drawable.portrait_default)
                        .error(R.drawable.portrait_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(infoViewHolder.iv_portrait);
                infoViewHolder.tv_username.setText(resInfo.getUserName());
            }
            infoViewHolder.tv_title.setText(resInfo.getTitle());
            if (resInfo.getDescription() == null || resInfo.getDescription().trim().length() == 0)
                infoViewHolder.etv_description.setContentText("描述：(暂无相关描述信息)");
            else
                infoViewHolder.etv_description.setContentText("描述：" + resInfo.getDescription());
            infoViewHolder.tv_link.setText(resInfo.getAddress());
            infoViewHolder.tv_time.setText(resInfo.getCreated());
            infoViewHolder.icv_like.setCount(resInfo.getLikeNumber());
            infoViewHolder.icv_like.setOnStateChangedListener(new IconCountView.OnSelectedStateChangedListener() {
                @Override
                public void select(boolean isSelected) {
                    if (isSelected)
                        resInfo.setLikeNumber(resInfo.getLikeNumber() + 1);
                    else
                        resInfo.setLikeNumber(resInfo.getLikeNumber() - 1);
                    notifyDataSetChanged();
                }
            });
            infoViewHolder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemDeleteClickListener != null)
                        onItemDeleteClickListener.onDeleteClick(position);
                }
            });
            infoViewHolder.tv_modify.setOnClickListener(new View.OnClickListener() {
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
        if (resInfoList == null)
            return 1;
        return resInfoList.size() == 0 ? 1 : resInfoList.size();
    }
}