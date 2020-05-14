package com.example.uipfrontend.Utils;

import android.content.Context;
import android.util.Log;

import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.Entity.UserRecord;
import com.example.uipfrontend.R;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserOperationRecord {

    /**
     * 描述：用户添加操作
     * 参数：context- 获取string里的地址
     *      record- 记录对象
     *      user- 插入成功后返回主键，在用户记录表添加新记录
     * 返回：void
     */
    public static void insertRecord(Context context, UserRecord record, UserInfo user) {

        new Thread(()->{

            OkHttpClient client = new OkHttpClient();
            
            String json = new Gson().toJson(record);
            RequestBody requestBody = FormBody.create(json, MediaType.parse("application/json;charset=utf-8"));
            
            Request request = new Request.Builder()
                    .url(context.getResources().getString(R.string.serverBasePath)
                            + context.getResources().getString(R.string.insertUserRecord))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("添加记录：", Objects.requireNonNull(e.getMessage()));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("添加记录：", resStr);

                    String resId = resStr.substring(1, resStr.length()-1).split(":")[1];
                    if (isNumber(resId)) {
                        String key = "";
                        switch (record.getType()) {
                            case 1: key = "post"; break;
                            case 2: key = "comment"; break;
                            case 3: key = "reply"; break;
                            case 5: key = "course_comment";break;
                        }
                        key += record.getToId();
                        
                        switch (record.getTag()) {
                            case 1:
                                user.getLikeRecord().put(key, Long.valueOf(resId));
                                break;
                            case 2: 
                                user.getReportRecord().put(key, Long.valueOf(resId));
                                break;
                        }
                    }
                    
                }
            });

        }).start();

    }

    /**
     * 描述：用户取消操作
     * 参数：context- 获取string里的地址
     *      infoId- 点赞记录id
     * 返回：void
     */
    public static void deleteRecord(Context context, Long infoId) {

        new Thread(()->{

            OkHttpClient client = new OkHttpClient();

            FormBody.Builder builder = new FormBody.Builder();
            builder.add("infoId", String.valueOf(infoId));
            RequestBody requestBody = builder.build();

            Request request = new Request.Builder()
                    .url(context.getResources().getString(R.string.serverBasePath)
                            + context.getResources().getString(R.string.deleteUserRecord))
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("删除记录：", Objects.requireNonNull(e.getMessage()));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("删除记录：", resStr);
                }
            });

        }).start();

    }

    /**
     * 描述：判断字符串是否全部为数字，用于插入记录的返回值判断
     * 参数：插入评论的返回值字符串
     * 返回：true：全部为数字，插入成功，返回的是评论的infoId
     *      false：包含非数字，插入失败，返回的是错误信息
     */
    private static boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
