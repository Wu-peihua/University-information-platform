package com.example.uipfrontend.CommonUser.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uipfrontend.CommonUser.Activity.AddResActivity;
import com.example.uipfrontend.CommonUser.Adapter.ResInfoAdapter;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.Entity.ResponseResource;
import com.example.uipfrontend.Entity.UserInfo;
import com.example.uipfrontend.Entity.UserRecord;
import com.example.uipfrontend.R;
import com.example.uipfrontend.Utils.UserOperationRecord;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResourceFragment extends Fragment {
    private View rootView;
    private View rootContentView;

    private List<ResInfo> resInfoList;
    private UserInfo user;

    private XRecyclerView xRecyclerView;
    private ResInfoAdapter resInfoAdapter;
    private DropDownMenu dropDownMenu;
    private ForegroundColorSpan span;
    private EditText et_search;
    private ImageView iv_delete;
    private String keyword = "";
    FloatingActionButton fab;

    private String subject = "全部";
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

            user = (UserInfo) getActivity().getApplication();
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
            public void getMenuOne(String var1, int position) {
                subject = var1;
                subjectId = position;
            }

            @Override
            public void getMenuTwo(String var1, int position) {
                typeId = position;
                getData();
                dropDownMenu.setTabText(subject + "" + var1);
                dropDownMenu.closeMenu();
            }
        });
    }

    /**
     * 描述：根据资源类型和关键字获取资源信息
     */
    private void getData() {
        xRecyclerView.setNoMore(false);
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case NETWORK_ERR:
                        Log.i("获取资源-结果", "网络错误");
                        Toast.makeText(getActivity(), "网络错误，请检查网络后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SERVER_ERR:
                        Log.i("获取资源-结果", "服务器错误");
                        Toast.makeText(getActivity(), "服务器错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case ZERO:
                        Log.i("获取资源-结果", "空");
                        xRecyclerView.scheduleLayoutAnimation();
                        resInfoAdapter.notifyDataSetChanged();
                        break;
                    case SUCCESS:
                        Log.i("获取资源-结果", "成功");
                        Log.i("获取资源-数量", String.valueOf(resInfoList.size()));
                        xRecyclerView.scheduleLayoutAnimation();
                        resInfoAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        new Thread(() -> {
            CUR_PAGE_NUM = 1;
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryResourceByTypeAndKeyword)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE + "&subjectId=" + subjectId + "&typeId=" + typeId + "&keyword=" + keyword)
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
     * 描述：根据资源类型和关键字加载更多资源
     */
    private void loadMoreData() {
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler() {
            public void handleMessage(Message message) {
                xRecyclerView.loadMoreComplete();
                switch (message.what) {
                    case NETWORK_ERR:
                        Log.i("加载资源-结果", "网络错误");
                        Toast.makeText(getActivity(), "网络错误，请检查网络后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case SERVER_ERR:
                        Log.i("加载资源-结果", "服务器错误");
                        Toast.makeText(getActivity(), "服务器错误，请稍后再试", Toast.LENGTH_SHORT).show();
                        break;
                    case ZERO:
                        Log.i("加载资源-结果", "空");
                        Log.i("加载资源-总数", String.valueOf(resInfoList.size()));
                        xRecyclerView.setNoMore(true);
                        break;
                    case SUCCESS:
                        Log.i("加载资源-结果", "成功");
                        Log.i("加载资源-总数", String.valueOf(resInfoList.size()));
                        resInfoAdapter.notifyDataSetChanged();
                        break;
                    case FINISH:
                        Log.i("加载资源-结果", "加载完毕");
                        Log.i("加载资源-总数", String.valueOf(resInfoList.size()));
                        xRecyclerView.setNoMore(true);
                        break;
                }
            }
        };

        new Thread(() -> {
            CUR_PAGE_NUM++;
            Request request = new Request.Builder()
                    .url(getResources().getString(R.string.serverBasePath) +
                            getResources().getString(R.string.queryResourceByTypeAndKeyword)
                            + "/?pageNum=" + CUR_PAGE_NUM + "&pageSize=" + PAGE_SIZE + "&subjectId=" + subjectId + "&typeId=" + typeId + "&keyword=" + keyword)
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

    private void initXRecyclerView() {
        resInfoList = new ArrayList<>();
        xRecyclerView = rootContentView.findViewById(R.id.rv_cu_res);

        resInfoAdapter = new ResInfoAdapter(resInfoList, rootContentView.getContext());
        resInfoAdapter.setHasStableIds(true);
        xRecyclerView.setAdapter(resInfoAdapter);

        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootContentView.getContext()) {
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

        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(rootContentView.getContext(), R.anim.layout_animation);
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

        resInfoAdapter.setOnItemLikeClickListener((isSelected, position) -> {
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
        resInfoAdapter.setOnItemReportClickListener((view, position) -> {
            String key = "resource" + resInfoList.get(position).getInfoId();
            if (user.getReportRecord().containsKey(key)) {
                Toast.makeText(getActivity(), "已经举报，感谢您的反馈", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage("是否确定举报该资源")
                        .setPositiveButton("确定", (dialog1, which) -> {
                            resInfoList.get(position).setReportNumber(resInfoList.get(position).getReportNumber() + 1);

                            UserRecord record = new UserRecord();
                            record.setUserId(user.getUserId());
                            record.setToId(resInfoList.get(position).getInfoId());
                            record.setTag(2);
                            record.setType(6);
                            UserOperationRecord.insertRecord(getContext(), record, user);

                            ((ImageView) view).setColorFilter(getContext().getResources().getColor(R.color.blue));
                            resInfoAdapter.notifyDataSetChanged();

                            Toast.makeText(getContext(), "举报成功，感谢您的反馈", Toast.LENGTH_SHORT).show();
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

                keyword = editable.toString().trim();
                getData();
                if (keyword.equals(""))
                    resInfoAdapter.setText(null, null);
                else
                    resInfoAdapter.setText(keyword, span);
                resInfoAdapter.notifyDataSetChanged();

            }
        });
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
            if (resultCode == 1)
                getData();
    }
}
