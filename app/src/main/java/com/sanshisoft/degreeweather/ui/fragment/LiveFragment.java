package com.sanshisoft.degreeweather.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanshisoft.degreeweather.R;

/**
 * Created by chenleicpp on 2014/12/2.
 */
public class LiveFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_live, container, false);
        return rootView;
    }
}
