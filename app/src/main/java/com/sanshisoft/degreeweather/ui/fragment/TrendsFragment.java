package com.sanshisoft.degreeweather.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.R;
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
public class TrendsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

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
                mTrendsMiddle1.setText(weather.getYtempMax()+"℃~"+weather.getYtempMin()+"℃");
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
                mTrendsMiddle1.setText(weather.getYestoday().getTempMax()+"℃~"+weather.getYestoday().getTempMin()+"℃");
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
}
