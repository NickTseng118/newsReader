package com.ntseng.cnn_top_headlines;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ntseng.cnn_top_headlines.Model.NewsItem;
import com.ntseng.cnn_top_headlines.Model.NewsItemDAO;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by nicktseng on 15/6/24.
 */
public class XMLParserService extends IntentService {

    private String text;
    private NewsItem newsItem;
    private List<NewsItem> newsItemList = new ArrayList<NewsItem>();
    List<NewsItem> datebaseList;

    public XMLParserService() {
        super("XMLParserService");
    }

    public static final String GRAB_COMPLETTION = "com.ntseng.cnn_top_headlines.XMLparserService.grab.COMPLETE";

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            xmlPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        NewsItemDAO newItemDAO = new NewsItemDAO(newsItemList);
        newItemDAO.compare();
        newItemDAO.save();

        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(GRAB_COMPLETTION));

    }

    public void onDestroy() {
        Log.v("test", "onDestroy");
        super.onDestroy();
    }

    public void xmlPullParser()throws XmlPullParserException, IOException {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        URL input = new URL("http://rss.cnn.com/rss/edition.rss");
        xpp.setInput(input.openStream(), null);

        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT){

            String tagName = xpp.getName();
            String url;

            if (eventType == XmlPullParser.START_DOCUMENT){

            }else if (eventType == XmlPullParser.START_TAG){
                if(tagName.equalsIgnoreCase("item"))
                    newsItem = new NewsItem();
            }else if (eventType == XmlPullParser.TEXT){

                text = xpp.getText();

            }else if (eventType == XmlPullParser.END_TAG){

                if(newsItem != null) {
                    if (tagName.equalsIgnoreCase("title")) {
                        newsItem.setTitle(text);
                    } else if (tagName.equalsIgnoreCase("guid")) {
                        newsItem.setGuid(text);
                    } else if (tagName.equalsIgnoreCase("link")) {
                       // Log.e("link", "is" + text);
                        newsItem.setLink(text);
                    } else if (tagName.equalsIgnoreCase("description")) {
                       // Log.e("description", "is" + text);
                        newsItem.setDescription(text);
                    } else if (tagName.equalsIgnoreCase("pubDate")) {
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss zzz");
                        try {
                            Date date = formatter.parse(text);
                            Log.e("date", "" + date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                       // Log.e("pubDate", "is" + text);
                        newsItem.setPubDate(text);
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
        SimpleDateFormat formatter = new SimpleDateFormat("EEE',' dd MMM yyyy HH:mm:ss zzz");
        try {
            //Log.e("date","" + formatter.parse("Tue, 16 Jun 2015 03:57:20 EDT"));
            return formatter.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
