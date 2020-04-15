package com.example.uipfrontend.Student.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.uipfrontend.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.uipfrontend.CommonUser.Activity.AddResActivity.hideSystemKeyboard;

public class StudentModifyPasswordActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        initToolBar();

    }

    public void initToolBar() {

        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar_modifyPassword);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //toolbar带有保存按钮
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit_menu, menu);
        return true;
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
            case R.id.submit:

        }
        return super.onOptionsItemSelected(item);
    }


}
