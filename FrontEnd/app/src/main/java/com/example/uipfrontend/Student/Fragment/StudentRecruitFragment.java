package com.example.uipfrontend.Student.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.Entity.Institute;
import com.example.uipfrontend.Entity.University;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Activity.RecruitReleaseActivity;
import com.example.uipfrontend.Student.Adapter.StudentRecruitRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.qlh.dropdownmenu.view.MultiMenusView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class StudentRecruitFragment extends Fragment {

    private String[] headers;//菜单头部选项
    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu dropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private XRecyclerView recyclerView;  //下拉刷新上拉加载
    private StudentRecruitRecyclerViewAdapter studentRecruitRecyclerViewAdapter;     //每条组队信息内容适配器

    private List<String> list;   //组队信息实体类数组
    private View rootView;  //根视图（下拉筛选框）
    private View rootContentView;    //根视图内容

    private FloatingActionButton fabtn;  //浮动按钮，发布新的组队信息

    public static final int MenuDataOk = 0;

    private String[] levelOneMenu;
    private String[][] levelTwoMenu;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_student_recruit, null);
//            recyclerView = rootView.findViewById(R.id.rv_student_group);
            rootContentView = inflater.inflate(R.layout.fragment_student_recruit_content,null);
            init();
        }
        return rootView;
    }

    private void init() {


        //获取下拉目录显示数据
        getMenusData();


        //初始化浮动按钮
        initFAB();

    }


    private void getMenusData(){
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == MenuDataOk) {//初始化下拉筛选
                    initMenus();
                    initListener();
                    //获取列表数据
                    getData();
                    //初始化列表
                    initRecyclerView();
                    System.out.println("adfa asfsaf+++++++++++=======");
                }
                super.handleMessage(msg);
            }
        };


        //发送http请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request requestUniversity = new Request.Builder().url(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryuniversity)).build();
                Request requestInstitute = new Request.Builder().url(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryinstitute)).build();

                try {
                   //请求大学目录
                    Response responseUniversity = client.newCall(requestUniversity).execute();//发送请求
                    Response responseInstitute = client.newCall(requestInstitute).execute();
                    String resultUniversity = Objects.requireNonNull(responseUniversity.body()).string();
                    String resultInstitute = Objects.requireNonNull(responseInstitute.body()).string();

                    //解析大学json字符串数组
                    JsonObject jsonObjectUniversity = new JsonParser().parse(resultUniversity).getAsJsonObject();
                    JsonArray jsonArrayUniversity = jsonObjectUniversity.getAsJsonArray("universityList");
                    //解析专业json字符串数组
                    JsonObject jsonObjectInstitute = new JsonParser().parse(resultInstitute).getAsJsonObject();
                    JsonArray jsonArrayInstitute = jsonObjectInstitute.getAsJsonArray("instituteList");

                    //初始化菜单栏
                    levelOneMenu = new String[jsonArrayUniversity.size()];
                    levelTwoMenu = new String[jsonArrayUniversity.size()][jsonArrayInstitute.size()];

                    //循环遍历数组
                    int index = 0;
                    for (JsonElement jsonElement : jsonArrayUniversity) {
                        University university = new Gson().fromJson(jsonElement, new TypeToken<University>() {
                        }.getType());
                        levelOneMenu[index] = university.getUniversityName();
                        ++index;
                    }

                    index = 0;
                    for(int i=0;i<jsonArrayUniversity.size();++i){
                        for (JsonElement jsonElement : jsonArrayInstitute) {
                            Institute institute = new Gson().fromJson(jsonElement, new TypeToken<Institute>() {
                            }.getType());

                            levelTwoMenu[i][index] = institute.getInstituteName();
                            ++index;
                        }
                    }


                    Log.d(TAG, "resultUniversity: " + resultUniversity);
                    Log.d(TAG, "resultInstitute: " + resultInstitute);


                    Message msg = new Message();
                    msg.what = MenuDataOk;
                    handler.sendMessage(msg);

                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    private void initMenus() {

        dropDownMenu = rootView.findViewById(R.id.dropDownMenu_student_group);

        headers = new String[]{"所属院校"};
        //初始化多级菜单
//        final String[] levelOneMenu = {"全部", "华南师范大学", "华南理工大学", "中山大学"};
//        final String[][] levelTwoMenu = {
//                {"哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学","艺术学"},
//                {"哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学","艺术学"},
//                {"哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学","艺术学"},
//                {"哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学","艺术学"}
//        };
        multiMenusView = new MultiMenusView(this.getContext(),levelOneMenu,levelTwoMenu);
        popupViews.add(multiMenusView);
        //初始化内容视图
        RelativeLayout contentView = rootContentView.findViewById(R.id.rl_student_recruit_recruit);
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

    private void getData(){
        list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
    }

    private void initRecyclerView() {

        recyclerView = rootContentView.findViewById(R.id.rv_student_group);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentRecruitRecyclerViewAdapter = new StudentRecruitRecyclerViewAdapter(this.getContext(), list);
        recyclerView.setAdapter(studentRecruitRecyclerViewAdapter);

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

    private void initFAB(){
        fabtn = rootContentView.findViewById(R.id.fabtn_student_recruit);
        fabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContentView.getContext(), RecruitReleaseActivity.class);
                startActivity(intent);
            }
        });
    }

}