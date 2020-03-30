package com.example.uipfrontend.Admin.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class SubFragment2 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // T.showShort(getActivity(), "SubFragment1==onCreateView");
        TextView tv = new TextView(getActivity());
        tv.setTextSize(25);
//        tv.setBackgroundColor(Color.parseColor("#FFA07A"));
        tv.setText("组队信息");
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
