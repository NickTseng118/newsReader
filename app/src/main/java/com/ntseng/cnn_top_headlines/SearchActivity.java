package com.ntseng.cnn_top_headlines;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ntseng.cnn_top_headlines.Singleton.DAOSingleton;
import com.ntseng.cnn_top_headlines.adapter.SearchesAdapter;
import com.ntseng.cnn_top_headlines.adapter.TopNewsAdapter;
import com.ntseng.cnn_top_headlines.model.NewsItem;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class SearchActivity extends AppCompatActivity {

    EditText keywordET;
    ListView preSearchesListView;
    TextView searchesTV;

    SharedPreferences spfKeyword;
    SharedPreferences.Editor spfEditor;

    private List<NewsItem> resultList;
    private List keywords = new ArrayList();

    TopNewsAdapter mTopNewsAdapter;
    SearchesAdapter mSearchesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        keywordET = (EditText) findViewById(R.id.text_search);
        ImageView cancelButton = (ImageView) findViewById(R.id.cancel_button);
        preSearchesListView = (ListView) findViewById(R.id.searches_listView);
        ListView resultListView = (ListView) findViewById(R.id.result_listView);
        searchesTV = (TextView) findViewById(R.id.searches_display);

        spfKeyword = getSharedPreferences("preSearches", MODE_PRIVATE);
        spfEditor = spfKeyword.edit();

        keywordET.setText(spfKeyword.getString("preSearches", null));
        keywordET.setOnKeyListener(mOnKeyListener);

        resultList = getResults(null);
        mTopNewsAdapter = new TopNewsAdapter(SearchActivity.this, resultList);
        resultListView.setAdapter(mTopNewsAdapter);
        resultListView.setOnItemClickListener(resultListOnItemClickListener);

        getKeywords();
        mSearchesAdapter = new SearchesAdapter(this, keywords);
        preSearchesListView.setAdapter(mSearchesAdapter);
        preSearchesListView.setOnItemClickListener(searchesListOnItemClickListener);


        cancelButton.setOnClickListener(cancelBTOnClickListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right );
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right );
        }
        return super.onKeyDown(keyCode, event);
    }

    private View.OnClickListener cancelBTOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(resultList != null) {
                resultList.clear();
                mTopNewsAdapter.notifyDataSetChanged();
            }
            keywordET.setText(null);
            searchesTV.setText("Previous Searches");
            preSearchesListView.setVisibility(View.VISIBLE);
        }
    };


    private AdapterView.OnItemClickListener resultListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(SearchActivity.this, NewsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("newsItem", resultList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    private AdapterView.OnItemClickListener searchesListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            keywordET.setText(String.valueOf(keywords.get(position)));
            resultList.addAll(getResults(String.valueOf(keywords.get(position))));
            mTopNewsAdapter.notifyDataSetChanged();
            preSearchesListView.setVisibility(View.GONE);
            searchesTV.setText(resultList.size() + " articles matched your keyword");


        }
    };

    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener(){
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    Log.e("text", "edit " + keywordET.getText());
                    String keyword = String.valueOf(keywordET.getText());
                    saveSearches(keyword);
                    resultList.addAll(getResults(keyword));
                    mTopNewsAdapter.notifyDataSetChanged();
                    preSearchesListView.setVisibility(View.GONE);
                    searchesTV.setText(resultList.size() + " articles matched your keyword");
                }
            }
            return false;
        }

    };

    private List<NewsItem> getResults(String keyword){
        return DAOSingleton.getDAOInstance().queryNewsByTitle(keyword);
    }

    private void saveSearches(String s){
        keywords.add(0, s);
        if(keywords.size() > 5){
            keywords.remove(5);
        }
        mSearchesAdapter.notifyDataSetChanged();

        Gson gson = new Gson();
        String keyword = gson.toJson(keywords);

        spfEditor.putString("keyword", keyword);
        spfEditor.commit();
    }

    private List<String> getKeywords(){

        String keyword = spfKeyword.getString("keyword", null);

        Gson gson = new Gson();
        Type type = new TypeToken<List>(){}.getType();
        if(keyword == null){
            keywords.add("");
        } else {
            keywords = gson.fromJson(keyword, type);
            searchesTV.setText("Previous Searches");
        }
        return keywords;
    }

}
