package com.example.uipfrontend.Student.Adapter;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.example.uipfrontend.Entity.ResponseRecruit;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.Entity.UserRecord;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Activity.RecruitReleaseActivity;
import com.example.uipfrontend.Utils.GlobalDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.lzy.widget.CircleImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentRecruitRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<RecruitInfo> list;
    private List<String> userNameList;
    private List<String> userPortraitList;

    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零

    private UserInfo userInfo;


    public StudentRecruitRecyclerViewAdapter(Context context, List list, List userNameList,List userPortraitList,UserInfo userInfo) {
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

        }


    }


    public void setList(List<RecruitInfo> list,List userNameList,List userPortraitList){
        this.list = list;
        this.userNameList = userNameList;
        this.userPortraitList = userPortraitList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_recruit, null);
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

        initReport(pos,viewHolder);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出对话框 选择拍照或从相册选择
                //弹出对话框 选择拍照或从相册选择
                new AlertView.Builder().setContext(view.getContext())
                        .setStyle(AlertView.Style.ActionSheet)
                        .setTitle("确定举报？")
                        .setCancelText("取消")
                        .setDestructive("确定")
                        .setOthers(null)
                        .setOnItemClickListener((object, position) -> {
                            switch (position) {
                                case 0:
                                    insertReport(pos,viewHolder);
                                    break;
                            }
                        })
                        .build()
                        .show();
            }
        });


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

    //初始化举报图标
    private void initReport(int position, ViewHolder viewHolder){
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
                        Toast.makeText(context, "获取举报信息失败", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        //前端设置已举报效果禁止点击
                        viewHolder.imageView.setColorFilter(context.getResources().getColor(R.color.blue));
                        viewHolder.imageView.setEnabled(false);

                        break;
                    case ZERO:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            Message msg = new Message();
            @Override
            public void run() {
                //建立client
                final OkHttpClient[] client = {new OkHttpClient()};

                //请求
                Request request=new Request.Builder()
                        .url(context.getResources().getString(R.string.serverBasePath)+context.getResources().getString(R.string.queryByUserIdAndObjectIdAndTagAndType) +
                                "/?userId="+ userInfo.getUserId() + "&objectId=" + list.get(position).getInfoId() + "&tag=" + 2 + "&type=" + 4)
                        .get()
                        .build();
                //新建call联结client和request
                Call call= client[0].newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //请求失败的处理
                        Log.i("RESPONSE","fail"+e.getMessage());
                        msg.what = FAIL;
                        handler.sendMessage(msg);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseStr = response.body().string();
                        Log.i("RESPONSE",responseStr);

                        UserRecord userRecord = new Gson().fromJson(responseStr,UserRecord.class);
                        if (userRecord == null){
                            msg.what = ZERO;
                        }else{
                            msg.what = SUCCESS;
                        }
                        handler.sendMessage(msg);

                    }

                });
            }
        }).start();
    }

    //插入举报记录
    private void insertReport(int position, ViewHolder viewHolder){
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
                        Toast.makeText(context, "举报失败", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        //前端设置已举报效果禁止点击
                        viewHolder.imageView.setColorFilter(context.getResources().getColor(R.color.blue));
                        viewHolder.imageView.setEnabled(false);
                        Toast.makeText(context, "举报成功", Toast.LENGTH_SHORT).show();
                        break;

                }
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            Message msg = new Message();
            @Override
            public void run() {
                //建立client
                final OkHttpClient[] client = {new OkHttpClient()};

                UserRecord userRecord = new UserRecord();
                userRecord.setTag(2);
                userRecord.setType(4);
                userRecord.setUserId(userInfo.getUserId());
                userRecord.setToId(list.get(position).getInfoId());


                //将传送实体类转为string类型的键值对
                Gson gson = new Gson();
                String json = gson.toJson(userRecord);

                //设置请求体并设置contentType
                RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"),json);                //请求
                Request request=new Request.Builder()
                        .url(context.getResources().getString(R.string.serverBasePath)+context.getResources().getString(R.string.insertReport))
                        .post(requestBody)
                        .build();
                //新建call联结client和request
                Call call= client[0].newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //请求失败的处理
                        Log.i("RESPONSE","fail"+e.getMessage());
                        msg.what = FAIL;
                        handler.sendMessage(msg);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseStr = response.body().string();
                        Log.i("RESPONSE",responseStr);

                        JsonObject jsonObject = new JsonParser().parse(responseStr).getAsJsonObject();
                        JsonElement element = jsonObject.get("result");

                        boolean result = new Gson().fromJson(element,boolean.class);

                        if (result){
                            msg.what = SUCCESS;
                        }else{
                            msg.what = FAIL;

                        }
                        handler.sendMessage(msg);

                    }

                });
            }
        }).start();
    }


}