package com.ntseng.cnn_top_headlines;


import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ntseng.cnn_top_headlines.singleton.DAOSingleton;
import com.ntseng.cnn_top_headlines.model.NewsItem;



public class NewsDetailActivity extends ActionBarActivity {

    private NewsItem newsItem;
    WebView myBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle  = getIntent().getExtras();
        newsItem = (NewsItem)bundle.getSerializable("newsItem");

        myBrowser = (WebView)findViewById(R.id.mybrowser);

        actionBar.setTitle(newsItem.getTitle());

        String myURL = newsItem.getLink();

        WebSettings webSettings = myBrowser.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);

        myBrowser.loadUrl(myURL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        changeFavoriteIcon(menu.findItem(R.id.action_add_favorite));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_favorite) {
            saveFavorite(item);
            return true;
        }else if (id == android.R.id.home) {
            onPauseBrowser();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
     public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
            onPauseBrowser();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void changeFavoriteIcon(MenuItem menuItem){
        if(newsItem.getFavorite() != null && newsItem.getFavorite()) {
            menuItem.setIcon(R.drawable.ic_star_black_36dp);
        } else {
            menuItem.setIcon(R.drawable.ic_star_border_black_36dp);
        }
    }

    private void saveFavorite(MenuItem menuItem){

        NewsItem newsItemDB = DAOSingleton.getDAOInstance().getNewsTitle(newsItem.getTitle()).get(0);
        if(newsItemDB.getFavorite() != null && newsItemDB.getFavorite()) {
            newsItemDB.setFavorite(false);
            menuItem.setIcon(R.drawable.ic_star_border_black_36dp);
        } else {
            newsItemDB.setFavorite(true);
            menuItem.setIcon(R.drawable.ic_star_black_36dp);
        }
        DAOSingleton.getDAOInstance().saveItem(newsItemDB);

    }

    private void onPauseBrowser(){
        myBrowser.clearHistory();
        myBrowser.onPause();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left );
    }
}
