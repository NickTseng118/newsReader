package com.ntseng.cnn_top_headlines.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ntseng.cnn_top_headlines.singleton.DAOSingleton;
import com.ntseng.cnn_top_headlines.model.NewsItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class XMLParserService extends IntentService {

    private String text;
    private List<NewsItem> newsItemList = new ArrayList<NewsItem>();
    List<NewsItem> datebaseList;

    public XMLParserService() {
        super("XMLParserService");
    }
    public static final String GRAB_COMPLETTION = "com.ntseng.cnn_top_headlines.XMLparserService.grab.COMPLETE";
    public static final String GRAB_FAILURE = "com.ntseng.cnn_top_headlines.XMLparserService.grab.GRAB_FAILURE";

    private boolean downloadSuccessful = false;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (networkIsAvailable()) {
            openCNNHttpConnection();
            if(newsItemList != null)
            DAOSingleton.getDAOInstance().merge(newsItemList);
        }

    }
    public void onDestroy() {
        Log.v("test", "onDestroy");
        super.onDestroy();
        Log.v("downloadSuccessful", "onDestroy" + downloadSuccessful);
        if(downloadSuccessful == true){
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(GRAB_COMPLETTION));
        }else{
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(GRAB_FAILURE));
        }
    }

    private void openCNNHttpConnection(){
        try{
            URL input = new URL("http://rss.cnn.com/rss/edition.rss");
            HttpURLConnection httpURLConnection = (HttpURLConnection)input.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.connect();

            if( httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK ){
                parseRssFeed(httpURLConnection.getInputStream());
                downloadSuccessful = true;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch(UnknownHostException e){
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseRssFeed(InputStream inputStream)throws XmlPullParserException, IOException {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(inputStream, null);

        int eventType = xpp.getEventType();

        NewsItem newsItem = null;

        while (eventType != XmlPullParser.END_DOCUMENT){

            String tagName = xpp.getName();
            String url;

            if (eventType == XmlPullParser.START_DOCUMENT){

            }else if (eventType == XmlPullParser.START_TAG){
                if(tagName.equalsIgnoreCase("item")) {
                    newsItem = new NewsItem();
                }
            }else if (eventType == XmlPullParser.TEXT){

                text = xpp.getText();

            }else if (eventType == XmlPullParser.END_TAG){

                if(newsItem != null) {
                    if (tagName.equalsIgnoreCase("title")) {
                        newsItem.setTitle(text);
                        Log.e("title", "is " + text);
                    } else if (tagName.equalsIgnoreCase("guid")) {
                        newsItem.setGuid(text);
                    } else if (tagName.equalsIgnoreCase("link")) {
                        newsItem.setLink(text);
                    } else if (tagName.equalsIgnoreCase("description")) {
                       // Log.e("description", "is" + text);
                        newsItem.setDescription(text);
                    } else if (tagName.equalsIgnoreCase("pubDate")) {
                        newsItem.setPubDate(dateFormat(text));
                    } else if (tagName.equalsIgnoreCase("thumbnail")) {
                       // Log.e("thumbnail", "is" + xpp.getAttributeValue(null, "url"));
                        url = xpp.getAttributeValue(null, "url");
                        newsItem.setMediaThumbnail(url);
                    } else if (tagName.equalsIgnoreCase("content")) {
                       // Log.e("content", "is" + xpp.getAttributeValue(null, "url"));
                        url = xpp.getAttributeValue(null, "url");
                        newsItem.setMediaContent(url);
                    } else if(tagName.equalsIgnoreCase("item")){
                        newsItemList.add(newsItem);
                    }
                }
            }
            eventType = xpp.next();
        }
    }

    private Date dateFormat(String text){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss Z", Locale.US);
        try {
            return formatter.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean networkIsAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }



}
