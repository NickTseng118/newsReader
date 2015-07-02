package com.ntseng.cnn_top_headlines;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.ActiveAndroid;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.ntseng.cnn_top_headlines.Adapter.SectionPagerAdapter;
import com.ntseng.cnn_top_headlines.Fragment.FavoriteNewsFragment;
import com.ntseng.cnn_top_headlines.Fragment.TopNewsFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Intent startservice;
    SectionPagerAdapter mSectionPagerAdapter;
    Toolbar toolBar;
    ActionBar actionBar;
    ViewPager viewPager;
    TabLayout tabLayout;

    List fragments;
    TopNewsFragment topNewsFragment;
    FavoriteNewsFragment favoriteNewsFragment;

    private final static int TOP_FRAGMENT = 0;
    private final static int FAVORITE_FRAGMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActiveAndroid.initialize(this);
        Fresco.initialize(this);

        startservice = new Intent(MainActivity.this, XMLParserService.class);
        startService(startservice);

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("CNN News Reader");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        fragments = new ArrayList();
        topNewsFragment = new TopNewsFragment();
        favoriteNewsFragment = new FavoriteNewsFragment();
        fragments.add(topNewsFragment);
        fragments.add(favoriteNewsFragment);

        mSectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mSectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(mOnPageChangeListener);


        IntentFilter grabCompletion = new IntentFilter(XMLParserService.GRAB_COMPLETTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onEvent, grabCompletion);

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(onEvent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(this,SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left );
            return true;
        }else if(id == R.id.action_refresh){
            startService(startservice);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            viewPager.setAdapter(mSectionPagerAdapter);
        }
    };

    public ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            if(position == TOP_FRAGMENT){
                topNewsFragment.onRefresh();
            }else if(position == FAVORITE_FRAGMENT){
                favoriteNewsFragment.onRefresh();
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
