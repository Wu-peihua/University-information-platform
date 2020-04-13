package com.example.uipfrontend.CommonUser.Fragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uipfrontend.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.ButterKnife;

public class MyReleaseForumFragment extends Fragment {

    XRecyclerView xRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ){
        View view = inflater.inflate(R.layout.fragment_my_release_forum,null);
        ButterKnife.bind(this, view);

        xRecyclerView = view.findViewById(R.id.xrv_mr_forum);

        init();
        return view;
    }

    public void init(){

    }
}