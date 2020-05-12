package com.example.uipfrontend.Student.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.Entity.RecruitInfo;
import com.example.uipfrontend.Entity.ResponseRecruit;
import com.example.uipfrontend.MainActivity;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Activity.RecruitReleaseActivity;
import com.example.uipfrontend.Student.Adapter.StudentRecruitRecyclerViewAdapter;
import com.example.uipfrontend.Utils.MultiMenusView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

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

import static android.content.Context.MODE_PRIVATE;


public class StudentRecruitFragment extends Fragment {

    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu dropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private XRecyclerView recyclerView;  //下拉刷新上拉加载
    private StudentRecruitRecyclerViewAdapter studentRecruitRecyclerViewAdapter;     //每条组队信息内容适配器

    private List<RecruitInfo> list;   //组队信息实体类数组
    private List<String> userNameList;   //存放组队信息发布人用户名
    private List<String> userPortraitList;

    private View rootView;  //根视图（下拉筛选框）
    private View rootContentView;    //根视图内容
    private TextView tv_blank;

    private String[] levelOneMenu;
    private String[][] levelTwoMenu;


    //分页请求数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    private static final int PAGE_SIZE = 6;   //默认一次请求6条数据
    private static int CUR_PAGE_NUM = 1;

    //记录下拉筛选菜单选择的学校ID和学院ID
    private int selectedUniversity = 0; //默认为0
    private int selectedInstitute = -1;  //默认为-1

