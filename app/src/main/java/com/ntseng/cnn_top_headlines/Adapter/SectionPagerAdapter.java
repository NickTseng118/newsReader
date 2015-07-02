package com.ntseng.cnn_top_headlines.Adapter;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ntseng.cnn_top_headlines.Fragment.FavoriteNewsFragment;
import com.ntseng.cnn_top_headlines.Fragment.TopNewsFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

