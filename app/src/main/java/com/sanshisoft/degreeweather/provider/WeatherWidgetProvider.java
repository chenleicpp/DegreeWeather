package com.sanshisoft.degreeweather.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.sanshisoft.degreeweather.service.WeatherUpdateService;
import com.sanshisoft.degreeweather.util.LogUtil;

/**
 * Created by chenleicpp on 2014/12/31.
 */
public class WeatherWidgetProvider extends AppWidgetProvider {

    public static final String UPDATE_WIDGET_WEATHER_ACTION = "com.sanshisoft.action.update_weather";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        LogUtil.d("onReceive action = " + action);
        if (action.equals("android.intent.action.USER_PRESENT")) {
            context.startService(new Intent(context, WeatherUpdateService.class));
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            context.startService(new Intent(context, WeatherUpdateService.class));
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent intent = new Intent(context, WeatherUpdateService.class);
        context.startService(intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, WeatherUpdateService.class);
        context.stopService(intent);
    }
}
