package com.example.uipfrontend.CommonUser.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.uipfrontend.CommonUser.Fragment.CommonUserHomeFragment;
import com.example.uipfrontend.CommonUser.Fragment.ForumFragment;
import com.example.uipfrontend.CommonUser.Fragment.ResourceFragment;

import java.util.ArrayList;
import java.util.List;

public class CommonUserFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public CommonUserFragmentAdapter(FragmentManager fm){
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentList.add(0, new ForumFragment());
        fragmentList.add(1, new ResourceFragment());
        fragmentList.add(2, new CommonUserHomeFragment());
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
