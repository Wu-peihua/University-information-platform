package com.example.uipfrontend.Student.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.uipfrontend.Student.Fragment.StudentCommentFragment;
import com.example.uipfrontend.Student.Fragment.StudentForumFragment;
import com.example.uipfrontend.Student.Fragment.StudentGroupFragment;
import com.example.uipfrontend.Student.Fragment.StudentHomeFragment;
import com.example.uipfrontend.Student.Fragment.StudentResourceFragment;

import java.util.ArrayList;
import java.util.List;

public class StudentFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = null;

    private String[] titles;

    public StudentFragmentAdapter(FragmentManager fm) {
        super(fm);

        fragmentList = new ArrayList<>();
        fragmentList.add(0, new StudentForumFragment());
        fragmentList.add(1, new StudentResourceFragment());
        fragmentList.add(2, new StudentGroupFragment());
        fragmentList.add(3, new StudentCommentFragment());
        fragmentList.add(4, new StudentHomeFragment());

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
