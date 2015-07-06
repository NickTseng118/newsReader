package com.ntseng.cnn_top_headlines.Singleton;

import com.activeandroid.query.Select;
import com.ntseng.cnn_top_headlines.model.NewsItem;
import com.ntseng.cnn_top_headlines.model.NewsItemDAO;

/**
 * Created by nicktseng on 15/7/6.
 */
public class DAOSingleton {

    private static NewsItemDAO INSTANCE;

    public static NewsItemDAO getDAOInstance(){
        if (INSTANCE == null){
            synchronized(DAOSingleton.class){
                INSTANCE = new NewsItemDAO();
            }
        }
        return INSTANCE;
    }
}