    //是否第一次加载
    private boolean isFirstLoading = true;


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
            rootContentView = inflater.inflate(R.layout.fragment_student_recruit_content,null);
            tv_blank = rootContentView.findViewById(R.id.tv_blank);
            init();
        }
        return rootView;
    }

    /**
     * 在fragment可见的时候，刷新数据
     */
    @Override
    public void onResume() {
        super.onResume();

        if (!isFirstLoading) {
            //如果不是第一次加载，刷新数据
            getData(getResources().getString(R.string.serverBasePath) +
                    getResources().getString(R.string.queryRecruit)
                    + "/?pageNum=1&pageSize=" + PAGE_SIZE );
        }

        isFirstLoading = false;
    }


    private void init() {

        //获取下拉目录显示数据
        getMenusData();

        initMenus();
        initListener();
        //获取列表数据
        getData(getResources().getString(R.string.serverBasePath) +
                getResources().getString(R.string.queryRecruit)
                + "/?pageNum=1&pageSize=" + PAGE_SIZE );

        //初始化浮动按钮
        initFAB();

    }


    private void getMenusData(){
        SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("data",MODE_PRIVATE);
        //第二个参数为缺省值，如果不存在该key，返回缺省值
        String strUniversity = sp.getString("university",null);
        String strInstitute = sp.getString("institute",null);

        List<String> universityList = new ArrayList<>();
        List<String> instituteList = new ArrayList<>();
        //将String转为List
        String str1[] = strUniversity.split(",");
        universityList = Arrays.asList(str1);
        String str[] = strInstitute.split(",");
        instituteList = Arrays.asList(str);

        levelOneMenu = universityList.toArray(new String[0]);
        String[] temp = instituteList.toArray(new String[0]);
        levelTwoMenu = new String [levelOneMenu.length][temp.length];

        for(int i = 0;i<levelOneMenu.length;++i){
            System.arraycopy(temp, 0, levelTwoMenu[i], 0, temp.length);
        }

    }

    private void initMenus() {

        dropDownMenu = rootView.findViewById(R.id.dropDownMenu_student_group);

        //菜单头部选项
        String[] headers = new String[]{"所属院校"};

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
                selectedUniversity = position + 1;

            }

            @Override
            public void getMenuTwo(String var1, int position) {
                selectedInstitute = position + 1;

                if(selectedUniversity <= 0 )
                    selectedUniversity = 1;
                System.out.println("university:"+selectedUniversity+"  institute:"+selectedInstitute);

                getData(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruitByUniAndIns)
                        + "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE  + "&universityId=" + selectedUniversity + "&instituteId=" + selectedInstitute);


                dropDownMenu.setTabText(var1);
                dropDownMenu.closeMenu();
            }
        });

    }

    private void getData(String requestUrl){

        ZLoadingDialog dialog = new ZLoadingDialog(getContext());
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(getResources().getColor(R.color.blue))//颜色
                .setHintText("加载中...")
                .show();

        list = new ArrayList<>();
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message){
                switch (message.what){
                    case SUCCESS:
                        Log.i("获取 ", "成功");
                        //初始化列表
                        initRecyclerView();
                        tv_blank.setVisibility(View.GONE);
                        dialog.dismiss();
                        break;

                    case FAIL:
                        Log.i("获取 ", "失败");
                        tv_blank.setText("网络好像出了点问题，请检查网络设置");
                        tv_blank.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        break;

                    case ZERO:
                        Log.i("获取", "0");
                        Toast.makeText(rootContentView.getContext(),"暂时没有新的信息！",Toast.LENGTH_SHORT).show();
                        tv_blank.setText("还没有发布组队信息，去发一条吧");
                        tv_blank.setVisibility(View.VISIBLE);
                        initRecyclerView();
                        dialog.dismiss();
                        break;
                }
            }
        };

        new Thread(()->{
            Request request = new Request.Builder()
                    .url(requestUrl)
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
                    userNameList = responseRecruit.getUserNameList();
                    userPortraitList = responseRecruit.getUserPortraitList();


                    if(list.size() == 0) { //获取的数量为0
                        msg.what = ZERO;
                    } else {
                        msg.what = SUCCESS;
                    }
                    handler.sendMessage(msg);
                    Log.i("获取", String.valueOf(list.size()));
                }
            });
        }).start();
    }

    private void initRecyclerView() {

        recyclerView = rootContentView.findViewById(R.id.rv_student_group);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentRecruitRecyclerViewAdapter = new StudentRecruitRecyclerViewAdapter(this.getContext(), list,userNameList,userPortraitList);
        recyclerView.setAdapter(studentRecruitRecyclerViewAdapter);

        recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if(selectedUniversity <= 0 && selectedInstitute <=0 ){
                    fetchRecruitInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruit) +
                            "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE );
                }else{
                    fetchRecruitInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruitByUniAndIns) +
                            "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE + "&universityId=" + selectedUniversity + "&instituteId=" + selectedInstitute);
                }

                recyclerView.refreshComplete();

            }

            @Override
            public void onLoadMore() {
                fetchRecruitInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruit) +
                        "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE );

                if(selectedUniversity <= 0 && selectedInstitute <=0 ){
                    fetchRecruitInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruit) +
                            "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE );
                }else{
                    fetchRecruitInfo(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruitByUniAndIns) +
                            "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE + "&universityId=" + selectedUniversity + "&instituteId=" + selectedInstitute);
                }
                recyclerView.setNoMore(true);
            }
        });

    }

    private void initFAB(){
        //浮动按钮，发布新的组队信息
        FloatingActionButton fabtn = rootContentView.findViewById(R.id.fabtn_student_recruit);
        fabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContentView.getContext(), RecruitReleaseActivity.class);
                startActivity(intent);
            }
        });
    }


    private void fetchRecruitInfo(String requestUrl){

        ZLoadingDialog dialog = new ZLoadingDialog(getContext());
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(getResources().getColor(R.color.blue))//颜色
                .setHintText("加载中...")
                .show();

        new Handler().postDelayed(() -> {
            @SuppressLint("HandlerLeak")
            Handler handler = new Handler(){
                @Override
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case SUCCESS:
                            Log.i("获取", "成功");
                            tv_blank.setVisibility(View.GONE);
                            studentRecruitRecyclerViewAdapter.setList(list, userNameList,userPortraitList);
                            studentRecruitRecyclerViewAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            break;
                        case FAIL:
                            Log.i("获取", "失败");
                            tv_blank.setText("获取信息失败");
                            tv_blank.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            break;
                        case ZERO:
                            Log.i("获取", "0");
                            tv_blank.setText("还没有发布组队信息，去发一条吧");
                            tv_blank.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                            break;
                    }
                    recyclerView.refreshComplete();
                }
            };

            new Thread(()->{
                CUR_PAGE_NUM = 1;
                Request request = new Request.Builder()
                        .url(requestUrl)
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
                        userNameList = responseRecruit.getUserNameList();
                        userPortraitList = responseRecruit.getUserPortraitList();
                        if( list.size() == 0 ) {
                            msg.what = ZERO;
                        } else {
                            msg.what = SUCCESS;
                        }
                        handler.sendMessage(msg);
                        Log.i("获取 ", String.valueOf(list.size()));

                    }
                });
            }).start();
        }, 1500);

    }
}