package com.ntseng.cnn_top_headlines.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ntseng.cnn_top_headlines.R;
import com.ntseng.cnn_top_headlines.model.NewsItem;


public class NewsDetailFragment extends Fragment {

    WebView myBrowser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Bundle bundle = getArguments();
        NewsItem newsItem = (NewsItem) bundle.getSerializable("newsItem");
        Log.e("@@","@@" + newsItem.getLink());
        myBrowser = (WebView)getActivity().findViewById(R.id.mybrowser);

        String myURL = newsItem.getLink();

        WebSettings webSettings = myBrowser.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);

        myBrowser.loadUrl(myURL);
    }

}
