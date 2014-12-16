package com.sanshisoft.degreeweather.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.model.Weather;
import com.sanshisoft.degreeweather.net.GsonRequest;
import com.sanshisoft.degreeweather.util.LogUtil;
import com.sanshisoft.degreeweather.util.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenleicpp on 2014/12/2.
 */
public class LiveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.now_city)
    TextView mNowCity;
    @InjectView(R.id.now_p_time)
    TextView mNowTime;
    @InjectView(R.id.now_description)
    TextView mNowDesc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);
        ButterKnife.inject(this,rootView);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        loadData(true);

        return rootView;
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    private void startProgress(){
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
    }

    private void stopProgress(){
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        });
    }

    private void loadData(boolean auto){
        if (auto){
            startProgress();
        }
        //load
        String url = Utils.getWeatherUrl(App.getInstance().getCityNumber());
        executeRequest(new GsonRequest<Weather>(url, Weather.class, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather weather) {
                LogUtil.d(weather.toString());
                mNowCity.setText(weather.getForecast().getCity());
                mNowTime.setText(weather.getRealtime().getTime());
                mNowDesc.setText(weather.getRealtime().getWeather());
                int todayMax = getTodayMax(weather.getForecast().getTemp1());
                int todayMin = getTodayMin(weather.getForecast().getTemp1());
                int yestodayMax = Integer.parseInt(weather.getYestoday().getTempMax());
                int yestodayMin = Integer.parseInt(weather.getYestoday().getTempMin());
                LogUtil.d("todayMax:"+todayMax+"   todayMin:"+todayMin);
                LogUtil.d("yestodayMax:"+yestodayMax+"   yestodayMin:"+yestodayMin);
                stopProgress();
            }
        }, errorListener()));
    }

    private int getTodayMax(String str){
        String res = null;
        String temp = str.split("~")[0];
        res = temp.substring(0,temp.indexOf("℃"));
        if (res != null && !res.isEmpty()){
            return Integer.parseInt(res);
        }
        return -256;
    }

    private int getTodayMin(String str){
        String res = null;
        String temp = str.split("~")[1];
        res = temp.substring(0,temp.indexOf("℃"));
        if (res != null && !res.isEmpty()){
            return Integer.parseInt(res);
        }
        return -256;
    }
}
