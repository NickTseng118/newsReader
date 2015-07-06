package com.ntseng.cnn_top_headlines.model;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class NewsItemDAO {

    //private List<NewsItem> newsItemList;
    private List<NewsItem> newsItemDBList;
    Select select = new Select();

    public NewsItemDAO(){

    }
    public List<NewsItem> selectAll(){
        newsItemDBList = select.all().from((NewsItem.class)).orderBy("pubDate DESC").execute();
        return newsItemDBList;
    }

    public List<NewsItem> getNewsTitle (String title) {
        return select.from(NewsItem.class).where ("title = ?", title).execute();
    }

    public List<NewsItem> queryNewsByTitle(String keyword){
        return select.from(NewsItem.class).where("title LIKE ?", new String[] {"%" + keyword + "%" }).execute();
    }

    public List<NewsItem> getFavoriteNews (boolean favorite) {
        return select.from(NewsItem.class).where ("favorite = ?", favorite).execute();
    }

    public void merge(List<NewsItem> newsItemList){
        List<NewsItem> saveNewsItemList = new ArrayList<NewsItem>();
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
                saveNewsItemList.add(0, newsItem);
            }
        }
        saveFromList(saveNewsItemList);
    }

    public void saveFromList(List<NewsItem> saveNewsItemList){
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

    public void saveItem(NewsItem newsItem){
        newsItem.save();
    }

}
