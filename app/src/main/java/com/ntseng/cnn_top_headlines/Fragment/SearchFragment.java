package com.ntseng.cnn_top_headlines.fragment;

import android.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ntseng.cnn_top_headlines.NewsDetailActivity;
import com.ntseng.cnn_top_headlines.R;
import com.ntseng.cnn_top_headlines.adapter.SearchesAdapter;
import com.ntseng.cnn_top_headlines.adapter.TopNewsAdapter;
import com.ntseng.cnn_top_headlines.model.NewsItem;
import com.ntseng.cnn_top_headlines.singleton.DAOSingleton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends DialogFragment {

    SharedPreferences spfKeyword;
    SharedPreferences.Editor spfEditor;

    private List<NewsItem> resultList;
    private List keywords = new ArrayList();

    TopNewsAdapter mTopNewsAdapter;
    SearchesAdapter mSearchesAdapter;

    ListView preSearchesListView;
    ListView resultListView;
    TextView searchesTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preSearchesListView = (ListView) getActivity().findViewById(R.id.searches_listView);
        resultListView = (ListView) getActivity().findViewById(R.id.result_listView);
        searchesTV = (TextView) getActivity().findViewById(R.id.searches_display);


        spfKeyword = getActivity().getSharedPreferences("preSearches", getActivity().MODE_PRIVATE);
        spfEditor = spfKeyword.edit();

        resultList = getResults(null);
        mTopNewsAdapter = new TopNewsAdapter(getActivity(), resultList);
        resultListView.setAdapter(mTopNewsAdapter);
        resultListView.setOnItemClickListener(resultListOnItemClickListener);

        getKeywords();
        mSearchesAdapter = new SearchesAdapter(getActivity(), keywords);
        preSearchesListView.setAdapter(mSearchesAdapter);
        preSearchesListView.setOnItemClickListener(searchesListOnItemClickListener);

    }

    private AdapterView.OnItemClickListener resultListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("newsItem", resultList.get(position));
            intent.putExtras(bundle);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
    };

    private AdapterView.OnItemClickListener searchesListOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            resultList.clear();
            resultList.addAll(getResults(String.valueOf(keywords.get(position))));
            mTopNewsAdapter.notifyDataSetChanged();
            preSearchesListView.setVisibility(View.GONE);
            resultListView.setVisibility(View.VISIBLE);
            searchesTV.setText(resultList.size() + " articles matched your keyword");
        }
    };


    private List<NewsItem> getResults(String keyword){
        return DAOSingleton.getDAOInstance().getNewsByKeyword(keyword);
    }

    private void saveSearches(String s){

        keywords.add(0, s);
        if(keywords.size() > 5){
            keywords.remove(5);
        }
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

    public void setKeyword(String keyword){
        saveSearches(keyword);
        resultList.clear();
        resultList.addAll(getResults(keyword));
        mTopNewsAdapter.notifyDataSetChanged();
        resultListView.setVisibility(View.VISIBLE);
        preSearchesListView.setVisibility(View.GONE);
        searchesTV.setText(resultList.size() + " articles matched your keyword");
    }

    public void displayPreSearches(){
//        keywords.clear();
//        keywords.addAll(getKeywords());
//       mSearchesAdapter.notifyDataSetChanged();
        getKeywords();
        preSearchesListView.setVisibility(View.VISIBLE);
        resultListView.setVisibility(View.GONE);
    }

}
