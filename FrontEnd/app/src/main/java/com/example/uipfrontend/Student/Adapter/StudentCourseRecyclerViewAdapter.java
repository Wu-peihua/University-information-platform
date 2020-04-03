package com.example.uipfrontend.Student.Adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.List;

public class StudentCourseRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Course> mTags = new ArrayList<>();

    public StudentCourseRecyclerViewAdapter(Context context, List<Course> list) {
        this.context = context;
        this.mTags = list;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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

        }
    }


//        @OnClick(R.id.tv_student_group_item_delete)
//        public void delete(View view) {
//            final GlobalDialog delDialog = new GlobalDialog(context);
//            delDialog.setCanceledOnTouchOutside(true);
//            delDialog.getTitle().setText("提示");
//            delDialog.getContent().setText("确定删除吗?");
//            delDialog.setLeftBtnText("取消");
//            delDialog.setRightBtnText("确定");
//            delDialog.setLeftOnclick(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
//                    delDialog.dismiss();
//                }
//            });
//            delDialog.setRightOnclick(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "确定", Toast.LENGTH_SHORT).show();
//                    delDialog.dismiss();
//                }
//            });
//            delDialog.show();
//        }



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

        viewHolder.name.setText(mTags.get(pos).getName());
        viewHolder.teacher.setText(mTags.get(pos).getTeacher());
        viewHolder.score.setText(String.valueOf(mTags.get(pos).getScore()));
        viewHolder.content.setText(mTags.get(pos).getDescription());

        //设置点击监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    //Log.e("click", "!");
                    itemClickListener.onItemClick(pos);
                }
            }
        });

        /*
        * holder.tvName.setText(mTags.get(position).getName());
        holder.tvTeacher.setText(mTags.get(position).getTeacher());
        holder.tvScore.setText(String.valueOf(mTags.get(position).getScore()));
        holder.tvContent.setText(mTags.get(position).getDescription());


        //holder.bindData(mTags.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    Log.e("click", "!");
                    itemClickListener.onItemClick(position);
                }
            }
        });
        * */


    }


    /*设置item 监听接口*/
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
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