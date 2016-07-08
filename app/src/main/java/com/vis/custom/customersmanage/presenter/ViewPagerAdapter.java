package com.vis.custom.customersmanage.presenter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/7/4.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter{

    private String[] mTitles;
    private List<Fragment> mFragments;

    public ViewPagerAdapter(FragmentManager fm,List<Fragment> mFragments,String[] mTitles) {
        super(fm);
        this.mFragments=mFragments;
        this.mTitles=mTitles;
    }


    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
