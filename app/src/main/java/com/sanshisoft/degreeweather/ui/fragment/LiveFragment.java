package com.sanshisoft.degreeweather.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.volley.Response;
import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.AppConfig;
import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.bean.City;
import com.sanshisoft.degreeweather.db.CityDB;
import com.sanshisoft.degreeweather.model.EventDesc;
import com.sanshisoft.degreeweather.model.TWeather;
import com.sanshisoft.degreeweather.model.Weather;
import com.sanshisoft.degreeweather.net.GsonRequest;
import com.sanshisoft.degreeweather.ui.MainActivity;
import com.sanshisoft.degreeweather.ui.WelcomeActivity;
import com.sanshisoft.degreeweather.util.LogUtil;
import com.sanshisoft.degreeweather.util.Utils;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.trinea.android.common.util.PreferencesUtils;
import de.greenrobot.event.EventBus;

/**
 * Created by chenleicpp on 2014/12/2.
 */
public class LiveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, AMapLocationListener {

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.now_city)
    TextView mNowCity;
    @InjectView(R.id.now_p_time)
    TextView mNowTime;
    @InjectView(R.id.now_description)
    TextView mNowDesc;
    @InjectView(R.id.high_temp)
    TextView mHighTemp;
    @InjectView(R.id.low_temp)
    TextView mLowTemp;

    private LocationManagerProxy mLocationManagerProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mLocationManagerProxy = LocationManagerProxy.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);
        ButterKnife.inject(this,rootView);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        loadFromDb();
        loadData(true);

        return rootView;
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    private void loadData(final boolean auto){
        if (auto){
            startProgress(mSwipeLayout);
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
                LogUtil.d("todayMax:" + todayMax + "   todayMin:" + todayMin);
                LogUtil.d("yestodayMax:" + yestodayMax + "   yestodayMin:" + yestodayMin);
                showDiffDegree(todayMax, todayMin, yestodayMax, yestodayMin);
                EventBus.getDefault().post(new EventDesc(weather.getRealtime().getWeather()));
                try {
                    dao.insertNew(weather);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                updateWidget();
                stopProgress(mSwipeLayout);
            }
        }, errorListener()));
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

    private void drawLeftHigh(TextView tv){
        Drawable drawable= getResources().getDrawable(R.drawable.ic_trending_up_white_48dp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable, null, null, null);
    }

    private void drawLeftLow(TextView tv){
        Drawable drawable= getResources().getDrawable(R.drawable.ic_trending_down_white_48dp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable,null,null,null);
    }

    private void drawLeftSame(TextView tv){
        Drawable drawable= getResources().getDrawable(R.drawable.ic_trending_neutral_white_48dp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable,null,null,null);
    }

    private void showDiffDegree(int todayMax,int todayMin,int yestodayMax,int yestodayMin){
        int diff;
        if (todayMax > yestodayMax) {
            diff = todayMax - yestodayMax;
            mHighTemp.setText(diff + "℃");
            drawLeftHigh(mHighTemp);
        } else if (todayMax == yestodayMax) {
            diff = todayMax - yestodayMax;
            mHighTemp.setText(diff + "℃");
            drawLeftSame(mHighTemp);
        } else if (todayMax < yestodayMax) {
            diff = Math.abs(todayMax - yestodayMax);
            mHighTemp.setText(diff + "℃");
            drawLeftLow(mHighTemp);
        }
        if (todayMin > yestodayMin) {
            diff = todayMin - yestodayMin;
            mLowTemp.setText(diff + "℃");
            drawLeftHigh(mLowTemp);
        } else if (todayMin == yestodayMin) {
            diff = todayMin - yestodayMin;
            mLowTemp.setText(diff + "℃");
            drawLeftSame(mLowTemp);
        } else if (todayMin < yestodayMin) {
            diff = Math.abs(todayMin - yestodayMin);
            mLowTemp.setText(diff + "℃");
            drawLeftLow(mLowTemp);
        }
    }

    private void loadFromDb(){
        if (dao.getRowCount() >= 0){
            TWeather weather = dao.getTWeather();
            if (weather != null){
                mNowCity.setText(weather.getCity());
                mNowTime.setText(weather.getTime());
                mNowDesc.setText(weather.getWeather());
                int yestodayMax = Integer.parseInt(weather.getYtempMax());
                int yestodayMin = Integer.parseInt(weather.getYtempMin());
                int todayMax = getTodayMax(weather.getTemp1());
                int todayMin = getTodayMin(weather.getTemp1());
                showDiffDegree(todayMax,todayMin,yestodayMax,yestodayMin);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case  R.id.action_refresh:
                loadData(true);
                return true;
            case R.id.action_location:
                requestLocation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        LogUtil.d("error:" + aMapLocation.getAMapException().getErrorMessage());
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            CityDB db = App.getInstance().getCityDB();
            if (db != null){
                City city = db.getCity(aMapLocation.getCity());
                App.getInstance().setCityNumber(city.getNumber());
                PreferencesUtils.putString(App.getContext(), AppConfig.CITY_NUMBER,city.getNumber());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadData(false);
        }else {
            //
            Toast.makeText(getActivity(), R.string.gps_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void requestLocation(){
        startProgress(mSwipeLayout);
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork,-1,15,this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationManagerProxy.removeUpdates(this);
        mLocationManagerProxy.destroy();
    }

}
