package com.ntseng.cnn_top_headlines;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.view.View;

import com.activeandroid.ActiveAndroid;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.ntseng.cnn_top_headlines.adapter.SectionPagerAdapter;
import com.ntseng.cnn_top_headlines.fragment.FavoriteNewsFragment;
import com.ntseng.cnn_top_headlines.fragment.TopNewsFragment;
import com.ntseng.cnn_top_headlines.service.RoutineUpdateService;
import com.ntseng.cnn_top_headlines.service.XMLParserService;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Intent startservice;
    Intent startRoutineUpdate;
    SectionPagerAdapter mSectionPagerAdapter;

    SharedPreferences prefs = null;

    List fragments;
    TopNewsFragment topNewsFragment;
    FavoriteNewsFragment favoriteNewsFragment;
    ViewPager viewPager;

    ProgressDialog dialog;

    private final static int TOP_FRAGMENT = 0;
    private final static int FAVORITE_FRAGMENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActiveAndroid.initialize(this);
        Fresco.initialize(this);

        prefs = getSharedPreferences("com.ntseng.cnn", MODE_PRIVATE);

        startservice = new Intent(MainActivity.this, XMLParserService.class);
        startRoutineUpdate = new Intent(MainActivity.this, RoutineUpdateService.class);
        startService(startRoutineUpdate);


        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("CNN News Reader");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
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
        IntentFilter favoriteIconChange = new IntentFilter(NewsDetailActivity.FAVORITE_ICON_CHANGE);
        LocalBroadcastManager.getInstance(this).registerReceiver(onEvent, favoriteIconChange);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onEvent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstinstall", true)) {
            prefs.edit().putBoolean("firstinstall", false).commit();
            startService(startservice);
        }
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
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left );
            return true;
        }else if(id == R.id.action_refresh){
            startService(startservice);
            dialog = ProgressDialog.show(this, "Refreshing", "Please Wait...", true);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            viewPager.setAdapter(mSectionPagerAdapter);
            if(dialog != null)
                dialog.dismiss();
        }
    };

    public ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener(){

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
