package com.ntseng.cnn_top_headlines.Singleton;

import com.activeandroid.query.Select;


public class SelectSingleton {

    private static Select INSTANCE;


    public static Select getSelectInstance(){
        if (INSTANCE == null){
            synchronized(SelectSingleton.class){
                INSTANCE = new Select();
            }
        }
        return INSTANCE;
    }
}
