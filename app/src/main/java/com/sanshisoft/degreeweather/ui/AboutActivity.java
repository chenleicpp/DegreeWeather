package com.sanshisoft.degreeweather.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.ui.fragment.SettingsFragment;
import com.sanshisoft.degreeweather.util.Utils;

/**
 * Created by chenleicpp on 2014/12/3.
 */
public class AboutActivity extends ActionBarActivity {

    private TextView mTvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar mToolBar = (Toolbar)findViewById(R.id.toolbar);
        if (mToolBar != null) setSupportActionBar(mToolBar);
        //getSupportAction的setTitle解决设置无效问题
        getSupportActionBar().setTitle(getResources().getString(R.string.pre_about));
        mToolBar.setNavigationIcon(R.drawable.ic_back);
        mToolBar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTvVersion = (TextView)findViewById(R.id.tv_version);
        mTvVersion.setText(Utils.getVersionName());
    }
}
