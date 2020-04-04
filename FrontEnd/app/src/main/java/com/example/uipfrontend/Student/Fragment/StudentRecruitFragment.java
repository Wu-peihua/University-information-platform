package com.example.uipfrontend.Student.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.AddResActivity;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Adapter.StudentRecruitRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.qlh.dropdownmenu.view.MultiMenusView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.net.wifi.WifiConfiguration.Status.strings;


public class StudentRecruitFragment extends Fragment {

    private String[] headers;//菜单头部选项
    private List<View> popupViews = new ArrayList<>();//菜单列表视图
    private DropDownMenu dropDownMenu;
    private MultiMenusView multiMenusView;//多级菜单
    private XRecyclerView recyclerView;  //下拉刷新上拉加载
    private StudentRecruitRecyclerViewAdapter studentRecruitRecyclerViewAdapter;     //每条组队信息内容适配器

    private List<String> list;   //组队信息实体类数组
    private View rootView;

    private com.jayfang.dropdownmenu.DropDownMenu mMenu;


    //测试下拉筛选控件选择值
    final String[] arr1=new String[]{"全部", "华南师范大学", "华南理工大学", "中山大学"};
    final String[] arr2=new String[]{"哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学","艺术学"};
    final String[] arr3=new String[]{"全部年龄","10","20","30","40","50","60","70"};

    final String[] strings=new String[]{"所属院校","所属专业","选择年龄"};




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
            recyclerView = rootView.findViewById(R.id.rv_student_group);

            init();
        }
        return rootView;
    }

    private void init() {
//        initMenus();
        initMenus2();
//        initListener();
        getData();
        initRecyclerView();
        initToolBar();

    }

    private void initToolBar(){

    }

    private void initMenus() {

        dropDownMenu = rootView.findViewById(R.id.dropDownMenu_student_group);

        headers = new String[]{"所属院校"};
        //初始化多级菜单
        final String[] levelOneMenu = {"全部", "华南师范大学", "华南理工大学", "中山大学"};
        final String[][] levelTwoMenu = {
                {"哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学","艺术学"},
                {"哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学","艺术学"},
                {"哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学","艺术学"},
                {"哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学","艺术学"}
        };
        multiMenusView = new MultiMenusView(this.getContext(),levelOneMenu,levelTwoMenu);
        popupViews.add(multiMenusView);
        //初始化内容视图
        View contentView = LayoutInflater.from(this.getContext()).inflate(R.layout.fragment_student_recruit,null);
        //装载
        dropDownMenu.setDropDownMenu(Arrays.asList(headers),popupViews,contentView);

    }

    private void initMenus2(){
        mMenu=(com.jayfang.dropdownmenu.DropDownMenu) rootView.findViewById(R.id.dropDownMenu_student_group);
        mMenu.setmMenuCount(3);//Menu的个数
        mMenu.setmShowCount(6);//Menu展开list数量太多时只显示的个数
        mMenu.setShowCheck(true);//是否显示展开list的选中项
        mMenu.setmMenuTitleTextSize(12);//Menu的文字大小
        mMenu.setmMenuTitleTextColor(Color.BLACK);//Menu的文字颜色
        mMenu.setmMenuListTextSize(12);//Menu展开list的文字大小
        mMenu.setmMenuListTextColor(Color.BLACK);//Menu展开list的文字颜色
        mMenu.setmMenuBackColor(getResources().getColor(R.color.lightGray));//Menu的背景颜色
        mMenu.setmMenuPressedBackColor(Color.WHITE);//Menu按下的背景颜色
        mMenu.setmCheckIcon(R.drawable.ico_make);//Menu展开list的勾选图片
        mMenu.setmUpArrow(R.drawable.arrow_up);//Menu默认状态的箭头
        mMenu.setmDownArrow(R.drawable.arrow_down);//Menu按下状态的箭头
        mMenu.setDefaultMenuTitle(strings);//默认未选择任何过滤的Menu title
        mMenu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {


            }
        });

        List<String[]> items = new ArrayList<>();
        items.add(arr1);
        items.add(arr2);
        items.add(arr3);
        mMenu.setmMenuItems(items);

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

}