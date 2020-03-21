package com.example.uipfrontend.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Adapter.StudentFragmentAdapter;

import butterknife.BindView;

public class StudentActivity extends AppCompatActivity {

//    @BindView(R.id.vp_student)
    StudentViewPager studentViewPager;
//    @BindView(R.id.bbl_student)
    AHBottomNavigation studentAHBottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        init();
    }

    public void init(){

        studentViewPager = findViewById(R.id.vp_student);
        studentAHBottomNavigation = findViewById(R.id.btmNav_student);

        studentViewPager.setAdapter(new StudentFragmentAdapter(getSupportFragmentManager()));
        studentViewPager.setCurrentItem(0);
        studentViewPager.setScanScroll(false);

        //创建items，3个参数分别是item的文字，item的icon，选中item时的整体颜色（该项需要开启）
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("论坛讨论", R.drawable.forum_ed, R.color.lightBlue);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("资源分享", R.drawable.resource_ed, R.color.lightBlue);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("组队招聘", R.drawable.group_ed, R.color.lightBlue);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("课程点评", R.drawable.comment_ed, R.color.lightBlue);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("我的信息", R.drawable.home_ed, R.color.lightBlue);

        // Add items
        studentAHBottomNavigation.addItem(item1);
        studentAHBottomNavigation.addItem(item2);
        studentAHBottomNavigation.addItem(item3);
        studentAHBottomNavigation.addItem(item4);
        studentAHBottomNavigation.addItem(item5);

        //设置整体背景颜色（如果开启了单个的背景颜色，该项将会无效）
        studentAHBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.lightGray));

        //设置item被选中和待选时的颜色
        studentAHBottomNavigation.setAccentColor(getResources().getColor(R.color.darkBlue));
        studentAHBottomNavigation.setInactiveColor(getResources().getColor(R.color.blue));

        //是否开启切换item切换颜色
//        bottomNavigation.setColored(true);

        //设置初始选中的item
        studentAHBottomNavigation.setCurrentItem(0);

        // Set listener
//        bottomNavigation.setAHBottomNavigationListener(new AHBottomNavigation.AHBottomNavigationListener() {
//            @Override
//            public void onTabSelected(int position) {
//            }
//        });

        studentAHBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                studentViewPager.setCurrentItem(position);
                switch (position){
                    case 0:
                        setTitle("论坛讨论");
                        break;
                    case 1:
                        setTitle("资源分享");
                        break;
                    case 2:
                        setTitle("组队招聘");
                        break;
                    case 3:
                        setTitle("课程点评");
                        break;
                    case 4:
                        setTitle("我的信息");
                        break;
                }
                return true;
            }
        });
        studentAHBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {

            }
        });
    }
}
