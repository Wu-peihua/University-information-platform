package com.example.uipfrontend.Student.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.uipfrontend.CommonUser.Fragment.ForumFragment;
import com.example.uipfrontend.CommonUser.Fragment.ResourceFragment;
import com.example.uipfrontend.Student.Fragment.StudentCommentFragment;
import com.example.uipfrontend.Student.Fragment.StudentForumFragment;
import com.example.uipfrontend.Student.Fragment.StudentHomeFragment;
import com.example.uipfrontend.Student.Fragment.StudentMyReleaseCourseFragment;
import com.example.uipfrontend.Student.Fragment.StudentMyReleaseForumFragment;
import com.example.uipfrontend.Student.Fragment.StudentMyReleaseRecruitFragment;
import com.example.uipfrontend.Student.Fragment.StudentMyReleaseResFragment;
import com.example.uipfrontend.Student.Fragment.StudentRecruitFragment;

import java.util.ArrayList;
import java.util.List;

public class StudentMyReleaseViewPagerAdapter extends FragmentPagerAdapter {

    /**
     * The m fragment list.
     */
    private List<Fragment> fragmentList = null;

    private String[] titles;

    private final int PAGER_COUNT = 4;

//    public StudentMyReleaseViewPagerAdapter(FragmentManager mFragmentManager, ArrayList<Fragment> fragmentList) {
//        super(mFragmentManager);
//        mFragmentList = fragmentList;
//    }
//
//    public StudentMyReleaseViewPagerAdapter(FragmentManager mFragmentManager) {
//        super(mFragmentManager);
//    }

    /**
     * titles是给TabLayout设置title用的
     *
     * @param mFragmentManager
     * @param titles
     */
    public StudentMyReleaseViewPagerAdapter(FragmentManager mFragmentManager, String[] titles) {
        super(mFragmentManager);
        this.titles = titles;

        fragmentList = new ArrayList<>();
        fragmentList.add(0, new StudentMyReleaseForumFragment());
        fragmentList.add(1, new StudentMyReleaseResFragment());
        fragmentList.add(2, new StudentMyReleaseRecruitFragment());
        fragmentList.add(3, new StudentMyReleaseCourseFragment());

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