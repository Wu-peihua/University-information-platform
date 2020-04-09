package com.example.uipfrontend.Student.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Adapter.StudentMyReleaseViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

import static com.example.uipfrontend.CommonUser.Activity.AddResActivity.hideSystemKeyboard;

/****
 * 个人发布记录页面
 */
public class StudentMyReleaseActivity extends AppCompatActivity {

    ViewPager vp_studentRelease;
    TabLayout tl_studentRelease;

    private String[] titles = new String[]{"论坛记录","资源记录","组队记录","课程记录"};

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState ){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_my_release);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        vp_studentRelease = findViewById(R.id.vp_student_myRelease);
        tl_studentRelease = findViewById(R.id.tl_student_myRelease);

        initToolBar();
        init();

    }


    public void initToolBar() {

        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar_studentMyRelease);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void init(){

        // 创建ViewPager适配器
        StudentMyReleaseViewPagerAdapter studentMyReleaseViewPagerAdapter = new StudentMyReleaseViewPagerAdapter(getSupportFragmentManager(),titles);

        // 给ViewPager设置适配器
        vp_studentRelease.setAdapter(studentMyReleaseViewPagerAdapter);

        vp_studentRelease.setCurrentItem(0);

        tl_studentRelease.setupWithViewPager(vp_studentRelease);





    }

    @Override
    public void onResume() {
        int id = getIntent().getIntExtra("id", 0);

        if(id == 2){
            vp_studentRelease.setCurrentItem(1);
        }
        if(id == 3){
            vp_studentRelease.setCurrentItem(2);
        }
        if(id == 4){
            vp_studentRelease.setCurrentItem(3);
        }

        super.onResume();
    }

    //toolbar的操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
             * 将actionBar的HomeButtonEnabled设为ture，
             *
             * 将会执行此case
             */
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }



}
