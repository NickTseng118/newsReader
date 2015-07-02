package com.ntseng.cnn_top_headlines.Model;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ntseng.cnn_top_headlines.Singleton.SelectSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicktseng on 15/6/26.
 */


public class NewsItemDAO {

    private List<NewsItem> newsItemList;
    private List<NewsItem> newsItemDBList;
    private List<NewsItem> saveNewsItemList = new ArrayList<NewsItem>();

    public NewsItemDAO(){

    }
    public NewsItemDAO(List<NewsItem> list){
        this.newsItemList = list;
    }

    public List<NewsItem> selectAll(){
        newsItemDBList = SelectSingleton.getSelectInstance().all().from((NewsItem.class)).execute();
        return newsItemDBList;
    }

    public void compare(){
        for(NewsItem newsItem : newsItemList){
            if(selectAll().size() == 0) {
                saveNewsItemList.add(newsItem);
            }else{
                int value = 0;
                for (NewsItem newsItemDB : selectAll()) {
                    value = newsItem.getTitle().compareToIgnoreCase(newsItemDB.getTitle());
                    if(value == 0){
                        break;
                    }
                }
                if(value != 0)
                saveNewsItemList.add(0,newsItem);
            }
        }
    }

    public void save(){
        ActiveAndroid.beginTransaction();
        try {
            for (NewsItem newsItem : saveNewsItemList) {
                newsItem.save();
            }
            saveNewsItemList.clear();
            ActiveAndroid.setTransactionSuccessful();
            Log.e("tag", "Save Successful");
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

}
