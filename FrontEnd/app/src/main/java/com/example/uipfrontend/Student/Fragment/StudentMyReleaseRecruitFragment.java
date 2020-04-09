package com.example.uipfrontend.Student.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uipfrontend.R;
import com.example.uipfrontend.Student.Adapter.StudentMyReleaseRecruitRecyclerAdapter;
import com.example.uipfrontend.Utils.DividerItemDecoration;
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
        initData();
        initView();
        return rootView;
    }

    public void init(){

    }

    // 初始化显示的数据
    public void initData(){
        listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/

        HashMap<String, Object> map1 = new HashMap<String, Object>();
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        HashMap<String, Object> map3 = new HashMap<String, Object>();
        HashMap<String, Object> map4 = new HashMap<String, Object>();
        HashMap<String, Object> map5 = new HashMap<String, Object>();
        HashMap<String, Object> map6 = new HashMap<String, Object>();

        map1.put("ItemTitle", "美国谷歌公司已发出");
        map1.put("ItemText", "发件人:谷歌 CEO Sundar Pichai");
        listItem.add(map1);

        map2.put("ItemTitle", "国际顺丰已收入");
        map2.put("ItemText", "等待中转");
        listItem.add(map2);

        map3.put("ItemTitle", "国际顺丰转件中");
        map3.put("ItemText", "下一站中国");
        listItem.add(map3);

        map4.put("ItemTitle", "中国顺丰已收入");
        map4.put("ItemText", "下一站广州华南理工大学");
        listItem.add(map4);

        map5.put("ItemTitle", "中国顺丰派件中");
        map5.put("ItemText", "等待派件");
        listItem.add(map5);

        map6.put("ItemTitle", "华南理工大学已签收");
        map6.put("ItemText", "收件人:Carson");
        listItem.add(map6);
    }

    // 绑定数据到RecyclerView
    public void initView(){
        recyclerView = (XRecyclerView) rootView.findViewById(R.id.xrv_studentReleaseRecruit);
        //使用线性布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //用自定义分割线类设置分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        //为ListView绑定适配器
        myAdapter = new StudentMyReleaseRecruitRecyclerAdapter(getContext(),listItem);
        recyclerView.setAdapter(myAdapter);
    }

}
