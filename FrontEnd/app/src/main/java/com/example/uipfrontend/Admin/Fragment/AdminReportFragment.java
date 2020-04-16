package com.example.uipfrontend.Admin.Fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.uipfrontend.R;


public class AdminReportFragment extends Fragment  implements View.OnClickListener {

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_admin_report,null);
//        return view;
//    }

    private static String  TAG = AdminReportFragment.class.getName();

    private LinearLayout lin_sub1, lin_sub2;

    private Fragment subFragment1;
    private Fragment subFragment2;

    private ImageView mTabline;
    private int mScreen1_2;
    private View rootView;
    private View rootContentView;    //根视图内容




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (null != bundle) {
            //
        }
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//       // T.showShort(getActivity(), "AdminReportFragment==onCreateView");
//        View view = inflater.inflate(R.layout.fragment_admin_report, null);
//        initView(view);
//        setLinstener();
//        initData();
//        return view;
//    }
@Nullable
@Override
public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,
                         @Nullable Bundle savedInstanceState) {
    if (rootView != null) {
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
    } else {
    // T.showShort(getActivity(), "AdminReportFragment==onCreateView");
         rootView = inflater.inflate(R.layout.fragment_admin_report, null);
    initView(rootView);
    setLinstener();
    initData();
    }
    return rootView;
}

    private void initView(View view) {

        lin_sub1 = (LinearLayout) view.findViewById(R.id.lin_sub1);
        lin_sub2 = (LinearLayout) view.findViewById(R.id.lin_sub2);
        mTabline = (ImageView) view.findViewById(R.id.imv_tabline);

    }

    protected void initData() {
        // Display display = getWindow().getWindowManager().getDefaultDisplay();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mScreen1_2 = outMetrics.widthPixels / 2;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabline.getLayoutParams();
        lp.width = mScreen1_2;
        mTabline.setLayoutParams(lp);



        //初次显示设置
        setSubFragment(0);
        setmTabline(0);

    }

    protected void setLinstener() {
        lin_sub1.setOnClickListener(this);
        lin_sub2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_sub1:
                setSubFragment(0);
                setmTabline(0);
                break;
            case R.id.lin_sub2:
                setSubFragment(1);
                setmTabline(1);
                break;
            default:
                break;
        }

    }

    public void setmTabline(int i) {

        LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
                .getLayoutParams();
        lp.leftMargin = i * mScreen1_2;
        mTabline.setLayoutParams(lp);

    }

    public void setSubFragment(int i){

        FragmentTransaction transaction =getFragmentManager().beginTransaction();

        if(0 == i ){
            subFragment1 = (subFragment1 == null ? new  SubFragment1():subFragment1);
            transaction.replace(R.id.id_content,subFragment1);
            //	transaction.addToBackStack(null);
            transaction.commit();

        }else if(1 == i ){
            subFragment2 = (subFragment2 == null ? new  SubFragment2():subFragment2);
            transaction.replace(R.id.id_content,subFragment2);
            //	transaction.addToBackStack(null);
            transaction.commit();
        }

    }



}
