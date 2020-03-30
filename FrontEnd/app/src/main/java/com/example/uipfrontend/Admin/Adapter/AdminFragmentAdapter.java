package com.example.uipfrontend.Admin.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.uipfrontend.Admin.Fragment.AdminCertificationFragment;
import com.example.uipfrontend.Admin.Fragment.AdminCourseFragment;
import com.example.uipfrontend.Admin.Fragment.AdminHomeFragment;
import com.example.uipfrontend.Admin.Fragment.AdminReportFragment;

import java.util.ArrayList;
import java.util.List;

public class AdminFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList ;

    public AdminFragmentAdapter(FragmentManager fm) {
        super(fm);

        fragmentList = new ArrayList<>();
        fragmentList.add(0, new AdminCertificationFragment());
        fragmentList.add(1, new AdminReportFragment());
        fragmentList.add(2, new AdminCourseFragment());
        fragmentList.add(3, new AdminHomeFragment());
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position < fragmentList.size()) {
            fragment = fragmentList.get(position);
        } else {
            fragment = fragmentList.get(0);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
