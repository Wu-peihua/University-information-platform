package com.example.uipfrontend.Student.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Activity.AddResActivity;
import com.example.uipfrontend.CommonUser.Adapter.MyReleaseResInfoAdapter;
import com.example.uipfrontend.Entity.RecruitInfo;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Activity.RecruitReleaseActivity;
import com.example.uipfrontend.Student.Adapter.StudentMyReleaseRecruitRecyclerViewAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class StudentMyReleaseRecruitFragment extends Fragment {

    View rootView;

    private XRecyclerView recyclerView;

    private List<RecruitInfo> list;   //组队信息实体类数组
    private StudentMyReleaseRecruitRecyclerViewAdapter studentRecruitRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ){
        rootView = inflater.inflate(R.layout.fragment_my_release_recruit,null);
        ButterKnife.bind(this, rootView);

        init();
        initView();
        getData();
        initRecyclerView();
        return rootView;
    }

    public void init(){

    }


    // 绑定数据到RecyclerView
    public void initView(){

    }

    private void getData(){
        list = new ArrayList<>();
//
//
//        list.add(new RecruitInfo(1,"互联网+创新创业招募队友","wx:alsdkjf","互联网找队友，人数：2，要求：经管学院，大三，积极有动力",new Date(2020,04,14),
//                "秋同学" ,"https://c-ssl.duitang.com/uploads/item/201511/21/20151121171107_zMZcy.thumb.1000_0.jpeg","",1,2));
//        list.add(new RecruitInfo(1,"互联网+创新创业招募队友","wx:alsdkjf","互联网找队友，人数：2，要求：经管学院，大三，积极有动力",new Date(2020,04,14),
//                "秋同学" ,"https://c-ssl.duitang.com/uploads/item/201511/21/20151121171107_zMZcy.thumb.1000_0.jpeg","",1,2));
//        list.add(new RecruitInfo(1,"互联网+创新创业招募队友","wx:alsdkjf","互联网找队友，人数：2，要求：经管学院，大三，积极有动力",new Date(2020,04,14),
//                "秋同学" ,"https://c-ssl.duitang.com/uploads/item/201511/21/20151121171107_zMZcy.thumb.1000_0.jpeg","",1,2));
//        list.add(new RecruitInfo(1,"互联网+创新创业招募队友","wx:alsdkjf","互联网找队友，人数：2，要求：经管学院，大三，积极有动力",new Date(2020,04,14),
//                "秋同学" ,"https://c-ssl.duitang.com/uploads/item/201511/21/20151121171107_zMZcy.thumb.1000_0.jpeg","",1,2));
    }

    public void initRecyclerView() {

        recyclerView = rootView.findViewById(R.id.xrv_studentReleaseRecruit);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        studentRecruitRecyclerViewAdapter = new StudentMyReleaseRecruitRecyclerViewAdapter(this.getContext(), list);
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



        studentRecruitRecyclerViewAdapter.setOnItemDeleteClickListener(new MyReleaseResInfoAdapter.OnItemDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(rootView.getContext())
                        .setTitle("提示")
                        .setMessage("是否确定删除该记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                studentRecruitRecyclerViewAdapter.notifyDataSetChanged();
                                Toast.makeText(rootView.getContext(), "记录删除成功", Toast.LENGTH_SHORT).show();
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
        studentRecruitRecyclerViewAdapter.setOnItemModifyClickListener(new MyReleaseResInfoAdapter.OnItemModifyClickListener() {
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



}
