package com.sanshisoft.degreeweather.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.AppConfig;
import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.model.Weather;
import com.sanshisoft.degreeweather.net.GsonRequest;
import com.sanshisoft.degreeweather.net.RequestManager;
import com.sanshisoft.degreeweather.provider.WeatherWidgetProvider;
import com.sanshisoft.degreeweather.ui.MainActivity;
import com.sanshisoft.degreeweather.ui.WelcomeActivity;
import com.sanshisoft.degreeweather.util.LogUtil;
import com.sanshisoft.degreeweather.util.Utils;

import cn.trinea.android.common.util.PreferencesUtils;

/**
 * Created by chenleicpp on 2015/1/20.
 */
public class WeatherUpdateService extends Service {

    private RemoteViews remoteViews;

    private BroadcastReceiver mUpdateBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(
                    WeatherWidgetProvider.UPDATE_WIDGET_WEATHER_ACTION)) {
                LogUtil.d("update from main app!!!");
                updateWeather();
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean flag = PreferencesUtils.getBoolean(App.getContext(), AppConfig.ISFIRSTIN, true);
        if (flag){
            remoteViews.setViewVisibility(R.id.ll_unready, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.ll_ready, View.GONE);
        }else {
            remoteViews.setViewVisibility(R.id.ll_ready, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.ll_unready, View.GONE);
        }

        IntentFilter updateIntent = new IntentFilter();
        updateIntent.addAction(WeatherWidgetProvider.UPDATE_WIDGET_WEATHER_ACTION);
        registerReceiver(mUpdateBroadcast, updateIntent);

        updateWeather();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        remoteViews = new RemoteViews(App.getContext().getPackageName(),R.layout.weather_appwidget);
        PendingIntent WeatherIconHotAreaPI = PendingIntent.getActivity(this, 0,
                new Intent(this, WelcomeActivity.class), 0);
        remoteViews.setOnClickPendingIntent(R.id.tv_jumpmain,
                WeatherIconHotAreaPI);
        remoteViews.setOnClickPendingIntent(R.id.ll_ready,WeatherIconHotAreaPI);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUpdateBroadcast != null){
            unregisterReceiver(mUpdateBroadcast);
        }
        RequestManager.cancelAll(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateWeather(){
        String cityNum = PreferencesUtils.getString(App.getContext(),AppConfig.CITY_NUMBER);

        String url = Utils.getWeatherUrl(cityNum);
        RequestManager.addRequest(new GsonRequest<Weather>(url, Weather.class, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather weather) {
                LogUtil.d(weather.toString());
                int todayMax = getTodayMax(weather.getForecast().getTemp1());
                int todayMin = getTodayMin(weather.getForecast().getTemp1());
                int yestodayMax = Integer.parseInt(weather.getYestoday().getTempMax());
                int yestodayMin = Integer.parseInt(weather.getYestoday().getTempMin());
                showDiffDegree(todayMax, todayMin, yestodayMax, yestodayMin);
                ComponentName componentName = new ComponentName(getApplication(),
                        WeatherWidgetProvider.class);
                AppWidgetManager.getInstance(getApplication()).updateAppWidget(
                        componentName, remoteViews);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(App.getContext(),R.string.toast_update_failed,Toast.LENGTH_LONG).show();
            }
        }),this);

    }

    private int getTodayMax(String str){
        int res1,res2;
        String temp1 = str.split("~")[0];
        String temp2 = str.split("~")[1];
        res1 = Integer.parseInt(temp1.substring(0,temp1.indexOf("℃")));
        res2 = Integer.parseInt(temp2.substring(0,temp2.indexOf("℃")));
        if (res1 < res2){
            return res2;
        }else {
            return res1;
        }
    }

    private int getTodayMin(String str){
        int res1,res2;
        String temp1 = str.split("~")[0];
        String temp2 = str.split("~")[1];
        res1 = Integer.parseInt(temp1.substring(0,temp1.indexOf("℃")));
        res2 = Integer.parseInt(temp2.substring(0,temp2.indexOf("℃")));
        if (res1 < res2){
            return res1;
        }else {
            return res2;
        }
    }

    private void showDiffDegree(int todayMax,int todayMin,int yestodayMax,int yestodayMin){
        int diff;
        if (todayMax > yestodayMax) {
            diff = todayMax - yestodayMax;
            remoteViews.setTextViewText(R.id.high_digit,diff + "°");
            remoteViews.setImageViewResource(R.id.high_img,R.drawable.ic_trending_up_black_48dp);
        } else if (todayMax == yestodayMax) {
            diff = todayMax - yestodayMax;
            remoteViews.setTextViewText(R.id.high_digit,diff + "°");
            remoteViews.setImageViewResource(R.id.high_img,R.drawable.ic_trending_neutral_black_48dp);
        } else if (todayMax < yestodayMax) {
            diff = Math.abs(todayMax - yestodayMax);
            remoteViews.setTextViewText(R.id.high_digit, diff + "°");
            remoteViews.setImageViewResource(R.id.high_img, R.drawable.ic_trending_down_black_48dp);
        }
        if (todayMin > yestodayMin) {
            diff = todayMin - yestodayMin;
            remoteViews.setTextViewText(R.id.low_digit,diff + "°");
            remoteViews.setImageViewResource(R.id.low_img,R.drawable.ic_trending_up_black_48dp);
        } else if (todayMin == yestodayMin) {
            diff = todayMin - yestodayMin;
            remoteViews.setTextViewText(R.id.low_digit, diff + "°");
            remoteViews.setImageViewResource(R.id.low_img, R.drawable.ic_trending_neutral_black_48dp);
        } else if (todayMin < yestodayMin) {
            diff = Math.abs(todayMin - yestodayMin);
            remoteViews.setTextViewText(R.id.low_digit, diff + "°");
            remoteViews.setImageViewResource(R.id.low_img, R.drawable.ic_trending_down_black_48dp);
        }
    }
}
