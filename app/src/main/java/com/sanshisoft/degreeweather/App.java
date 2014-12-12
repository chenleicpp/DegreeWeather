package com.sanshisoft.degreeweather;

import android.app.Application;
import android.content.Context;

import com.sanshisoft.degreeweather.db.CityDB;

/**
 * Created by chenleicpp on 2014/12/2.
 */
public class App extends Application {
    private static Context sContext;

    private CityDB cityDB;

    private static App mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        sContext = getApplicationContext();
    }

    public static App getInstance(){
        return mApplication;
    }

    public static Context getContext() {
        return sContext;
    }

    public void setCityDB(CityDB db){
        this.cityDB = db;
    }

    public CityDB getCityDB(){
        return cityDB;
    }
}

