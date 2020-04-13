package com.example.uipfrontend.CommonUser.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.uipfrontend.CommonUser.Adapter.CommonUserMyReleaseViewPagerAdapter;
import com.example.uipfrontend.R;
import com.google.android.material.tabs.TabLayout;

public class CommonUserMyReleaseActivity extends AppCompatActivity {
    ViewPager vp_myRelease;
    TabLayout tl_myRelease;

    private String[] titles = new String[]{"论坛记录", "资源记录"};

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cu_my_release);

        vp_myRelease = findViewById(R.id.vp_cu_myRelease);
        tl_myRelease = findViewById(R.id.tl_cu_myRelease);

        initToolBar();
        init();
    }


    public void initToolBar() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        setTitle("");
        Toolbar toolbar = findViewById(R.id.toolbar_cu_MyRelease);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void init() {
        // 创建ViewPager适配器
        CommonUserMyReleaseViewPagerAdapter adapter = new CommonUserMyReleaseViewPagerAdapter(getSupportFragmentManager(), titles);

        // 给ViewPager设置适配器
        vp_myRelease.setAdapter(adapter);

        vp_myRelease.setCurrentItem(0);

        tl_myRelease.setupWithViewPager(vp_myRelease);
    }

    @Override
    public void onResume() {
        int id = getIntent().getIntExtra("id", 0);
        if (id == 2) vp_myRelease.setCurrentItem(1);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
