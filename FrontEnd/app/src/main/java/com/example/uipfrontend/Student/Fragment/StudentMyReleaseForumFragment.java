package com.example.uipfrontend.Student.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uipfrontend.R;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.ButterKnife;

/*
    我的记录之论坛发布
 */
public class StudentMyReleaseForumFragment extends Fragment {


    XRecyclerView xrv_studentReleaseForm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState ){
        View view = inflater.inflate(R.layout.fragment_my_release_forum,null);
        ButterKnife.bind(this, view);

        xrv_studentReleaseForm = view.findViewById(R.id.xrv_studentReleaseForum);

        init();
        return view;
    }

    public void init(){

    }

}
