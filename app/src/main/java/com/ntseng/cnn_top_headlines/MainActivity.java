package com.ntseng.cnn_top_headlines;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.ntseng.cnn_top_headlines.adapter.SectionPagerAdapter;
import com.ntseng.cnn_top_headlines.fragment.FavoriteNewsFragment;
import com.ntseng.cnn_top_headlines.fragment.NewsDetailFragment;
import com.ntseng.cnn_top_headlines.fragment.NewsFragment;
import com.ntseng.cnn_top_headlines.fragment.SearchFragment;
import com.ntseng.cnn_top_headlines.fragment.TopNewsFragment;
import com.ntseng.cnn_top_headlines.service.RoutineUpdateService;
import com.ntseng.cnn_top_headlines.service.XMLParserService;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Intent startservice;
    Intent startRoutineUpdate;

    SharedPreferences prefs = null;

    ProgressDialog dialog;
    FragmentManager fragmentManager;

    NewsFragment newsFragment;
    SearchFragment searchFragment;

    ImageView cancelButton;
    ImageView refreshButton;
    ImageView favoriteButton;
    ImageView searchButton;
    ImageView backButton;
    EditText keywordET;
    TextView titleTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsFragment = new NewsFragment();
        searchFragment = new SearchFragment();

        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.fragment_container, newsFragment)
                    .commit();
        }

        ActiveAndroid.initialize(this);
        Fresco.initialize(this);

        prefs = getSharedPreferences("com.ntseng.cnn", MODE_PRIVATE);

        startservice = new Intent(MainActivity.this, XMLParserService.class);
        startRoutineUpdate = new Intent(MainActivity.this, RoutineUpdateService.class);
        startService(startRoutineUpdate);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);


        refreshButton = (ImageView) findViewById(R.id.refresh_button);
        searchButton = (ImageView) findViewById(R.id.search_button);
        favoriteButton = (ImageView) findViewById(R.id.favorite_button);
        keywordET = (EditText) findViewById(R.id.text_search);
        cancelButton = (ImageView) findViewById(R.id.cancel_button);
        backButton = (ImageView) findViewById(R.id.back_button);
        titleTV = (TextView) findViewById(R.id.title_textview);

        titleTV.setText("CNN Reader");
        fragmentManager = getSupportFragmentManager();


        refreshButton.setOnClickListener(refreshButtonOnClickListener);
        searchButton.setOnClickListener(searchButtonOnClickListener);
        backButton.setOnClickListener(backButtonOnClickListsner);
        cancelButton.setOnClickListener(cancelButtonOnClickListener);
        keywordET.setOnKeyListener(mOnKeyListener);

        IntentFilter grabCompletion = new IntentFilter(XMLParserService.GRAB_COMPLETTION);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(onEvent, grabCompletion);

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


    private BroadcastReceiver onEvent = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(XMLParserService.GRAB_COMPLETTION)){
                if (dialog != null)
                    dialog.dismiss();
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK) {
            backToNewsFragment();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener(){
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchFragment.setKeyword(String.valueOf(keywordET.getText()));
                }

            }
            return false;
        }

    };

    private View.OnClickListener searchButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancelButton.setVisibility(View.VISIBLE);
            keywordET.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.GONE);
            refreshButton.setVisibility(View.GONE);
            transferToSearchFragment();
        }
    };

    private View.OnClickListener refreshButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startService(startservice);
            dialog = ProgressDialog.show(MainActivity.this, "Refreshing", "Please Wait...", true);
        }
    };

    private View.OnClickListener cancelButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            keywordET.setText(null);
            searchFragment.displayPreSearches();
        }
    };

    private View.OnClickListener favoriteButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener backButtonOnClickListsner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backToNewsFragment();
        }
    };

    private void backToNewsFragment(){
        cancelButton.setVisibility(View.GONE);
        keywordET.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);
        refreshButton.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
                .replace(R.id.fragment_container, newsFragment)
                .commit();
    }
    private void transferToSearchFragment(){
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left)
                .replace(R.id.fragment_container, searchFragment)
                .commit();
    }

}
