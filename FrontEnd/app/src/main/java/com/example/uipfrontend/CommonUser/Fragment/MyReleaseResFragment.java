package com.example.uipfrontend.CommonUser.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.uipfrontend.CommonUser.Activity.AddResActivity;
import com.example.uipfrontend.CommonUser.Adapter.MyReleaseResInfoAdapter;
import com.example.uipfrontend.CommonUser.Adapter.ResInfoAdapter;
import com.example.uipfrontend.Entity.ResInfo;
import com.example.uipfrontend.R;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class MyReleaseResFragment extends Fragment {
    private View                    rootView;
    private XRecyclerView           xRecyclerView;
    private MyReleaseResInfoAdapter adapter;
    private List<ResInfo>           list;
    private int                     republishPos;

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
            initData();
            initXRecyclerView();
        }
        return rootView;
    }

    public void initData() {
        list = new ArrayList<>();
        list.add(new ResInfo(0, 0, "http://5b0988e595225.cdn.sohucs.com/images/20181204/bb053972948e4279b6a5c0eae3dc167e.jpeg",
                "张咩阿", "支付宝破解版", "清明前后，种瓜种豆。当然，那都是为漫长夏日的怎样过活做准备的，儿时生在乡下的人，" +
                "这些话多半还是有些耳熟的。一般的话，清明过后是一日热胜一日，偶尔会有个不应时的桃花暮春雪，也是个稀少的意外，大多这个时候，" +
                "植物随着气候变化也即将转换着面目，即便是在城里，也能掐算着哪个时候能见上什么。踏春的时尚，之外哪能少的了吃货们的小算计呢。",
                "www.baidu.com", "2020-02-02 00:00", 0, false));
    }

    public void initXRecyclerView() {
        xRecyclerView = rootView.findViewById(R.id.xrv_mr_res);
        adapter = new MyReleaseResInfoAdapter(list, rootView.getContext());
        xRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext()) {
            @Override
            public boolean canScrollVertically() {
                if (list == null || list.size() == 0)
                    return false;
                return super.canScrollVertically();
            }
        };
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.getDefaultRefreshHeaderView().setRefreshTimeVisible(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);

        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                xRecyclerView.setNoMore(true);
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
                                list.remove(position);
                                adapter.notifyDataSetChanged();
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
        adapter.setOnItemModifyClickListener(new MyReleaseResInfoAdapter.OnItemModifyClickListener() {
            @Override
            public void onModifyClick(int position) {
                AlertDialog dialog = new AlertDialog.Builder(rootView.getContext())
                        .setTitle("提示")
                        .setMessage("修改后该资源将重新发布，是否确定修改？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                republishPos = position;
                                Intent intent = new Intent(rootView.getContext(), AddResActivity.class);
                                intent.putExtra("resInfo", list.get(position));
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
            if (resultCode == 1) {
                list.remove(republishPos);
                ResInfo receiveData = (ResInfo) data.getSerializableExtra("resInfo");
                list.add(0, receiveData);
                adapter.notifyDataSetChanged();
            }
    }
}