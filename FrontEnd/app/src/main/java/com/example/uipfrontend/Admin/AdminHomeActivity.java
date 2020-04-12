package com.example.uipfrontend.Admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.uipfrontend.Admin.Adapter.AdminFragmentAdapter;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.StudentActivity;
import com.lzy.ninegrid.NineGridView;
import com.squareup.picasso.Picasso;

public class AdminHomeActivity extends AppCompatActivity {

//    @BindView(R.id.vp_admin)
    AdminViewPager adminViewPager;
//    @BindView(R.id.bbl_admin)
    AHBottomNavigation adminAHBottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        getSupportActionBar().hide();

        init();
    }

    public void init(){
        NineGridView.setImageLoader(new AdminHomeActivity.PicassoImageLoader());

        adminViewPager = findViewById(R.id.vp_admin);
        adminAHBottomNavigation = findViewById(R.id.btmNav_admin);

        adminViewPager.setAdapter(new AdminFragmentAdapter(getSupportFragmentManager()));
        adminViewPager.setCurrentItem(0);
        adminViewPager.setScanScroll(false);

        //创建items，3个参数分别是item的文字，item的icon，选中item时的整体颜色（该项需要开启）
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("学生认证", R.drawable.forum_ed, R.color.lightBlue);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("举报情况", R.drawable.resource_ed, R.color.lightBlue);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("课程发布", R.drawable.comment_ed, R.color.lightBlue);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("我的信息", R.drawable.home_ed, R.color.lightBlue);

        // Add items
        adminAHBottomNavigation.addItem(item1);
        adminAHBottomNavigation.addItem(item2);
        adminAHBottomNavigation.addItem(item3);
        adminAHBottomNavigation.addItem(item4);


        //设置整体背景颜色（如果开启了单个的背景颜色，该项将会无效）
        adminAHBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.lightGray));

        //设置item被选中和待选时的颜色
        adminAHBottomNavigation.setAccentColor(getResources().getColor(R.color.darkBlue));
        adminAHBottomNavigation.setInactiveColor(getResources().getColor(R.color.blue));

        //是否开启切换item切换颜色
//        bottomNavigation.setColored(true);

        adminAHBottomNavigation.setCurrentItem(0);

        // Set listener
//        bottomNavigation.setAHBottomNavigationListener(new AHBottomNavigation.AHBottomNavigationListener() {
//            @Override
//            public void onTabSelected(int position) {
//            }
//        });

//        adminAHBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
//            @Override
//            public boolean onTabSelected(int position, boolean wasSelected) {
//                adminViewPager.setCurrentItem(position);
//                switch (position){
//                    case 0:
//                        setTitle("学生认证");
//                        break;
//                    case 1:
//                        setTitle("举报情况");
//                        break;
//                    case 2:
//                        setTitle("课程发布");
//                        break;
//                    case 3:
//                        setTitle("我的信息");
//                        break;
//                }
//                return true;
//            }
//        });
        adminAHBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                adminViewPager.setCurrentItem(position);
                switch (position){
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
                return true;
            }
        });
        adminAHBottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {

            }
        });

    }
    /** Picasso 加载 */
    private class PicassoImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Picasso.with(context).load(url)//
                    .placeholder(R.drawable.ic_default_image)//
                    .error(R.drawable.ic_default_image)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
