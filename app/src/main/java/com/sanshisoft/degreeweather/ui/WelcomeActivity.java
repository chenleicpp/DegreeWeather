package com.sanshisoft.degreeweather.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenleicpp on 2014/12/8.
 */
public class WelcomeActivity extends Activity {

    public static final String CITY_DB_NAME = "city.db";

    private static final int DB_COPY_SUCCESS = 1;
    private static final int DB_COPY_FAILED = 2;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DB_COPY_SUCCESS:
                    LogUtil.d("copy db file succeed!!!");
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                copyDbData();
            }
        }).start();
    }

    private void copyDbData(){
        Message msg = mHandler.obtainMessage();
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "com.sanshisoft.degreeweather" + File.separator
                + CITY_DB_NAME;
        File file = new File(path);
        if (!file.exists()){
            LogUtil.d("db file not found!");
            try {
                InputStream is = getAssets().open(CITY_DB_NAME);
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
        }
    }
}
