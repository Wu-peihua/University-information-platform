package com.example.uipfrontend.CommonUser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uipfrontend.R;

/*
 * 点击搜索框旁边的+号跳转到这个Activity
 * 跳转时携带用户ID
 */
public class WritePostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_write_post);
    }
}
