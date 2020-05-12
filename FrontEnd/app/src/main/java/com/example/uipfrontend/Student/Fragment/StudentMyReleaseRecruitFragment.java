package com.example.uipfrontend.Student.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Activity.AddResActivity;
import com.example.uipfrontend.CommonUser.Adapter.MyReleaseResInfoAdapter;
import com.example.uipfrontend.Entity.RecruitInfo;
import com.example.uipfrontend.Entity.ResponseRecruit;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Activity.RecruitReleaseActivity;
import com.example.uipfrontend.Student.Adapter.StudentMyReleaseRecruitRecyclerViewAdapter;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class StudentMyReleaseRecruitFragment extends Fragment {

    private View rootView;
    private TextView tv_blank;

    //分页请求数据
    private static final int SUCCESS = 1;
    private static final int FAIL = -1;
    private static final int ZERO = 0; //记录请求回来的数据条数是否为零
    private static final int PAGE_SIZE = 6;   //默认一次请求6条数据
    private static int CUR_PAGE_NUM = 1;

    private XRecyclerView recyclerView;

    private List<RecruitInfo> list;   //组队信息实体类数组
    private List<String> userNameList;   //存放组队信息发布人用户名
    private List<String> userPortraitList;

    private StudentMyReleaseRecruitRecyclerViewAdapter studentMyReleaseRecruitRecyclerViewAdapter;

    private Long userId; //当前登陆用户id

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ){
        rootView = inflater.inflate(R.layout.fragment_my_release_recruit,null);
        ButterKnife.bind(this, rootView);

        tv_blank = rootView.findViewById(R.id.tv_blank);

        init();
        //初始化列表
        getData(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruitByUserId) +
                "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE + "&userId=" + userId );
        initRecyclerView();
        return rootView;
    }

    public void init(){
        UserInfo user = (UserInfo) Objects.requireNonNull(getActivity()).getApplication();
        userId = user.getUserId();
    }


    // 绑定数据到RecyclerView
    private void initView(){

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
                        tv_blank.setVisibility(View.GONE);
                        //初始化列表
                        initRecyclerView();
                        dialog.dismiss();
                        break;

                    case FAIL:
                        Log.i("获取 ", "失败");
                        tv_blank.setText("获取信息失败");
                        tv_blank.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        break;

                    case ZERO:
                        Log.i("获取", "0");
                        Toast.makeText(rootView.getContext(),"暂时没有新的信息！",Toast.LENGTH_SHORT).show();
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



    public void initRecyclerView() {

        recyclerView = rootView.findViewById(R.id.xrv_studentReleaseRecruit);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentMyReleaseRecruitRecyclerViewAdapter = new StudentMyReleaseRecruitRecyclerViewAdapter(this.getContext(), list,userNameList,userPortraitList);
        recyclerView.setAdapter(studentMyReleaseRecruitRecyclerViewAdapter);

        recyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                fetchRecruitRecord(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruitByUserId) +
                        "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE + "&userId=" + userId );
                recyclerView.refreshComplete();

            }

            @Override
            public void onLoadMore() {
                fetchRecruitRecord(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruitByUserId) +
                        "/?pageNum="+ CUR_PAGE_NUM +"&pageSize="+ PAGE_SIZE + "&userId=" + userId );
                recyclerView.setNoMore(true);
            }
        });



        studentMyReleaseRecruitRecyclerViewAdapter.setOnItemDeleteClickListener(new MyReleaseResInfoAdapter.OnItemDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(rootView.getContext())
                        .setTitle("提示")
                        .setMessage("是否确定删除该记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                System.out.println("position:"+position);
                                deleteRecruit(position,list.get(position).getInfoId());
                            }
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
            }
        });
        studentMyReleaseRecruitRecyclerViewAdapter.setOnItemModifyClickListener(new MyReleaseResInfoAdapter.OnItemModifyClickListener() {
            @Override
            public void onModifyClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(rootView.getContext())
                        .setTitle("提示")
                        .setMessage("修改后该组队信息将重新发布，是否确定修改？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(rootView.getContext(), RecruitReleaseActivity.class);
                                intent.putExtra("recruitInfo", list.get(position));
                                startActivityForResult(intent, 1);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .setCancelable(false)
                        .create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.blue));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.blue));
            }
        });

    }

    private void fetchRecruitRecord(String requestUrl){
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
                        Log.i("刷新", "成功");
                        studentMyReleaseRecruitRecyclerViewAdapter.setList(list,userNameList,userPortraitList);
                        studentMyReleaseRecruitRecyclerViewAdapter.notifyDataSetChanged();
                        tv_blank.setVisibility(View.GONE);
                        dialog.dismiss();
                        break;
                    case FAIL:
                        Log.i("刷新", "失败");
                        tv_blank.setText("获取信息失败");
                        tv_blank.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        break;
                    case ZERO:
                        Log.i("刷新", "0");
                        studentMyReleaseRecruitRecyclerViewAdapter.setList(list,userNameList,userPortraitList);
                        studentMyReleaseRecruitRecyclerViewAdapter.notifyDataSetChanged();
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

    }

    //删除组队发布记录
    private void deleteRecruit(int pos,Long infoId){
        ZLoadingDialog dialog = new ZLoadingDialog(getContext());
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(getResources().getColor(R.color.blue))//颜色
                .setHintText("加载中...")
                .show();

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FAIL:
                        Toast.makeText(rootView.getContext(), "记录删除失败", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        Toast.makeText(rootView.getContext(), "记录删除成功", Toast.LENGTH_SHORT).show();
                        getData(getResources().getString(R.string.serverBasePath) + getResources().getString(R.string.queryRecruitByUserId) +
                                "/?pageNum="+ 1 +"&pageSize="+ PAGE_SIZE + "&userId=" + userId );
                        studentMyReleaseRecruitRecyclerViewAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            Message msg = new Message();
            @Override
            public void run() {
                //建立client
                final OkHttpClient[] client = {new OkHttpClient()};

                FormBody.Builder builder = new FormBody.Builder();
                RequestBody requestBody = builder.build();

                //请求
                Request request=new Request.Builder()
                        .url(getResources().getString(R.string.serverBasePath)+getResources().getString(R.string.deleteRecruit) + "/?infoId="+ infoId)
                        .post(requestBody)
                        .build();
                //新建call联结client和request
                Call call= client[0].newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //请求失败的处理
                        Log.i("RESPONSE:","fail"+e.getMessage());
                        msg.what = FAIL;
                        dialog.dismiss();
                        handler.sendMessage(msg);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("RESPONSE:",response.body().string());
                        msg.what = SUCCESS;
                        dialog.dismiss();
                        handler.sendMessage(msg);

                    }

                });
            }
        }).start();

    }





}
