package com.sanshisoft.degreeweather.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.db.CityDB;
import com.sanshisoft.degreeweather.util.LogUtil;
import com.sanshisoft.degreeweather.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenleicpp on 2014/12/8.
 */
public class WelcomeActivity extends Activity implements AMapLocationListener {

    private LocationManagerProxy mLocationManagerProxy;

    private static final int DB_COPY_SUCCESS = 1;
    private static final int DB_COPY_FAILED = 2;

    public static final String LOCATION_CITY = "location_city";

    private long beginTime;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DB_COPY_SUCCESS:
                    LogUtil.d("copy db file succeed!!!");
                    //3.高德地图定位
                    requestLocation();
                    break;
                case DB_COPY_FAILED:
                    Toast.makeText(WelcomeActivity.this,"copy db file failed!",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        beginTime = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //1.判断网络
        if (Utils.isNetVisible(this)){
            //2.复制db文件
            new Thread(new Runnable() {
                @Override
                public void run() {
                    copyDbData();
                }
            }).start();
        }else {
            openNoNetDialog();
        }
    }

    private void copyDbData(){
        Message msg = mHandler.obtainMessage();
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "com.sanshisoft.degreeweather" + File.separator
                + CityDB.CITY_DB_NAME;
        File file = new File(path);
        if (!file.exists()){
            LogUtil.d("db file not found!");
            try {
                InputStream is = getAssets().open(CityDB.CITY_DB_NAME);
                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
                msg.what = DB_COPY_SUCCESS;
                mHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
                msg.what = DB_COPY_FAILED;
                msg.obj = e;
                mHandler.sendMessage(msg);
            }
        }else {
            requestLocation();
        }
    }

    private void openNoNetDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.net_dialog_title).setMessage(R.string.net_dialog_message);
        builder.setPositiveButton(R.string.dialog_ok,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                try{
                    String sdkVersion = android.os.Build.VERSION.SDK;
                    if (Integer.valueOf(sdkVersion) > 10 ){
                        intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                    }else {
                        intent = new Intent();
                        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                        intent.setComponent(comp);
                        intent.setAction("android.intent.action.VIEW");
                    }
                    startActivity(intent);
                }catch (Exception e){
                    LogUtil.d("open network settings failed");
                    e.printStackTrace();
                }
            }
        }).setNegativeButton(R.string.dialog_cancel,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        }).show();
    }

    private void requestLocation(){
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork,60*1000,15,this);
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        LogUtil.d("error:"+aMapLocation.getAMapException().getErrorMessage());
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            //4.请求天气
            long intervalTime = System.currentTimeMillis() - beginTime;
            if (intervalTime < 2 * 1000) {
                try {
                    Thread.sleep(2000 - intervalTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this,MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(LOCATION_CITY,aMapLocation.getCity());
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }else {
            //
            Toast.makeText(WelcomeActivity.this,R.string.gps_error,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManagerProxy.removeUpdates(this);
        mLocationManagerProxy.destroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
