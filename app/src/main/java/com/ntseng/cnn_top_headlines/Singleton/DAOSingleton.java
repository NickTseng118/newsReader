package com.ntseng.cnn_top_headlines.singleton;

import com.ntseng.cnn_top_headlines.model.NewsItemDAO;

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
