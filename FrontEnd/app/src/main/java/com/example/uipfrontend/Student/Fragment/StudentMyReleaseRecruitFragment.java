package com.example.uipfrontend.Student.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Adapter.StudentMyReleaseRecruitRecyclerAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

public class StudentMyReleaseRecruitFragment extends Fragment {

    View rootView;

    private XRecyclerView recyclerView;

    private ArrayList<HashMap<String,Object>> listItem;
    private StudentMyReleaseRecruitRecyclerAdapter myAdapter;

    XRecyclerView xrv_studentReleaseRecruit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ){
        rootView = inflater.inflate(R.layout.fragment_my_release_recruit,null);
        ButterKnife.bind(this, rootView);

        xrv_studentReleaseRecruit = rootView.findViewById(R.id.xrv_studentReleaseRecruit);

        init();
        initView();
        return rootView;
    }

    public void init(){

    }


    // 绑定数据到RecyclerView
    public void initView(){


        recyclerView = (XRecyclerView) rootView.findViewById(R.id.xrv_studentReleaseRecruit);
        //使用线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //为ListView绑定适配器
        myAdapter = new StudentMyReleaseRecruitRecyclerAdapter(this.getContext(),listItem);
        recyclerView.setAdapter(myAdapter);


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
