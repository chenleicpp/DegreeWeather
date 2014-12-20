package com.sanshisoft.degreeweather.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.db.dao.TWeatherDao;
import com.sanshisoft.degreeweather.net.RequestManager;

/**
 * Created by chenleicpp on 2014/12/16.
 */
public class BaseFragment extends Fragment{

    public TWeatherDao dao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new TWeatherDao(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }

    protected void executeRequest(Request request) {
        RequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(App.getContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        };
    }

    public void startProgress(final SwipeRefreshLayout swipeLayout){
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });
    }

    public void stopProgress(final SwipeRefreshLayout swipeLayout){
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
