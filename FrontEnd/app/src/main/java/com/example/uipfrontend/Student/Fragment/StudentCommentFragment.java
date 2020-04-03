package com.example.uipfrontend.Student.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.Course;
import com.example.uipfrontend.Student.Adapter.StudentCourseRecyclerViewAdapter;
import com.example.uipfrontend.Student.CourseDetailActivity;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.qlh.dropdownmenu.view.MultiMenusView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class StudentCommentFragment extends Fragment {

    private String[] headers;//菜单头部选项
    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu dropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private XRecyclerView recyclerView;  //下拉刷新上拉加载
    private StudentCourseRecyclerViewAdapter studentCourseRecyclerViewAdapter;     //课程内容适配器

    private View rootView;
    private List<Course> mTags=new ArrayList<>();//课程实体数组


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_student_comment, null);
            recyclerView = rootView.findViewById(R.id.rv_student_course);

            init();
        }
        return rootView;
    }

    private void init() {
        initMenus();
        initListener();
        initData();
        initRecyclerView();

    }

    private void initMenus() {

        dropDownMenu = rootView.findViewById(R.id.dropDownMenu_student_course);
        headers = new String[]{"所属院校"};
        //初始化多级菜单
        final String[] levelOneMenu = {"全部", "华南师范大学", "华南理工大学", "中山大学"};
        //学院 暂定18个
        final String[][] levelTwoMenu = {
                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"},
                {"计算机学院","软件学院","信息光电子学院","数学科学学院","地理科学学院","生命科学学院","外国语言文化学院","经济与管理学院","化学学院",
                        "心理学院","文学院","法学院","物理与电信工程学院","马克思学院","历史文化学院","音乐学院","教育信息技术学院","教育科学学院"}

        };
        multiMenusView = new MultiMenusView(this.getContext(),levelOneMenu,levelTwoMenu);
        popupViews.add(multiMenusView);
        //初始化内容视图
        View contentView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_student_comment,null);
        //装载
        dropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,contentView);

    }

    private void initListener() {

        //下拉菜单
        multiMenusView.setOnSelectListener(new MultiMenusView.OnSelectListener() {
            @Override
            public void getValue(String showText) {
                dropDownMenu.setTabText(showText);
                dropDownMenu.closeMenu();
            }
        });

    }


    public void initData() {
        for (int i = 0; i < 10; i++) {
            Course course = new Course("大数据与云计算", "Mr.ZHANG", "大数据与云计算平台使用", 2.50);
            mTags.add(course);
        }


        //count = mTags.size();
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentCourseRecyclerViewAdapter = new StudentCourseRecyclerViewAdapter(this.getContext(), mTags);
        recyclerView.setAdapter(studentCourseRecyclerViewAdapter);

        //设置item 点击跳转至课程详情页面
        studentCourseRecyclerViewAdapter.setOnItemClickListener(new StudentCourseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Course course = (Course) mTags.get(position);
                String name = course.getName();
                Log.i("点击了","name:"+name);
                Intent intent = new Intent(getContext(), CourseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                intent.putExtras(bundle);
                Log.e("Main", "" + position + "被点击了！！");
                startActivity(intent);

            }
        });



        recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                new Handler().postDelayed(() -> {
//                    @SuppressLint("HandlerLeak")
//                    Handler handler = new Handler(){
//                        @Override
//                        public void handleMessage(Message msg){
//                            switch (msg.what){
//                                case SUCCESS:
//                                    Log.i("刷新", "成功");
//                                    adapter.setList(infoList);
//                                    adapter.notifyDataSetChanged();
//                                    break;
//                                case FAIL:
//                                    Log.i("刷新", "失败");
//                                    break;
//                                case ZERO:
//                                    Log.i("刷新", "0");
//                                    break;
//                            }
//                            recyclerView.refreshComplete();
//                        }
//                    };

//                    new Thread(()->{
//                        CUR_PAGE_NUM = 1;
//                        Request request = new Request.Builder()
//                                .url(getResources().getString(R.string.serverBasePath) +
//                                        getResources().getString(R.string.getAdoptMessage)
//                                        + "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE +"&state=0")
//                                .get()
//                                .build();
//                        Message msg = new Message();
//                        OkHttpClient okHttpClient = new OkHttpClient();
//                        Call call = okHttpClient.newCall(request);
//                        call.enqueue(new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                Log.i("获取: ", e.getMessage());
//                                msg.what = FAIL;
//                                handler.sendMessage(msg);
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//
//                                ResponseAdoptInfo adoptMessage = new Gson().fromJson(response.body().string(),
//                                        ResponseAdoptInfo.class);
//                                infoList = adoptMessage.getAdoptInfoList();
//                                if(infoList.size() == 0) {
//                                    msg.what = ZERO;
//                                } else {
//                                    msg.what = SUCCESS;
//                                }
//                                handler.sendMessage(msg);
//                                Log.i("获取: ", String.valueOf(infoList.size()));
//                            }
//                        });
//                    }).start();
//                }, 1500);
                recyclerView.refreshComplete();

            }

            @Override
            public void onLoadMore() {
//                new Handler().postDelayed(() -> {
//                    @SuppressLint("HandlerLeak")
//                    Handler handler = new Handler(){
//                        @Override
//                        public void handleMessage(Message msg){
//                            switch (msg.what){
//                                case SUCCESS:
//                                    Log.i("加载", "成功");
//                                    recyclerView.refreshComplete();
//                                    adapter.notifyDataSetChanged();
//                                    break;
//                                case FAIL:
//                                    Log.i("加载", "失败");
//                                    break;
//                                case ZERO:
//                                    Log.i("加载", "0");
//                                    recyclerView.setNoMore(true);
//                                    break;
//                            }
//                        }
//                    };

//                    new Thread(()->{
//                        CUR_PAGE_NUM++;
//                        Request request = new Request.Builder()
//                                .url(getResources().getString(R.string.serverBasePath) +
//                                        getResources().getString(R.string.getAdoptMessage)
//                                        + "/?pageNum="+ CUR_PAGE_NUM +"&pageSize=" + PAGE_SIZE + "&state=0")
//                                .get()
//                                .build();
//                        Message msg = new Message();
//                        OkHttpClient okHttpClient = new OkHttpClient();
//                        Call call = okHttpClient.newCall(request);
//                        call.enqueue(new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//                                Log.i("获取: ", e.getMessage());
//                                msg.what = FAIL;
//                                handler.sendMessage(msg);
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//
//                                ResponseAdoptInfo adoptMessage = new Gson().fromJson(response.body().string(),
//                                        ResponseAdoptInfo.class);
//                                infoList.addAll(adoptMessage.getAdoptInfoList());
//                                if((CUR_PAGE_NUM - 2) * PAGE_SIZE + adoptMessage.getPageSize() <
//                                        adoptMessage.getTotal() ){
//                                    msg.what = ZERO;
//                                } else {
//                                    msg.what = SUCCESS;
//                                }
//                                handler.sendMessage(msg);
//                                Log.i("获取: ", String.valueOf(infoList.size()));
//                            }
//                        });
//                    }).start();
//                }, 1500);
                recyclerView.setNoMore(true);
            }
        });

    }


}