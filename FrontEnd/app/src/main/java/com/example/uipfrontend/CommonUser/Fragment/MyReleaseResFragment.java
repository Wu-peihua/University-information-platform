package com.example.uipfrontend.CommonUser.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Activity.AddResActivity;
import com.example.uipfrontend.CommonUser.Adapter.MyReleaseResInfoAdapter;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.Entity.ResponseResource;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.Entity.UserRecord;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Utils.UserOperationRecord;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyReleaseResFragment extends Fragment {
    private View rootView;
    private XRecyclerView xRecyclerView;
    private MyReleaseResInfoAdapter adapter;
    private List<ResInfo> resInfoList;
    private int deletePos;
    private UserInfo user;
    private Long userId;

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;
    private static final int FINISH = 2;
    private static final int PAGE_SIZE = 10;
    private static int CUR_PAGE_NUM;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_my_release_res, null);
            resInfoList = new ArrayList<>();
            user = (UserInfo) getActivity().getApplication();
            userId = user.getUserId();
            getData();
        }
        return rootView;
    }

    /**
     * 描述：根据用户id获取资源发布记录
     */
    private void getData() {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message) {
                initXRecyclerView();
                switch (message.what) {
                    case NETWORK_ERR:
                        Log.i("获取资源发布记录-结果", "网络错误");
                        Toast.makeText(getActivity(), "网络错误，请检查网络后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SERVER_ERR:
                        Log.i("获取资源发布记录-结果", "服务器错误");
                        Toast.makeText(getActivity(), "服务器错误，请稍后再试", Toast.LENGTH_SHORT).show();

                        break;
                    case ZERO:
                        Log.i("获取资源发布记录-结果", "空");
                        xRecyclerView.scheduleLayoutAnimation();
                        adapter.notifyDataSetChanged();
                        break;
                    case SUCCESS:
                        Log.i("获取资源发布记录-结果", "成功");
                        Log.i("获取资源发布记录-数量", String.valueOf(resInfoList.size()));
                        xRecyclerView.scheduleLayoutAnimation();
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        new Thread(() -> {
            CUR_PAGE_NUM = 1;
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryResourceByUserId)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE + "&userId=" + userId)
                    .get()
                    .build();
            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("获取资源发布记录-结果 ", e.getMessage());
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseResource responseResource = new Gson().fromJson(response.body().string(),
                            ResponseResource.class);

                    if (responseResource.getResInfoList() == null)
                        msg.what = SERVER_ERR;
                    else {
                        if (resInfoList.size() != 0)
                            resInfoList.clear();
                        if (responseResource.getResInfoList().size() == 0)
                            msg.what = ZERO;
                        else {
                            resInfoList.addAll(responseResource.getResInfoList());
                            msg.what = SUCCESS;
                        }
                    }
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }

    /**
     * 描述：根据用户id加载资源发布记录
     */
    private void loadMoreData() {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message) {
                xRecyclerView.loadMoreComplete();
                switch (message.what) {
                    case NETWORK_ERR:
                        Log.i("加载资源发布记录-结果", "网络错误");
                        Toast.makeText(getActivity(), "网络错误，请检查网络后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SERVER_ERR:
                        Log.i("加载资源发布记录-结果", "服务器错误");
                        Toast.makeText(getActivity(), "服务器错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case ZERO:
                        Log.i("加载资源发布记录-结果", "空");
                        Log.i("加载资源发布记录-总数", String.valueOf(resInfoList.size()));
                        xRecyclerView.setNoMore(true);
                        break;
                    case SUCCESS:
                        Log.i("加载资源发布记录-结果", "成功");
                        Log.i("加载资源发布记录-总数", String.valueOf(resInfoList.size()));
                        adapter.notifyDataSetChanged();
                        break;
                    case FINISH:
                        Log.i("加载资源发布记录-结果", "加载完毕");
                        Log.i("加载资源发布记录-总数", String.valueOf(resInfoList.size()));
                        xRecyclerView.setNoMore(true);
                        break;
                }
            }
        };

        new Thread(() -> {
            CUR_PAGE_NUM++;
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryResourceByUserId)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE + "&userId=" + userId)
                    .get()
                    .build();
            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("加载资源发布记录-结果", e.getMessage());
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseResource responseResource = new Gson().fromJson(response.body().string(),
                            ResponseResource.class);

                    if (responseResource.getResInfoList() == null)
                        msg.what = SERVER_ERR;
                    else if (responseResource.getResInfoList().size() == 0)
                        msg.what = ZERO;
                    else {
                        resInfoList.addAll(responseResource.getResInfoList());
                        if (CUR_PAGE_NUM * PAGE_SIZE < responseResource.getTotal())
                            msg.what = SUCCESS;
                        else
                            msg.what = FINISH;
                    }
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }

    /**
     * 描述：根据主键id删除资源发布记录
     */
    private void deleteData() {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case NETWORK_ERR:
                        Log.i("删除资源发布记录-结果", "网络错误");
                        Toast.makeText(getActivity(), "网络错误，请检查网络后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SERVER_ERR:
                        Log.i("删除资源发布记录-结果", "服务器错误");
                        Toast.makeText(getActivity(), "服务器错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        Log.i("删除资源发布记录-结果", "成功");
                        getData();
                        break;
                }
            }
        };
        new Thread(() -> {
            Message msg = new Message();
            OkHttpClient client = new OkHttpClient();

            FormBody.Builder builder = new FormBody.Builder();
            builder.add("infoId", String.valueOf(resInfoList.get(deletePos).getInfoId()));
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath)
                            + getResources().getString(R.string.deleteResourceByInfoId))
                    .post(requestBody)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("删除资源发布记录-结果", e.getMessage());
                    msg.what = NETWORK_ERR;
                    handler.sendMessage(msg);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String resStr = response.body().string();
                    Log.i("删除资源发布记录-服务器返回信息", resStr);

                    JsonObject jsonObject = new JsonParser().parse(resStr).getAsJsonObject();
                    JsonElement element = jsonObject.get("success");

                    boolean res = new Gson().fromJson(element, boolean.class);
                    msg.what = res ? SUCCESS : SERVER_ERR;
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }

    public void initXRecyclerView() {
        xRecyclerView = rootView.findViewById(R.id.xrv_mr_res);
        adapter = new MyReleaseResInfoAdapter(resInfoList, rootView.getContext());
        adapter.setHasStableIds(true);
        xRecyclerView.setAdapter(adapter);
        xRecyclerView.setNoMore(false);

        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext()) {
            @Override
            public boolean canScrollVertically() {
                if (resInfoList == null || resInfoList.size() == 0)
                    return false;
                return super.canScrollVertically();
            }
        };
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(rootView.getContext(), R.anim.layout_animation);
        xRecyclerView.setLayoutAnimation(animationController);

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(() -> {
                    getData();
                    xRecyclerView.refreshComplete();
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(() -> {
                    loadMoreData();
                }, 1000);
            }
        });

        adapter.setOnItemDeleteClickListener(new MyReleaseResInfoAdapter.OnItemDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(rootView.getContext())
                        .setTitle("提示")
                        .setMessage("是否确定删除该记录？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePos = position;
                                deleteData();
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
        adapter.setOnItemModifyClickListener(new MyReleaseResInfoAdapter.OnItemModifyClickListener() {
            @Override
            public void onModifyClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(rootView.getContext())
                        .setTitle("提示")
                        .setMessage("修改后该资源将重新发布，是否确定修改？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePos = position;
                                Intent intent = new Intent(rootView.getContext(), AddResActivity.class);
                                intent.putExtra("resInfo", resInfoList.get(position));
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
        adapter.setOnItemLikeClickListener((isSelected, position) -> {
            if (isSelected) {
                resInfoList.get(position).setLikeNumber(resInfoList.get(position).getLikeNumber() + 1);
                UserRecord record = new UserRecord();
                record.setUserId(user.getUserId());
                record.setToId(resInfoList.get(position).getInfoId());
                record.setTag(1);
                record.setType(6);
                UserOperationRecord.insertRecord(getContext(), record, user);
            } else {
                resInfoList.get(position).setLikeNumber(resInfoList.get(position).getLikeNumber() - 1);
                String key = "resource" + resInfoList.get(position).getInfoId();
                Long infoId = user.getLikeRecord().get(key);
                UserOperationRecord.deleteRecord(getContext(), infoId);
                user.getLikeRecord().remove(key);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == 1) {
                xRecyclerView.scheduleLayoutAnimation();
                deleteData();
            }
    }
}