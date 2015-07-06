package com.ntseng.cnn_top_headlines.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by nicktseng on 15/6/24.
 */
public class SectionPagerAdapter extends FragmentStatePagerAdapter {

    private List<android.support.v4.app.Fragment> fragments;

    public SectionPagerAdapter(FragmentManager fm, List<android.support.v4.app.Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
       return fragments.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "TOP NEWS";
            case 1:
                return "FAVORITE NEWS";
            default:
                return null;
        }
    }

}

