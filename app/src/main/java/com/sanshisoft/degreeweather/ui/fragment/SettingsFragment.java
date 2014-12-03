package com.sanshisoft.degreeweather.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.sanshisoft.degreeweather.R;

/**
 * Created by chenleicpp on 2014/12/3.
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
