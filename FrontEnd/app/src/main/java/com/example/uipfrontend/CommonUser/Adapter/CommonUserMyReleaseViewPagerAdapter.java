package com.example.uipfrontend.CommonUser.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.uipfrontend.CommonUser.Fragment.MyReleasePostFragment;
import com.example.uipfrontend.CommonUser.Fragment.MyReleaseResFragment;

import java.util.ArrayList;
import java.util.List;

public class CommonUserMyReleaseViewPagerAdapter extends FragmentPagerAdapter {
    private       List<Fragment> fragmentList = null;
    private       String[]       titles;
    private final int            PAGER_COUNT  = 2;

    public CommonUserMyReleaseViewPagerAdapter(FragmentManager mFragmentManager, ArrayList<Fragment> fragmentList) {
        super(mFragmentManager);
        this.fragmentList = fragmentList;
    }

    public CommonUserMyReleaseViewPagerAdapter(FragmentManager mFragmentManager) {
        super(mFragmentManager);
    }

    public CommonUserMyReleaseViewPagerAdapter(FragmentManager mFragmentManager, String[] titles) {
        super(mFragmentManager);
        this.titles = titles;

        fragmentList = new ArrayList<>();
        fragmentList.add(0, new MyReleasePostFragment());
        fragmentList.add(1, new MyReleaseResFragment());
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
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
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > 0)
            return titles[position];
        return null;
    }
}

