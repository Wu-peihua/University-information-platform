package com.example.uipfrontend.CommonUser;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.uipfrontend.CommonUser.Adapter.CommonUserFragmentAdapter;
import com.example.uipfrontend.R;

public class CommonUserActivity extends AppCompatActivity {

    private CommonUserViewPager commonUserViewPager;
    AHBottomNavigation commonUserAHBottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_user);

        //沉浸式状态栏
        //布局文件同样要加上一行适应系统的代码
        getSupportActionBar().hide();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue));

        init();
    }

    private void init() {

        commonUserViewPager = findViewById(R.id.vp_common_user);
        commonUserAHBottomNav = findViewById(R.id.btmNav_common_user);

        commonUserViewPager.setAdapter(new CommonUserFragmentAdapter(getSupportFragmentManager()));
        commonUserViewPager.setCurrentItem(0);
        commonUserViewPager.setScanScroll(false);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("论坛讨论", R.drawable.forum_ed);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("资源分享", R.drawable.resource_ed);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("我的信息", R.drawable.home_ed);

        commonUserAHBottomNav.addItem(item1);
        commonUserAHBottomNav.addItem(item2);
        commonUserAHBottomNav.addItem(item3);

        commonUserAHBottomNav.setCurrentItem(0);

        // 导航栏背景色
        commonUserAHBottomNav.setDefaultBackgroundColor(getResources().getColor(R.color.lightGray));
        // 导航栏图标选中颜色
        commonUserAHBottomNav.setAccentColor(getResources().getColor(R.color.darkBlue));
        // 导航栏图标未选中颜色
        commonUserAHBottomNav.setInactiveColor(getResources().getColor(R.color.blue));

        commonUserAHBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            commonUserViewPager.setCurrentItem(position);
            return true;
        });
    }
}
