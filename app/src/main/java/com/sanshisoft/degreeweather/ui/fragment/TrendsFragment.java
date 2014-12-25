package com.sanshisoft.degreeweather.ui.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.bean.City;
import com.sanshisoft.degreeweather.db.CityDB;
import com.sanshisoft.degreeweather.model.TWeather;
import com.sanshisoft.degreeweather.model.Weather;
import com.sanshisoft.degreeweather.net.GsonRequest;
import com.sanshisoft.degreeweather.util.LogUtil;
import com.sanshisoft.degreeweather.util.Utils;

import java.sql.SQLException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenleicpp on 2014/12/2.
 */
public class TrendsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AMapLocationListener {

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    @InjectView(R.id.trends_city)
    TextView mTrendsCity;
    @InjectView(R.id.trends_p_time)
    TextView mTrendsPTime;

    @InjectView(R.id.trends_middle_1)
    TextView mTrendsMiddle1;
    @InjectView(R.id.trends_right_1)
    TextView mTrendsRight1;

    @InjectView(R.id.trends_middle_2)
    TextView mTrendsMiddle2;
    @InjectView(R.id.trends_right_2)
    TextView mTrendsRight2;

    @InjectView(R.id.trends_middle_3)
    TextView mTrendsMiddle3;
    @InjectView(R.id.trends_right_3)
    TextView mTrendsRight3;

    @InjectView(R.id.trends_middle_4)
    TextView mTrendsMiddle4;
    @InjectView(R.id.trends_right_4)
    TextView mTrendsRight4;

    private LocationManagerProxy mLocationManagerProxy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mLocationManagerProxy = LocationManagerProxy.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trends, container, false);
        ButterKnife.inject(this, rootView);
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

    private void loadFromDb(){
        if (dao.getRowCount() >= 0){
            TWeather weather = dao.getTWeather();
            if (weather != null){
                mTrendsCity.setText(weather.getCity());
                mTrendsPTime.setText(weather.getTime());
                if (isAsc(weather.getTemp1())){
                    mTrendsMiddle1.setText(weather.getYtempMin()+"℃~"+weather.getYtempMax()+"℃");
                }else {
                    mTrendsMiddle1.setText(weather.getYtempMax()+"℃~"+weather.getYtempMin()+"℃");
                }
                mTrendsRight1.setText(weather.getWeatherEnd());
                mTrendsMiddle2.setText(weather.getTemp1());
                mTrendsRight2.setText(weather.getWeather1());
                mTrendsMiddle3.setText(weather.getTemp2());
                mTrendsRight3.setText(weather.getWeather2());
                mTrendsMiddle4.setText(weather.getTemp3());
                mTrendsRight4.setText(weather.getWeather3());
            }
        }
    }

    private void loadData(boolean auto){
        if (auto){
            startProgress(mSwipeLayout);
        }
        //load
        String url = Utils.getWeatherUrl(App.getInstance().getCityNumber());
        executeRequest(new GsonRequest<Weather>(url, Weather.class, new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather weather) {
                LogUtil.d(weather.toString());
                mTrendsCity.setText(weather.getForecast().getCity());
                mTrendsPTime.setText(weather.getRealtime().getTime());
                if (isAsc(weather.getForecast().getTemp1())){
                    mTrendsMiddle1.setText(weather.getYestoday().getTempMin()+"℃~"+weather.getYestoday().getTempMax()+"℃");
                }else {
                    mTrendsMiddle1.setText(weather.getYestoday().getTempMax()+"℃~"+weather.getYestoday().getTempMin()+"℃");
                }

                mTrendsRight1.setText(weather.getYestoday().getWeatherEnd());
                mTrendsMiddle2.setText(weather.getForecast().getTemp1());
                mTrendsRight2.setText(weather.getForecast().getWeather1());
                mTrendsMiddle3.setText(weather.getForecast().getTemp2());
                mTrendsRight3.setText(weather.getForecast().getWeather2());
                mTrendsMiddle4.setText(weather.getForecast().getTemp3());
                mTrendsRight4.setText(weather.getForecast().getWeather3());
                try {
                    dao.insertNew(weather);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                stopProgress(mSwipeLayout);
            }
        }, errorListener()));
    }

    private boolean isAsc(String str){
        int res1,res2;
        String temp1 = str.split("~")[0];
        String temp2 = str.split("~")[1];
        res1 = Integer.parseInt(temp1.substring(0,temp1.indexOf("℃")));
        res2 = Integer.parseInt(temp2.substring(0,temp2.indexOf("℃")));
        if (res1 < res2){
            return true;
        }
        return false;
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
