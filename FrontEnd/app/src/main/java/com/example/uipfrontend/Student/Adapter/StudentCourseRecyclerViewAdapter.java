package com.example.uipfrontend.Student.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.R;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.widget.CircleImageView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Course> mTags = new ArrayList<>();

    public StudentCourseRecyclerViewAdapter(Context context, List<Course> list) {
        this.context = context;
        this.mTags = list;
    }

    public void setList(List<Course> list) { this.mTags = list; }



    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout card_item;  // 对cardview设置监听
        TextView name;//课程名称
        TextView content;//课程内容
        TextView teacher;//教师
        TextView score;//总评分
        ImageView img;//课程图片

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.courseImage);
            name = itemView.findViewById(R.id.coursename);
            content = itemView.findViewById(R.id.courseDescription);
            teacher = itemView.findViewById(R.id.Teacher);
            score = itemView.findViewById(R.id.RatingScore);
            card_item = itemView.findViewById(R.id.item_course);

        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course_card, parent, false);


        ViewHolder holder = new ViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        ViewHolder viewHolder = new ViewHolder(holder.itemView);
        Course course = mTags.get(pos);
        viewHolder.name.setText(course.getName());
        viewHolder.teacher.setText(course.getTeacher());
        //加载课程图片
        //Glide.with(context).load("https://cdnp0.stackassets.com/a11acbee21b698eacc6c9e4eedc0cda167ad2212/store/opt/596/298/b58d430921e8f3f5bbada7638202b59404de5443a5f8fea0ccddd38afa9a/product_13994_product_shot_wide_image.jpg"
        //).into(viewHolder.img);

        Glide.with(context).load(course.getCoursePicture())
                .placeholder(R.drawable.coding_class)
                .error(R.drawable.coding_class)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);

        BigDecimal b   =   new   BigDecimal(course.getScore());
        float trimScore =   b.setScale(1,   BigDecimal.ROUND_HALF_UP).floatValue();
        viewHolder.score.setText(String.valueOf(trimScore));
        viewHolder.content.setText(course.getDescription());
        viewHolder.card_item.setOnClickListener(view -> itemClickListener.onItemClick(pos));
        //System.out.println("当前课程"+course.getName());
        //System.out.println("当前分数"+course.getScore());
        //System.out.println("当前教师"+course.getTeacher());

        //设置点击监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    Log.e("click course item", "!");
                    itemClickListener.onItemClick(pos);
                }
            }
        });



    }


    /*设置item 监听接口*/
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        //void onClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listenser) {
        this.itemClickListener = listenser;
    }


    @Override
    public int getItemCount() {

//        return list.size();

        return mTags.size();
    }


}