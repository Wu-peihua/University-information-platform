package com.example.uipfrontend.Admin.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.Admin.Adapter.AdminReportRecruitRecyclerViewAdapter;
import com.example.uipfrontend.Entity.RecruitInfo;
import com.example.uipfrontend.Entity.ResponseRecruit;
import com.example.uipfrontend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import com.qlh.dropdownmenu.DropDownMenu;
import com.example.uipfrontend.Utils.MultiMenusView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class SubFragment1 extends Fragment {

    private String[] headers;//菜单头部选项
    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu dropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private XRecyclerView recyclerView;  //下拉刷新上拉加载
    private AdminReportRecruitRecyclerViewAdapter studentRecruitRecyclerViewAdapter;     //每条组队信息内容适配器

    private List<RecruitInfo> list;   //组队信息实体类数组
    private View rootView;  //根视图（下拉筛选框）
    private View rootContentView;    //根视图内容

    //private FloatingActionButton fabtn;  //浮动按钮，发布新的组队信息

    private String[] levelOneMenu;
    private String[][] levelTwoMenu;


    //分页请求数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    private static final int PAGE_SIZE = 6;   //默认一次请求6条数据
    private static int CUR_PAGE_NUM = 1;


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
           rootContentView = inflater.inflate(R.layout.fragment_admin_recruit_content,null);
       //     rootContentView=rootView;
            init();
        }
        return rootView;
    }

    private void init() {
        //初始化下拉筛选
        getMenusData();
        initMenus();
        initListener();

        //获取列表数据
        getData();


    }
    private void getMenusData(){
//        @SuppressLint("HandlerLeak")
//        Handler handler = new Handler() {
//            public void handleMessage(Message msg) {
//                if (msg.what == MenuDataOk) {//初始化下拉筛选
//
//                }
//                super.handleMessage(msg);
//            }
//        };


//        //发送http请求
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                Request requestUniversity = new Request.Builder().url(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryuniversity)).build();
//                Request requestInstitute = new Request.Builder().url(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryinstitute)).build();
//
//                try {
//                   //请求大学目录
//                    Response responseUniversity = client.newCall(requestUniversity).execute();//发送请求
//                    Response responseInstitute = client.newCall(requestInstitute).execute();
//                    String resultUniversity = Objects.requireNonNull(responseUniversity.body()).string();
//                    String resultInstitute = Objects.requireNonNull(responseInstitute.body()).string();
//
//                    //解析大学json字符串数组
//                    JsonObject jsonObjectUniversity = new JsonParser().parse(resultUniversity).getAsJsonObject();
//                    JsonArray jsonArrayUniversity = jsonObjectUniversity.getAsJsonArray("universityList");
//                    //解析专业json字符串数组
//                    JsonObject jsonObjectInstitute = new JsonParser().parse(resultInstitute).getAsJsonObject();
//                    JsonArray jsonArrayInstitute = jsonObjectInstitute.getAsJsonArray("instituteList");
//
//                    //初始化菜单栏
//                    levelOneMenu = new String[jsonArrayUniversity.size()];
//                    levelTwoMenu = new String[jsonArrayUniversity.size()][jsonArrayInstitute.size()];
//
//                    //循环遍历数组
//                    int index = 0;
//                    for (JsonElement jsonElement : jsonArrayUniversity) {
//                        University university = new Gson().fromJson(jsonElement, new TypeToken<University>() {
//                        }.getType());
//                        levelOneMenu[index] = university.getUniversityName();
//                        ++index;
//                    }
//
//                    index = 0;
//                    for(int i=0;i<jsonArrayUniversity.size();++i){
//                        for (JsonElement jsonElement : jsonArrayInstitute) {
//                            Institute institute = new Gson().fromJson(jsonElement, new TypeToken<Institute>() {
//                            }.getType());
//
//                            levelTwoMenu[i][index] = institute.getInstituteName();
//                            ++index;
//                        }
//                    }
//
//
//                    Log.d(TAG, "resultUniversity: " + resultUniversity);
//                    Log.d(TAG, "resultInstitute: " + resultInstitute);
//
//
//                    Message msg = new Message();
//                    msg.what = MenuDataOk;
//                    handler.sendMessage(msg);
//
//                } catch(IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

        //通过sharepreference获取顶部筛选菜单数据
        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("data",MODE_PRIVATE);
        //第二个参数为缺省值，如果不存在该key，返回缺省值
        Set<String> setUniversity = sp.getStringSet("university",null);
        Set<String> setInstitute = sp.getStringSet("institute",null);

        assert setUniversity != null;
        List<String> universityList = new ArrayList<>(setUniversity);
        assert setInstitute != null;
        List<String> instituteList = new ArrayList<>(setInstitute);

        levelOneMenu = universityList.toArray(new String[0]);
        String[] temp = instituteList.toArray(new String[0]);
        levelTwoMenu = new String [levelOneMenu.length][temp.length];

        for(int i = 0;i<levelOneMenu.length;++i){
            System.arraycopy(temp, 0, levelTwoMenu[i], 0, temp.length);
        }

    }


    private void initMenus() {

        dropDownMenu = rootView.findViewById(R.id.dropDownMenu_student_group);

        headers = new String[]{"所属院校"};

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
            public void getMenuOne(String var1, int position) {
                Toast.makeText(rootContentView.getContext(),var1,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void getMenuTwo(String var1, int position) {
                Toast.makeText(rootContentView.getContext(),var1,Toast.LENGTH_SHORT).show();

                dropDownMenu.setTabText(var1);
                dropDownMenu.closeMenu();
            }
        });

    }

    private void getData(){
        list = new ArrayList<>();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message){
                switch (message.what){
                    case SUCCESS:
                        Log.i("获取: ", "成功");
                        //初始化列表
                        initRecyclerView();
                        break;

                    case FAIL:
                        Log.i("获取: ", "失败");
                        break;

                    case ZERO:
                        Log.i("获取: ", "0");
                        Toast.makeText(rootContentView.getContext(),"暂时没有新的信息！",Toast.LENGTH_SHORT).show();
                        initRecyclerView();
                        break;
                }
            }
        };

        new Thread(()->{
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryRecruit)
                            + "/?pageNum=1&pageSize=" + PAGE_SIZE )
                    .get()
                    .build();
            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("获取: ", e.getMessage());
                    msg.what = FAIL;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    // System.out.println("recruit 请求返回数据:"+response.body().string());
                    ResponseRecruit responseRecruit = new Gson().fromJson(response.body().string(),
                            ResponseRecruit.class);


                    list = responseRecruit.getRecruitInfoList();
                    if(list.size() == 0) { //获取的数量为0
                        msg.what = ZERO;
                    } else {
                        msg.what = SUCCESS;
                    }
                    handler.sendMessage(msg);
                    Log.i("获取: ", String.valueOf(list.size()));
                }
            });
        }).start();
    }

    private void initRecyclerView() {

        recyclerView = rootContentView.findViewById(R.id.rv_student_group);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentRecruitRecyclerViewAdapter = new AdminReportRecruitRecyclerViewAdapter(this.getContext(), list);
        recyclerView.setAdapter(studentRecruitRecyclerViewAdapter);

        recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(() -> {
                    @SuppressLint("HandlerLeak")
                    Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            switch (msg.what){
                                case SUCCESS:
                                    Log.i("刷新", "成功");
                                    studentRecruitRecyclerViewAdapter.setList(list);
                                    studentRecruitRecyclerViewAdapter.notifyDataSetChanged();
                                    break;
                                case FAIL:
                                    Log.i("刷新", "失败");
                                    break;
                                case ZERO:
                                    Log.i("刷新", "0");
                                    break;
                            }
                            recyclerView.refreshComplete();
                        }
                    };

                    new Thread(()->{
                        CUR_PAGE_NUM = 1;
                        Request request = new Request.Builder()
                                .url(getResources().getString(R.string.serverBasePath) +
                                        getResources().getString(R.string.queryRecruit)
                                        + "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE +"&state=0")
                                .get()
                                .build();
                        Message msg = new Message();
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.i("获取: ", e.getMessage());
                                msg.what = FAIL;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                ResponseRecruit responseRecruit = new Gson().fromJson(response.body().string(),
                                        ResponseRecruit.class);
                                list = responseRecruit.getRecruitInfoList();
                                if(list.size() == 0) {
                                    msg.what = ZERO;
                                } else {
                                    msg.what = SUCCESS;
                                }
                                handler.sendMessage(msg);
                                Log.i("获取: ", String.valueOf(list.size()));
                            }
                        });
                    }).start();
                }, 1500);
                recyclerView.refreshComplete();

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(() -> {
                    @SuppressLint("HandlerLeak")
                    Handler handler = new Handler(){
                        @Override
                        public void handleMessage(Message msg){
                            switch (msg.what){
                                case SUCCESS:
                                    Log.i("加载", "成功");
                                    recyclerView.refreshComplete();
                                    studentRecruitRecyclerViewAdapter.notifyDataSetChanged();
                                    break;
                                case FAIL:
                                    Log.i("加载", "失败");
                                    break;
                                case ZERO:
                                    Log.i("加载", "0");
                                    recyclerView.setNoMore(true);
                                    break;
                            }
                        }
                    };

                    new Thread(()->{
                        CUR_PAGE_NUM++;
                        Request request = new Request.Builder()
                                .url(getResources().getString(R.string.serverBasePath) +
                                        getResources().getString(R.string.queryRecruit)
                                        + "/?pageNum="+ CUR_PAGE_NUM +"&pageSize=" + PAGE_SIZE + "&state=0")
                                .get()
                                .build();
                        Message msg = new Message();
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.i("获取: ", e.getMessage());
                                msg.what = FAIL;
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                ResponseRecruit responseRecruit = new Gson().fromJson(response.body().string(),
                                        ResponseRecruit.class);
                                list.addAll(responseRecruit.getRecruitInfoList());
                                if((CUR_PAGE_NUM - 2) * PAGE_SIZE + responseRecruit.getPageSize() <
                                        responseRecruit.getTotal() ){
                                    msg.what = ZERO;
                                } else {
                                    msg.what = SUCCESS;
                                }
                                handler.sendMessage(msg);
                                Log.i("获取: ", String.valueOf(list.size()));
                            }
                        });
                    }).start();
                }, 1500);
                recyclerView.setNoMore(true);
            }
        });

    }
}
