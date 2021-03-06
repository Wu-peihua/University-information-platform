package com.example.uipfrontend.Admin.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.alertview.AlertView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.RecruitInfo;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class AdminReportRecruitRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<RecruitInfo> list;
    private List<String> userNameList;
    private List<String> userPortraitList;

    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零

    private UserInfo userInfo;

    public AdminReportRecruitRecyclerViewAdapter(Context context, List list,List userNameList,List userPortraitList,UserInfo userInfo) {
        this.context = context;
        this.list = list;
        this.userNameList = userNameList;
        this.userPortraitList = userPortraitList;
        this.userInfo = userInfo;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView content;
        CircleImageView portrait;
        TextView infoDate;
        TextView userName;
        TextView contact;
        NineGridView nineGridView;
        FButton pass;
        FButton unpass;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.admintv_student_group_item_title);
            this.content = itemView.findViewById(R.id.admintv_student_group_item_content);
            this.portrait = itemView.findViewById(R.id.adminiv_student_group_item_portrait);
            this.infoDate = itemView.findViewById(R.id.admintv_student_group_item_time);
            this.userName = itemView.findViewById(R.id.admintv_student_group_item_name);
            this.contact = itemView.findViewById(R.id.admintv_student_group_item_contact);
            this.nineGridView = itemView.findViewById(R.id.adminnineGrid_student_group_item_pic);
            this.pass=itemView.findViewById(R.id.admin_btn_pass_recruit);
            this.unpass=itemView.findViewById(R.id.admin_btn_unpass_recruit);

        }


    }

    public interface OnItemPassClickListener {
        void onPassClick(int position);
    }

    private OnItemPassClickListener onItemPassClickListener;

    public void setOnItemPassClickListener(OnItemPassClickListener onItemPassClickListener) {
        this.onItemPassClickListener = onItemPassClickListener;
    }
    public interface OnItemUnPassClickListener {
        void onUnPassClick(int position);
    }

    private OnItemUnPassClickListener onItemUnPassClickListener;

    public void setOnItemUnPassClickListener(OnItemUnPassClickListener onItemUnPassClickListener) {
        this.onItemUnPassClickListener = onItemUnPassClickListener;
    }


    public void setList(List<RecruitInfo> list,List userNameList,List userPortraitList){
        this.list = list;
        this.userNameList = userNameList;
        this.userPortraitList = userPortraitList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_report_recruit, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        ViewHolder viewHolder = new ViewHolder(holder.itemView);

        viewHolder.contact.setText("联系方式："+ list.get(pos).getContact());
        viewHolder.userName.setText("联系人："+ userNameList.get(pos));
        viewHolder.infoDate.setText(list.get(pos).getInfoDate());
        viewHolder.title.setText(list.get(pos).getTitle());
        viewHolder.content.setText(list.get(pos).getContent());
        //发布人头像
        setImage(context,viewHolder.portrait,userPortraitList.get(pos));
        viewHolder.portrait.setBorderWidth(0);


        //显示组队信息图片
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        ImageInfo info;
        if(list.get(pos).getPictures() != null){
            String tempUrl[] = list.get(pos).getPictures().split(",");
            for(String url : tempUrl){
                info = new ImageInfo();
                url = url.substring(1,url.length()-1);
                //localhost需改为服务器的ip才能正常显示图片
                info.setThumbnailUrl(url); //略缩图
                info.setBigImageUrl(url);  //点击放大图
                imageInfo.add(info);
            }
        }

        viewHolder.nineGridView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));

        viewHolder.pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemPassClickListener != null)
                    onItemPassClickListener.onPassClick(pos);
            }
        });
        viewHolder.unpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemUnPassClickListener != null)
                    onItemUnPassClickListener.onUnPassClick(pos);
            }
        });
    }


    private void setImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public int getItemCount() {

        return list.size();

    }


}
