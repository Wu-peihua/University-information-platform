package com.example.uipfrontend.Student.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
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
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Activity.RecruitReleaseActivity;
import com.example.uipfrontend.Utils.GlobalDialog;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

public class StudentRecruitRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List list;

    public StudentRecruitRecyclerViewAdapter(Context context, List list) {
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
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.tv_student_group_item_title);
            this.content = itemView.findViewById(R.id.tv_student_group_item_content);
            this.portrait = itemView.findViewById(R.id.iv_student_group_item_portrait);
            this.infoDate = itemView.findViewById(R.id.tv_student_group_item_time);
            this.userName = itemView.findViewById(R.id.tv_student_group_item_name);
            this.contact = itemView.findViewById(R.id.tv_student_group_item_contact);
            this.nineGridView = itemView.findViewById(R.id.nineGrid_student_group_item_pic);
            this.imageView = (ImageView) itemView.findViewById(R.id.iv_student_recruit_report);

            this.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出对话框 选择拍照或从相册选择
                    new AlertView.Builder().setContext(view.getContext())
                            .setStyle(AlertView.Style.ActionSheet)
                            .setTitle("确定举报？")
//                            .setCancelText("取消")
                            .setDestructive("取消", "确定")
                            .setOthers(null)
                            .setOnItemClickListener((object, position) -> {
                                switch (position) {
                                    case 0:
                                        Toast.makeText(context,"取消举报",Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(context,"确定举报",Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            })
                            .build()
                            .show();
                }
            });
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_recruit, null);
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

//        return list.size();

        return 10;
    }


}