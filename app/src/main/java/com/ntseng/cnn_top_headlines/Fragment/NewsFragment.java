package com.ntseng.cnn_top_headlines.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntseng.cnn_top_headlines.NewsDetailActivity;
import com.ntseng.cnn_top_headlines.R;
import com.ntseng.cnn_top_headlines.adapter.SectionPagerAdapter;
import com.ntseng.cnn_top_headlines.service.RoutineUpdateService;
import com.ntseng.cnn_top_headlines.service.XMLParserService;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends android.support.v4.app.Fragment {

    Intent startservice;
    Intent startRoutineUpdate;
    SectionPagerAdapter mSectionPagerAdapter;

    List fragments;
    TopNewsFragment topNewsFragment;
    FavoriteNewsFragment favoriteNewsFragment;
    NewsDetailFragment newsDetailFragment;
    ViewPager viewPager;


    private final static int TOP_FRAGMENT = 0;
    private final static int FAVORITE_FRAGMENT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        startservice = new Intent(getActivity(), XMLParserService.class);
        startRoutineUpdate = new Intent(getActivity(), RoutineUpdateService.class);
        getActivity().startService(startRoutineUpdate);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        viewPager = (ViewPager) getActivity().findViewById(R.id.pager);

        fragments = new ArrayList();
        topNewsFragment = new TopNewsFragment();
        favoriteNewsFragment = new FavoriteNewsFragment();
        newsDetailFragment = new NewsDetailFragment();
        fragments.add(topNewsFragment);
        fragments.add(favoriteNewsFragment);

        mSectionPagerAdapter = new SectionPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(mSectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);

        IntentFilter grabCompletion = new IntentFilter(XMLParserService.GRAB_COMPLETTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onEvent, grabCompletion);
        IntentFilter favoriteIconChange = new IntentFilter(NewsDetailActivity.FAVORITE_ICON_CHANGE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(onEvent, favoriteIconChange);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(onEvent);
    }


    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == NewsDetailActivity.FAVORITE_ICON_CHANGE){
                topNewsFragment.refresh();
            }else if(intent.getAction() == XMLParserService.GRAB_COMPLETTION){
                topNewsFragment.refresh();
            }
        }
    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            if(position == TOP_FRAGMENT){
                topNewsFragment.refresh();
            }else if(position == FAVORITE_FRAGMENT){
                favoriteNewsFragment.refresh();
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
