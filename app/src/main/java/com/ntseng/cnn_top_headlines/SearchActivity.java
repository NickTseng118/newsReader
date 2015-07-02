package com.ntseng.cnn_top_headlines;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
import com.ntseng.cnn_top_headlines.Adapter.SearchesAdapter;
import com.ntseng.cnn_top_headlines.Adapter.TopNewsAdapter;
import com.ntseng.cnn_top_headlines.Model.NewsItem;
import com.ntseng.cnn_top_headlines.Singleton.SelectSingleton;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class SearchActivity extends ActionBarActivity {

    Toolbar toolBar;
    ActionBar actionBar;
    EditText keywordET;
    ImageView cancelButton;
    ListView preSearchesListView;
    ListView resultListView;

    SharedPreferences spfKeyword;
    SharedPreferences.Editor spfEditor;

    private List<String> preSearchesList = new ArrayList();
    private List<NewsItem> resultList;

    TopNewsAdapter mTopNewsAdapter;
    SearchesAdapter mSearchesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        keywordET = (EditText) findViewById(R.id.text_search);
        cancelButton = (ImageView) findViewById(R.id.cancel_button);
        preSearchesListView = (ListView) findViewById(R.id.searches_listView);
        resultListView = (ListView) findViewById(R.id.result_listView);

        resultListView.setOnItemClickListener(resultListOnItemClickListener);
        cancelButton.setOnClickListener(mOnClickListener);

        spfKeyword = getSharedPreferences("preSearches", MODE_PRIVATE);
        spfEditor = spfKeyword.edit();

        keywordET.addTextChangedListener(mTextWatcher);
        keywordET.setText(spfKeyword.getString("preSearches", null));
        keywordET.setOnKeyListener(mOnKeyListener);


        mSearchesAdapter = new SearchesAdapter(this, getSearches());
        preSearchesListView.setAdapter(mSearchesAdapter);
        preSearchesListView.setOnItemClickListener(searchesListOnItemClickListener);

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

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(resultList != null)
                resultList.clear();
            keywordET.setText("");
            preSearchesListView.setVisibility(View.VISIBLE);
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private List<NewsItem> search(String keyword){
        resultList = SelectSingleton.getSelectInstance().from(NewsItem.class).where("title LIKE ?", new String[] {"%" + keyword + "%" }).execute();
        return resultList;
    }

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
            keywordET.setText(preSearchesList.get(position));
        }
    };

    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener(){
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    saveSearches(String.valueOf(keywordET.getText()));
                    search(String.valueOf(keywordET.getText()));
                    mTopNewsAdapter = new TopNewsAdapter(SearchActivity.this, resultList);
                    resultListView.setAdapter(mTopNewsAdapter);
                    preSearchesListView.setVisibility(View.GONE);

                }
            }
            return false;
        }

    };

    private void saveSearches(String s){
        preSearchesList.add(0,s);
        if(preSearchesList.size() > 5){
            preSearchesList.remove(5);
            mSearchesAdapter.notifyDataSetChanged();
        }
        Set<String> savedString = new LinkedHashSet<String>();
        savedString.addAll(preSearchesList);
        spfEditor.putStringSet("keyword", savedString);
        spfEditor.commit();
    }

    private List<String> getSearches(){
        Set<String> savedString = new LinkedHashSet<String>();
        savedString = spfKeyword.getStringSet("keyword", null);
        if(savedString != null) {
            for (String s : savedString) {
                preSearchesList.add(s);
            }
        }
        return preSearchesList;
    }
}
