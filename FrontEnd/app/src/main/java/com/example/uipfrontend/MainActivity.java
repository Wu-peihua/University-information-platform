package com.example.uipfrontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.uipfrontend.Admin.AdminHomeActivity;
import com.example.uipfrontend.CommonUser.CommonUserActivity;
import com.example.uipfrontend.Student.StudentActivity;


public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.btn_commonUser)
    Button commonUser;

//    @BindView( R.id.btn_student)
    Button student;

//    @BindView( R.id.btn_admin)
    Button admin;
    //自定义顶部导航栏
    Toolbar toolbar;
    TextView tv_toolBar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commonUser = findViewById(R.id.btn_commonUser);
        student = findViewById(R.id.btn_student);
        admin = findViewById(R.id.btn_admin);
//        toolbar = (Toolbar)findViewById(R.id.toolbar);
//        tv_toolBar_title = findViewById(R.id.tv_toolBar_title);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        tv_toolBar_title.setTextColor("高校交流平台");




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
