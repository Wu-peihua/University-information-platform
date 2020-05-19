package com.example.uipfrontend.Admin.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.Certification;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.lzy.ninegrid.ImageInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import info.hoang8f.widget.FButton;

public class AdminCertificationAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Certification> list;
    private List<String> universityList;
    private List<String> instituteList;

    private UserInfo userInfo;

    public AdminCertificationAdapter(Context context, List list,List universityList,List instituteList,UserInfo userInfo) {
        this.context = context;
        this.list = list;
        this.universityList = universityList;
        this.instituteList = instituteList;
        this.userInfo = userInfo;
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
    public interface StudentPassClickListener {
        void onPassClick(int position);
    }

    private StudentPassClickListener studentPassClickListener;

    public void setStudentPassClickListener(StudentPassClickListener studentPassClickListener) {
        this.studentPassClickListener = studentPassClickListener;
    }
    public interface StudentUnPassClickListener {
        void onUnPassClick(int position);
    }

    private StudentUnPassClickListener studentUnPassClickListener;

    public void setStudentUnPassClickListener(StudentUnPassClickListener studentUnPassClickListener) {
        this.studentUnPassClickListener = studentUnPassClickListener;
    }

    public void setList(List<Certification> list,List universityList,List instituteList){
        this.list = list;
        this.universityList = universityList;
        this.instituteList = instituteList;
    }


//
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_cer, null);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        //AdminCertificationAdapter.ViewHolder viewHolder = new AdminCertificationAdapter.ViewHolder(holder.itemView);

        ViewHolder viewHolder = new ViewHolder(holder.itemView);

        viewHolder.portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context, R.style.Theme_Design_NoActionBar);
                ImageView imgView = getView(viewHolder.portrait);
//                ImageView imgView = viewHolder.portrait;
                dialog.setContentView(imgView);
                dialog.show();
            //    dialog.dismiss();
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
// TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
            }
        });

        viewHolder.studentName.setText(list.get(pos).getStuName());
        viewHolder.studentNum.setText(list.get(pos).getStuNumber());
        viewHolder.studentTime.setText(list.get(pos).getCreated());
        viewHolder.studentSchool.setText(universityList.get(pos));
        viewHolder.studentCollege.setText(instituteList.get(pos));

        setImage(context,viewHolder.portrait,list.get(pos).getStuCard());

        //显示组队信息图片
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        ImageInfo info = new ImageInfo();
        if(list.get(pos).getStuCard() != null){
            String tempUrl[] = list.get(pos).getStuCard().split(",");
            for(String url : tempUrl){
                //localhost需改为服务器的ip才能正常显示图片
                info.setThumbnailUrl(url); //略缩图
                info.setBigImageUrl(url);  //点击放大图
                imageInfo.add(info);
            }
        }




        //临时图片地址
//       setImage(context,viewHolder.portrait,"http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg");
//        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
//        ImageInfo info = new ImageInfo();
//        info.setThumbnailUrl("http://image.biaobaiju.com/uploads/20180111/00/1515601824-VFslzoEJkU.jpg");
//        info.setBigImageUrl("http://image.biaobaiju.com/uploads/20180111/00/1515601824-VFslzoEJkU.jpg");
//        imageInfo.add(info);
//        viewHolder.portrait.setAdapter(new NineGridViewClickAdapter(context, imageInfo));



        viewHolder.pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentPassClickListener != null)
                    studentPassClickListener.onPassClick(pos);
            }
        });
        viewHolder.unpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentUnPassClickListener != null)
                    studentUnPassClickListener.onUnPassClick(pos);
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

    public ImageView getView(ImageView imgg) {
        ImageView imgView = new ImageView(context);
        imgView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        //@SuppressLint("ResourceType") InputStream is = context.getResources().openRawResource(R.drawable.portrait_default);
//        @SuppressLint("ResourceType") InputStream is = context.getResources().openRawResource(R.drawable.portrait_default);
//        Drawable drawable = BitmapDrawable.createFromStream(is, null);
        Drawable drawable = imgg.getDrawable();
        imgView.setImageDrawable(drawable);
        return imgView;
    }

}

