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
    private List list;

    public AdminReportRecruitRecyclerViewAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
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
    public void setList(List<RecruitInfo> list) { this.list = list; }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_report_recruit, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        ViewHolder viewHolder = new ViewHolder(holder.itemView);

        viewHolder.contact.setText("联系方式："+"13665665676");
        viewHolder.userName.setText("联系人："+"小红");
        viewHolder.infoDate.setText("2020-03-26 15:30");
        viewHolder.title.setText("比赛组队");
        viewHolder.content.setText("青研杯找队友，要求：认真负责，人数：2");
        //临时图片地址
        setImage(context,viewHolder.portrait,"http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg");
        viewHolder.portrait.setBorderWidth(0);
        //临时显示组队信息图片
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        ImageInfo info = new ImageInfo();
        info.setThumbnailUrl("http://image.biaobaiju.com/uploads/20180111/00/1515601824-VFslzoEJkU.jpg");
        info.setBigImageUrl("http://image.biaobaiju.com/uploads/20180111/00/1515601824-VFslzoEJkU.jpg");
        imageInfo.add(info);
        imageInfo.add(info);
        imageInfo.add(info);

        viewHolder.nineGridView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
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