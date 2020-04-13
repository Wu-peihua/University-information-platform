package com.example.uipfrontend.Admin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.R;

import com.lzy.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class AdminCertificationAdapter extends RecyclerView.Adapter {
    private Context context;
    private List list;

    public AdminCertificationAdapter(Context context, List list) {
        this.context = context;
        this.list = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView studentName;
        TextView studentNum;
        ImageView portrait;
        TextView studentSchool;
        TextView studentCollege;
        FButton pass;
        FButton unpass;
        TextView studentTime;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.studentName = itemView.findViewById(R.id.admin_student_name);
            this.studentNum = itemView.findViewById(R.id.admin_student_num);
            this.portrait = itemView.findViewById(R.id.admin_student_picture);
            this.studentSchool = itemView.findViewById(R.id.admin_student_school);
            this.studentCollege = itemView.findViewById(R.id.admin_student_college);
            this.pass=itemView.findViewById(R.id.admin_btn_pass_cer);
            this.unpass=itemView.findViewById(R.id.admin_btn_unpass_cer);
            this.studentTime=itemView.findViewById(R.id.admin_student_time);
        }


    }

//
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_cer, null);
        return new AdminCertificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        AdminCertificationAdapter.ViewHolder viewHolder = new AdminCertificationAdapter.ViewHolder(holder.itemView);

        viewHolder.studentName.setText("王大明");
        viewHolder.studentNum.setText("20172131154");
        viewHolder.studentTime.setText("2020-04-13 15:30");
        viewHolder.studentSchool.setText("华南师范大学");
        viewHolder.studentCollege.setText("计算机学院");
        //临时图片地址
        setImage(context,viewHolder.portrait,"http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg");

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

//        return list.size();

        return 10;
    }

}
