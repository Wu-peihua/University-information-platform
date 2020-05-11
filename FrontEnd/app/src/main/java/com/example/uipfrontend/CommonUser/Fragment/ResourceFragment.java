package com.example.uipfrontend.CommonUser.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.notification.ZenPolicy;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Activity.AddResActivity;
import com.example.uipfrontend.CommonUser.Adapter.ResInfoAdapter;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.Entity.ResponseRecruit;
import com.example.uipfrontend.Entity.ResponseResource;
import com.example.uipfrontend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.qlh.dropdownmenu.DropDownMenu;
import com.qlh.dropdownmenu.view.MultiMenusView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResourceFragment extends Fragment {

    private View rootView;
    private View rootContentView;

    private XRecyclerView xRecyclerView;
    private ResInfoAdapter resInfoAdapter;

    private List<ResInfo> posts;
    private List<ResInfo> whole;

    private DropDownMenu dropDownMenu;
    private ForegroundColorSpan span;
    private EditText et_search;
    private ImageView iv_delete;

    FloatingActionButton fab;

    private int subjectId = 0;
    private int typeId = 0;

    private static final int NETWORK_ERR = -2;
    private static final int SERVER_ERR = -1;
    private static final int ZERO = 0;
    private static final int SUCCESS = 1;
    private static final int FINISH = 2;
    private static final int PAGE_SIZE = 10;
    private static int CUR_PAGE_NUM;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_cu_resource, null);
            rootContentView = inflater.inflate(R.layout.fragment_cu_resource_content, null);

            initDropDownMenu();
            setSearch();
            initFab();
            initXRecyclerView();
            getData();
        }
        return rootView;
    }

    private void initDropDownMenu() {
        String[] headers = new String[]{"资源类型"};
        String[] levelOneMenu = {"全部", "哲学", "经济学", "法学", "教育学", "文学", "历史学", "理学", "工学", "农学", "医学", "军事学", "管理学", "艺术学"};
        String[][] levelTwoMenu = {
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"},
                {"全部", "论文、报告", "试题", "电子书", "视频课程", "其他"}
        };

        MultiMenusView multiMenusView = new MultiMenusView(this.getContext(), levelOneMenu, levelTwoMenu);

        List<View> popupViews = new ArrayList<>();
        popupViews.add(multiMenusView);

        dropDownMenu = rootView.findViewById(R.id.ddm_cu_resource);
        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, rootContentView.findViewById(R.id.rl_cu_resource));

        multiMenusView.setOnSelectListener(new MultiMenusView.OnSelectListener() {
            @Override
            public void getValue(String showText) {
                dropDownMenu.setTabText(showText);
                dropDownMenu.closeMenu();
            }
        });
    }

    /**
     * 描述：根据资源类型获取资源信息
     */
    private void getData() {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case NETWORK_ERR:
                        Log.i("获取资源-结果", "网络错误");
                        break;
                    case SERVER_ERR:
                        Log.i("获取资源-结果", "服务器错误");
                        break;
                    case ZERO:
                        Log.i("获取资源-结果", "空");
                        resInfoAdapter.notifyDataSetChanged();
                        break;
                    case SUCCESS:
                        Log.i("获取资源-结果", "成功");
                        Log.i("获取资源-数量", String.valueOf(posts.size()));
                        resInfoAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        new Thread(() -> {
            CUR_PAGE_NUM = 1;
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryResourceByType)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE + "&subjectId=" + subjectId + "&typeId=" + typeId)
                    .get()
                    .build();
            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("获取资源-结果", e.getMessage());
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
                        if (posts.size() != 0) {
                            posts.clear();
                            whole.clear();
                        }
                        posts.addAll(responseResource.getResInfoList());
                        whole.addAll(responseResource.getResInfoList());
                        msg.what = SUCCESS;
                    }
                    handler.sendMessage(msg);
                }
            });
        }).start();
    }

    /**
     * 描述：根据资源类型加载更多资源
     */
    private void loadMoreData() {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message) {
                xRecyclerView.loadMoreComplete();
                switch (message.what) {
                    case NETWORK_ERR:
                        Log.i("加载资源-结果", "网络错误");
                        break;
                    case SERVER_ERR:
                        Log.i("加载资源-结果", "服务器错误");
                        break;
                    case ZERO:
                        Log.i("加载资源-结果", "空");
                        Log.i("加载资源-总数", String.valueOf(posts.size()));
                        xRecyclerView.setNoMore(true);
                        break;
                    case SUCCESS:
                        Log.i("加载资源-结果", "成功");
                        Log.i("加载资源-总数", String.valueOf(posts.size()));
                        resInfoAdapter.notifyDataSetChanged();
                        break;
                    case FINISH:
                        Log.i("加载资源-结果", "加载完毕");
                        Log.i("加载资源-总数", String.valueOf(posts.size()));
                        xRecyclerView.setNoMore(true);
                        break;
                }
            }
        };

        new Thread(() -> {
            CUR_PAGE_NUM++;
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryResourceByType)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE + "&subjectId=" + subjectId + "&typeId=" + typeId)
                    .get()
                    .build();
            Message msg = new Message();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("加载资源-结果", e.getMessage());
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
                        posts.addAll(responseResource.getResInfoList());
                        whole.addAll(responseResource.getResInfoList());
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

    private void initXRecyclerView() {
        posts = new ArrayList<>();
        whole = new ArrayList<>();
        xRecyclerView = rootContentView.findViewById(R.id.rv_cu_res);

        resInfoAdapter = new ResInfoAdapter(posts, rootContentView.getContext());
        resInfoAdapter.setHasStableIds(true);
        xRecyclerView.setAdapter(resInfoAdapter);

        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootContentView.getContext()) {
            @Override
            public boolean canScrollVertically() {
                if (posts == null || posts.size() == 0)
                    return false;
                return super.canScrollVertically();
            }
        };
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(rootContentView.getContext(), R.anim.layout_animation);
        xRecyclerView.setLayoutAnimation(animationController);

        //        resInfoAdapter.setOnItemClickListener(new ResInfoAdapter.OnItemClickListener() {
        //            @Override
        //            public void onClick(int position) {
        //                Toast.makeText(rootView.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
        //            }
        //        });

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
    }

    private void setSearch() {
        span = new ForegroundColorSpan(Color.rgb(255, 0, 0));
        et_search = rootContentView.findViewById(R.id.edt_cu_res_search);
        iv_delete = rootContentView.findViewById(R.id.imgv_cu_res_delete);

        iv_delete.setOnClickListener(view -> et_search.setText(""));

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    iv_delete.setVisibility(View.GONE);
                } else {
                    iv_delete.setVisibility(View.VISIBLE);
                }

                setSearchResult(editable.toString().trim());
            }
        });
    }

    private void setSearchResult(String text) {
        posts.clear();
        if (text.equals("")) {
            posts.addAll(whole);
            resInfoAdapter.setText(null, null);
        } else {
            for (int i = 0; i < whole.size(); i++) {
                if (whole.get(i).getTitle().contains(text)) {
                    posts.add(whole.get(i));
                }
            }
            resInfoAdapter.setText(text, span);
        }
        resInfoAdapter.notifyDataSetChanged();
        xRecyclerView.scheduleLayoutAnimation();
    }

    public void initFab() {
        fab = rootContentView.findViewById(R.id.fab_cu_res);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootContentView.getContext(), AddResActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == 1) {
                getData();
                resInfoAdapter.notifyDataSetChanged();
            }
    }
}
