package com.sanshisoft.degreeweather.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by chenleicpp on 2014/12/26.
 */
public class BaseActivity extends ActionBarActivity {
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BaseActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BaseActivity");
        MobclickAgent.onPause(this);
    }
}
