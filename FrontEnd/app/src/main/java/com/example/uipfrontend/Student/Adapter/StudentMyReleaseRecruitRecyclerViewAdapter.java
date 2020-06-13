package com.example.uipfrontend.Student.Adapter;

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
import com.example.uipfrontend.CommonUser.Adapter.MyReleaseResInfoAdapter;
import com.example.uipfrontend.Entity.RecruitInfo;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.R;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class StudentMyReleaseRecruitRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<RecruitInfo> list;
    private List<String> userNameList;
    private List<String> userPortraitList;


    public StudentMyReleaseRecruitRecyclerViewAdapter(Context context, List<RecruitInfo> list,List userNameList,List userPortraitList) {
        this.context = context;
        this.list = list;
        this.userNameList = userNameList;
        this.userPortraitList = userPortraitList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView content;
        CircleImageView portrait;
        TextView infoDate;
        TextView userName;
        TextView contact;
        NineGridView nineGridView;
        TextView delete;
        TextView modify;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.tv_studentReleaseRecruitTitle);
            this.content = itemView.findViewById(R.id.tv_studentReleaseRecruitContent);
            this.portrait = itemView.findViewById(R.id.iv_studentReleaseRecruitPortrait);
            this.infoDate = itemView.findViewById(R.id.tv_studentReleaseRecruitDate);
            this.userName = itemView.findViewById(R.id.tv_studentReleaseRecruitName);
            this.contact = itemView.findViewById(R.id.tv_studentReleaseRecruitContact);
            this.nineGridView = itemView.findViewById(R.id.nineGrid_studentReleaseRecruitPhoto);
            this.delete = itemView.findViewById(R.id.tv_studentReleaseRecruitDelete);
            this.modify = itemView.findViewById(R.id.tv_studentReleaseRecruitModify);

        }


    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_release_recruit, null);
        return new StudentMyReleaseRecruitRecyclerViewAdapter.ViewHolder(view);    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        StudentMyReleaseRecruitRecyclerViewAdapter.ViewHolder viewHolder = new StudentMyReleaseRecruitRecyclerViewAdapter.ViewHolder(holder.itemView);

        viewHolder.contact.setText("联系方式："+list.get(pos).getContact());
        viewHolder.userName.setText("联系人："+userNameList.get(pos));
        viewHolder.infoDate.setText(list.get(pos).getInfoDate().toString());
        viewHolder.title.setText(list.get(pos).getTitle());
        viewHolder.content.setText(list.get(pos).getContent());
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


        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemDeleteClickListener != null)
                    onItemDeleteClickListener.onDeleteClick(pos);
            }
        });
        viewHolder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemModifyClickListener != null)
                    onItemModifyClickListener.onModifyClick(pos);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    //删除与修改
    public interface OnItemDeleteClickListener {
        void onDeleteClick(int position);
    }

    private MyReleaseResInfoAdapter.OnItemDeleteClickListener onItemDeleteClickListener;

    public void setOnItemDeleteClickListener(MyReleaseResInfoAdapter.OnItemDeleteClickListener onItemDeleteClickListener) {
        this.onItemDeleteClickListener = onItemDeleteClickListener;
    }

    public interface OnItemModifyClickListener {
        void onModifyClick(int position);
    }

    private MyReleaseResInfoAdapter.OnItemModifyClickListener onItemModifyClickListener;

    public void setOnItemModifyClickListener(MyReleaseResInfoAdapter.OnItemModifyClickListener onItemModifyClickListener) {
        this.onItemModifyClickListener = onItemModifyClickListener;
    }

    public void setList(List<RecruitInfo> list,List userNameList,List userPortraitList){
        this.list = list;
        this.userNameList = userNameList;
        this.userPortraitList = userPortraitList;
    }

    private void setImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .placeholder(R.drawable.portrait_default)
                .error(R.drawable.portrait_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

}
