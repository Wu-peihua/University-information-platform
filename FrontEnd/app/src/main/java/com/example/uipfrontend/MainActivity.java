package com.example.uipfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.uipfrontend.Admin.AdminHomeActivity;
import com.example.uipfrontend.CommonUser.CommonUserActivity;
import com.example.uipfrontend.Entity.Institute;
import com.example.uipfrontend.Entity.University;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.Entity.UserRecord;
import com.example.uipfrontend.Student.StudentActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.btn_commonUser)
    Button commonUser;

//    @BindView( R.id.btn_student)
    Button student;

//    @BindView( R.id.btn_admin)
    Button admin;
    //顶部筛选菜单选项
    private String[] levelOneUniversityMenu;
    private String[][] levelTwoInstituteMenu;

    private static final int MenuDataOk = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commonUser = findViewById(R.id.btn_commonUser);
        student = findViewById(R.id.btn_student);
        admin = findViewById(R.id.btn_admin);

        initUser();
        getMenusData();
        initListener();

    }

    private void initUser(){
        UserInfo user = (UserInfo) getApplication();
        user.setUserId(4L);
        user.setUserName("悟空");
        user.setPortrait("http://pic4.zhimg.com/50/v2-6ecab2cd6c1bbf9835030682db83543d_hd.jpg");
        getUserRecord(user);
    }

    private void getMenusData(){
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == MenuDataOk) {//初始化下拉筛选
                    initListener();
                }
                super.handleMessage(msg);
            }
        };


        //发送http请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request requestUniversity = new Request.Builder().url(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryUniversity)).build();
                Request requestInstitute = new Request.Builder().url(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryInstitute)).build();

                try {
                    //请求大学目录
                    Response responseUniversity = client.newCall(requestUniversity).execute();//发送请求
                    Response responseInstitute = client.newCall(requestInstitute).execute();
                    String resultUniversity = Objects.requireNonNull(responseUniversity.body()).string();
                    String resultInstitute = Objects.requireNonNull(responseInstitute.body()).string();


                    //解析大学json字符串数组
                    JsonObject jsonObjectUniversity = new JsonParser().parse(resultUniversity).getAsJsonObject();
                    JsonArray jsonArrayUniversity = jsonObjectUniversity.getAsJsonArray("universityList");
                    //解析专业json字符串数组
                    JsonObject jsonObjectInstitute = new JsonParser().parse(resultInstitute).getAsJsonObject();
                    JsonArray jsonArrayInstitute = jsonObjectInstitute.getAsJsonArray("instituteList");

                    //初始化菜单栏
                    levelOneUniversityMenu = new String[jsonArrayUniversity.size()];
                    levelTwoInstituteMenu = new String[jsonArrayUniversity.size()][jsonArrayInstitute.size()];

                    //设置sharepreference保存大学菜单和专业菜单
                    SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
                    @SuppressLint("CommitPrefEdits")
                    SharedPreferences.Editor et = sp.edit();

                    List<String> universityList = new ArrayList<>();
                    List<String> instituteList = new ArrayList<>();


                    //循环遍历数组
                    int index = 0;
                    for (JsonElement jsonElement : jsonArrayUniversity) {
                        University university = new Gson().fromJson(jsonElement, new TypeToken<University>() {
                        }.getType());
                        levelOneUniversityMenu[index] = university.getUniversityName();
                        universityList.add(levelOneUniversityMenu[index]);
                        ++index;
                    }

                    for (JsonElement jsonElement : jsonArrayInstitute) {
                        Institute institute = new Gson().fromJson(jsonElement, new TypeToken<Institute>() {
                        }.getType());
                        instituteList.add(institute.getInstituteName());
                    }

                    StringBuilder universityStrBuilder = new StringBuilder();
                    StringBuilder instituteStrBuilder = new StringBuilder();
                    boolean first = true;

                    //将universityList转为String
                    for(String string :universityList) {
                        if(first) {
                            first=false;
                        }else{
                            universityStrBuilder.append(",");
                        }
                        universityStrBuilder.append(string);
                    }

                    first = true;
                    //将universityList转为String
                    for(String string :instituteList) {
                        if(first) {
                            first=false;
                        }else{
                            instituteStrBuilder.append(",");
                        }
                        instituteStrBuilder.append(string);
                    }



                    //将大学数组和专业数组添加到shareprefernece
                    et.putString("university", universityStrBuilder.toString());
                    et.putString("institute",instituteStrBuilder.toString());
                    et.commit();

                    Message msg = new Message();
                    msg.what = MenuDataOk;
                    handler.sendMessage(msg);

                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void getUserRecord(UserInfo user) {
        new Thread(()->{
            
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                        + getResources().getString(R.string.getUserRecord)
                        + "/?userId=" + user.getUserId())
                    .get()
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("获取用户记录: ", Objects.requireNonNull(e.getMessage()));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = Objects.requireNonNull(response.body()).string();
                    Log.i("获取用户记录: ", resStr);

                    JsonObject jsonObject = new JsonParser().parse(resStr).getAsJsonObject();
                    JsonArray jsonArray1 = jsonObject.getAsJsonArray("likeRecord");
                    JsonArray jsonArray2 = jsonObject.getAsJsonArray("reportRecord");
                    
                    Map<String, Long> map1 = new HashMap<>();
                    Map<String, Long> map2 = new HashMap<>();
                    Gson gson = new Gson();
                    for (JsonElement element : jsonArray1) {
                        UserRecord record = gson.fromJson(element, new TypeToken<UserRecord>() {}.getType());
                        String key = "";
                        switch (record.getType()) {
                            case 1: key = "post"; break;
                            case 2: key = "comment"; break;
                            case 3: key = "reply"; break;
                        }
                        key += record.getToId();
                        map1.put(key, record.getInfoId());
                    }
                    for (JsonElement element : jsonArray2) {
                        UserRecord record = gson.fromJson(element, new TypeToken<UserRecord>() {}.getType());
                        String key = "";
                        switch (record.getType()) {
                            case 1: key = "post"; break;
                            case 2: key = "comment"; break;
                            case 3: key = "reply"; break;
                        }
                        key += record.getToId();
                        map2.put(key, record.getInfoId());
                    }
                    
                    user.setLikeRecord(map1);
                    user.setReportRecord(map2);
                }
            });
        }).start();
    }

    private void initListener(){
        commonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this , CommonUserActivity.class);
                //启动
                startActivity(i);
            }
        });


        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, StudentActivity.class);
                startActivity(i);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AdminHomeActivity.class);
                startActivity(i);
            }
        });
    }


}
