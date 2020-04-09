package com.example.uipfrontend.Student.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.uipfrontend.Student.Fragment.StudentForumFragment;
import com.example.uipfrontend.Student.Fragment.StudentMyReleaseCourseFragment;
import com.example.uipfrontend.Student.Fragment.StudentMyReleaseForumFragment;
import com.example.uipfrontend.Student.Fragment.StudentMyReleaseResFragment;

import java.util.ArrayList;
import java.util.List;

public class StudentMyReleaseViewPagerAdapter extends FragmentPagerAdapter {

    /**
     * The m fragment list.
     */
    private List<Fragment> mFragmentList = null;

    private String[] titles;

    private final int PAGER_COUNT = 4;

    public StudentMyReleaseViewPagerAdapter(FragmentManager mFragmentManager, ArrayList<Fragment> fragmentList) {
        super(mFragmentManager);
        mFragmentList = fragmentList;
    }

    public StudentMyReleaseViewPagerAdapter(FragmentManager mFragmentManager) {
        super(mFragmentManager);
    }

    /**
     * titles是给TabLayout设置title用的
     *
     * @param mFragmentManager
     * @param titles
     */
    public StudentMyReleaseViewPagerAdapter(FragmentManager mFragmentManager, String[] titles) {
        super(mFragmentManager);
        this.titles = titles;
    }
    /**
     * 描述：获取数量.
     *
     * @return the count
     */
    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    /**
     * 描述：获取索引位置的Fragment.
     *
     * @param position the position
     * @return the item
     */
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new StudentMyReleaseForumFragment();
                break;
            case 1:
                fragment = new StudentMyReleaseResFragment();
                break;
            case 2:
                fragment = new StudentMyReleaseResFragment();
                break;
            case 3:
                fragment = new StudentMyReleaseCourseFragment();
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
