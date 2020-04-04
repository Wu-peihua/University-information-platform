package com.example.uipfrontend.CommonUser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.uipfrontend.R;

/*
 * 当某条评论被点击时跳转到这个Activity
 * 跳转时携带该条评论对象
 */

public class CommentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_comment_detail);
    }
}
