package com.sanshisoft.degreeweather.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.R;

/**
 * Created by chenleicpp on 2014/12/4.
 */
public class Utils {

    /**
     * 获取app版本信息
     * @return
     */
    public static String getVersionName() {
        String version = null;
        try {
            PackageManager manager = App.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(App.getContext().getPackageName(), 0);
            version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

    }

    public static boolean isNetVisible(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null){
            return cm.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
}
