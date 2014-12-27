package com.sanshisoft.degreeweather.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.ui.fragment.SettingsFragment;
import com.sanshisoft.degreeweather.util.Utils;
import com.umeng.update.UmengUpdateAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chenleicpp on 2014/12/3.
 */
public class AboutActivity extends BaseActivity {

    @InjectView(R.id.tv_version)
    TextView mTvVersion;
    @InjectView(R.id.btn_check)
    Button mBtnCheck;
    @InjectView(R.id.btn_evaluation)
    Button mBtnEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ButterKnife.inject(this);

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

        mTvVersion.setText(Utils.getVersionName());
        mBtnEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id="+getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        mBtnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUpdateAgent.update(AboutActivity.this);
            }
        });
    }
}
