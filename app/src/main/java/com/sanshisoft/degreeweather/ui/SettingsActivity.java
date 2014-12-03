package com.sanshisoft.degreeweather.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.ui.fragment.SettingsFragment;

/**
 * Created by chenleicpp on 2014/12/3.
 */
public class SettingsActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar mToolBar = (Toolbar)findViewById(R.id.toolbar);
        if (mToolBar != null) setSupportActionBar(mToolBar);
        mToolBar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolBar.setTitle(getResources().getStringArray(R.array.drawer_titles)[2]);

        getFragmentManager().beginTransaction().replace(R.id.settings_content,new SettingsFragment()).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
