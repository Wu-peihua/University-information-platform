package com.example.uipfrontend.CommonUser.Adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

import java.util.List;

public class ResInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ResInfo> resInfoList;
    private Context context;

    private int beginPos;
    private String text;
    private ForegroundColorSpan span;

    public ResInfoAdapter(List<ResInfo> resInfoList, Context context) {
        super();
        this.resInfoList = resInfoList;
        this.context = context;
    }

    public void setText(String text, ForegroundColorSpan span) {
        this.text = text;
        this.span = span;
    }

    static class ResInfoViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_item;
        ImageView iv_portrait;
        TextView tv_username;
        TextView tv_title;
        TextView tv_description;
        TextView tv_link;
        TextView tv_time;

        public ResInfoViewHolder(View view) {
            super(view);
            ll_item = view.findViewById(R.id.ll_cu_resItem);
            iv_portrait = view.findViewById(R.id.iv_cu_resItem_portrait);
            tv_username = view.findViewById(R.id.tv_cu_resItem_username);
            tv_title = view.findViewById(R.id.tv_cu_resItem_title);
            tv_description = view.findViewById(R.id.tv_cu_resItem_description);
            tv_link = view.findViewById(R.id.tv_cu_resItem_link);
            tv_time = view.findViewById(R.id.tv_cu_resItem_time);
        }
    }

    static class ResEmptyViewHolder extends RecyclerView.ViewHolder {
        public ResEmptyViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener onItemClickListenerlistener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListenerlistener) {
        this.onItemClickListenerlistener = onItemClickListenerlistener;
    }

    public int getItemViewType(int position) {
        if (resInfoList == null)
            return -1;
        return resInfoList.size() == 0 ? -1 : super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == -1) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cu_res_empty, viewGroup, false);
            return new ResEmptyViewHolder(view);
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cu_resource, null);
        return new ResInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ResInfoViewHolder) {
            ResInfoViewHolder resInfoViewHolder = (ResInfoViewHolder) viewHolder;
            ResInfo resInfo = resInfoList.get(position);

            if (text == null) {
                resInfoViewHolder.tv_title.setText(resInfo.getTitle());
            } else {
                beginPos = resInfoList.get(position).getTitle().indexOf(text);
                if (beginPos != -1) {
                    SpannableStringBuilder builder = new SpannableStringBuilder(resInfoList.get(position).getTitle());
                    builder.setSpan(span, beginPos, beginPos + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    resInfoViewHolder.tv_title.setText(builder);
                }
            }

            Glide.with(context).load(resInfo.getPortraitUri())
                    .placeholder(R.drawable.portrait_default)
                    .error(R.drawable.portrait_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(resInfoViewHolder.iv_portrait);
            resInfoViewHolder.tv_username.setText(resInfo.getUsername());
            resInfoViewHolder.tv_description.setText(resInfo.getDescription());
            resInfoViewHolder.tv_link.setText(resInfo.getLink());
            resInfoViewHolder.tv_time.setText(resInfo.getTime());

            resInfoViewHolder.ll_item.setOnClickListener(view -> onItemClickListenerlistener.onClick(position));
        }
    }

    @Override
    public int getItemCount() {
        if (resInfoList == null)
            return 1;
        return resInfoList.size() == 0 ? 1 : resInfoList.size();
    }
}
