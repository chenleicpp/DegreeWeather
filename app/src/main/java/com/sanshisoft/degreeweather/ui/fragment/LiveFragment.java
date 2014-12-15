package com.sanshisoft.degreeweather.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.model.Weather;
import com.sanshisoft.degreeweather.net.GsonRequest;
import com.sanshisoft.degreeweather.util.LogUtil;
import com.sanshisoft.degreeweather.util.Utils;

/**
 * Created by chenleicpp on 2014/12/2.
 */
public class LiveFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeLayout;

    private RequestQueue mQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mQueue = Volley.newRequestQueue(getActivity());

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
        GsonRequest<Weather> gsonRequest = new GsonRequest<Weather>(url,Weather.class,new Response.Listener<Weather>() {
            @Override
            public void onResponse(Weather response) {
                LogUtil.d(response.toString());
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.d(error.toString());
            }
        });
        mQueue.add(gsonRequest);
        stopProgress();
    }
}
