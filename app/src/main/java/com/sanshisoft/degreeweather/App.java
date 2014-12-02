package com.sanshisoft.degreeweather;

import android.app.Application;
import android.content.Context;

/**
 * Created by chenleicpp on 2014/12/2.
 */
public class App extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
