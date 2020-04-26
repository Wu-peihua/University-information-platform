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
import com.example.uipfrontend.Student.StudentActivity;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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

        getMenusData();

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

                    Set setUniversity = new HashSet();
                    Set setInstitute = new HashSet();


                    //循环遍历数组
                    int index = 0;
                    for (JsonElement jsonElement : jsonArrayUniversity) {
                        University university = new Gson().fromJson(jsonElement, new TypeToken<University>() {
                        }.getType());
                        levelOneUniversityMenu[index] = university.getUniversityName();
                        setUniversity.add(levelOneUniversityMenu[index]);
                        ++index;
                    }

//                    index = 0;
//                    for(int i=0;i<jsonArrayUniversity.size();++i){
//                        for (JsonElement jsonElement : jsonArrayInstitute) {
//                            Institute institute = new Gson().fromJson(jsonElement, new TypeToken<Institute>() {
//                            }.getType());
//
//                            levelTwoInstituteMenu[i][index] = institute.getInstituteName();
//                            ++index;
//                        }
//                    }
                    for (JsonElement jsonElement : jsonArrayInstitute) {
                        Institute institute = new Gson().fromJson(jsonElement, new TypeToken<Institute>() {
                        }.getType());
                        setInstitute.add(institute.getInstituteName());
                    }



                    //将大学数组和专业数组添加到shareprefernece
                    et.putStringSet("university", setUniversity);
                    et.putStringSet("institute",setInstitute);
                    et.commit();


                    Log.d(TAG, "resultUniversity: " + resultUniversity);
                    Log.d(TAG, "resultInstitute: " + resultInstitute);


                    Message msg = new Message();
                    msg.what = MenuDataOk;
                    handler.sendMessage(msg);

                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
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
