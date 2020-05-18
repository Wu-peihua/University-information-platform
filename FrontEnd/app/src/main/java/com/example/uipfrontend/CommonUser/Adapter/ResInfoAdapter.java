package com.example.uipfrontend.CommonUser.Adapter;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.alertview.AlertView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView;
import com.parfoismeng.expandabletextviewlib.weiget.ExpandableTextView;
import com.sunbinqiang.iconcountview.IconCountView;

import java.util.List;

public class ResInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ResInfo> resInfoList;
    private Context context;
    private UserInfo user;

    private int beginPos;
    private String text;
    private ForegroundColorSpan span;

    public ResInfoAdapter(List<ResInfo> resInfoList, Context context) {
        super();
        this.resInfoList = resInfoList;
        this.context = context;
        user = (UserInfo) context.getApplicationContext();
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
        ExpandableTextView etv_description;
        TextView tv_link;
        TextView tv_time;
        IconCountView icv_like;
        ImageView iv_report;

        public ResInfoViewHolder(View view) {
            super(view);
            ll_item = view.findViewById(R.id.ll_cu_resItem);
            iv_portrait = view.findViewById(R.id.iv_cu_resItem_portrait);
            tv_username = view.findViewById(R.id.tv_cu_resItem_username);
            tv_title = view.findViewById(R.id.tv_cu_resItem_title);
            etv_description = view.findViewById(R.id.etv_cu_resItem_description);
            tv_link = view.findViewById(R.id.tv_cu_resItem_link);
            tv_time = view.findViewById(R.id.tv_cu_resItem_time);
            icv_like = view.findViewById(R.id.icv_cu_resItem_like);
            iv_report = view.findViewById(R.id.iv_cu_resItem_report);
        }
    }

    static class ResEmptyViewHolder extends RecyclerView.ViewHolder {
        public ResEmptyViewHolder(View view) {
            super(view);
        }
    }

    public interface OnItemLikeClickListener {
        void onClick(boolean isSelected, int position);
    }

    private OnItemLikeClickListener onItemLikeClickListener;

    public void setOnItemLikeClickListener(OnItemLikeClickListener onItemLikeClickListener) {
        this.onItemLikeClickListener = onItemLikeClickListener;
    }

    public interface OnItemReportClickListener {
        void onClick(View view, int position);
    }

    private OnItemReportClickListener onItemReportClickListener;

    public void setOnItemReportClickListener(OnItemReportClickListener onItemReportClickListener) {
        this.onItemReportClickListener = onItemReportClickListener;
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
                beginPos = resInfo.getTitle().indexOf(text);
                if (beginPos != -1) {
                    SpannableStringBuilder builder = new SpannableStringBuilder(resInfo.getTitle());
                    builder.setSpan(span, beginPos, beginPos + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    resInfoViewHolder.tv_title.setText(builder);
                }
            }

            if (resInfo.isAnonymous()) {
                Glide.with(context).load("")
                        .placeholder(R.drawable.portrait_default)
                        .error(R.drawable.portrait_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(resInfoViewHolder.iv_portrait);
                resInfoViewHolder.tv_username.setText("匿名者");
            } else {
                Glide.with(context).load(resInfo.getPortrait())
                        .placeholder(R.drawable.portrait_default)
                        .error(R.drawable.portrait_default)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(resInfoViewHolder.iv_portrait);
                resInfoViewHolder.tv_username.setText(resInfo.getUserName());
            }
            if (resInfo.getDescription() == null || resInfo.getDescription().trim().length() == 0)
                resInfoViewHolder.etv_description.setContentText("描述：(暂无相关描述信息)");
            else
                resInfoViewHolder.etv_description.setContentText("描述：" + resInfo.getDescription());
            resInfoViewHolder.tv_link.setText(resInfo.getAddress());
            resInfoViewHolder.tv_time.setText(resInfo.getCreated());
            resInfoViewHolder.icv_like.setCount(resInfo.getLikeNumber());
            if (user.getLikeRecord().containsKey("resource" + resInfo.getInfoId()))
                resInfoViewHolder.icv_like.setState(true);
            if (user.getReportRecord().containsKey("resource" + resInfo.getInfoId()))
                resInfoViewHolder.iv_report.setColorFilter(context.getResources().getColor(R.color.blue));

            resInfoViewHolder.icv_like.setOnStateChangedListener(isSelected -> onItemLikeClickListener.onClick(isSelected, position));
            resInfoViewHolder.iv_report.setOnClickListener(v -> onItemReportClickListener.onClick(v, position));
        }
    }

    @Override
    public int getItemCount() {
        if (resInfoList == null)
            return 1;
        return resInfoList.size() == 0 ? 1 : resInfoList.size();
    }
}
