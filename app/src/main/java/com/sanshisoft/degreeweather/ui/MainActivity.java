package com.sanshisoft.degreeweather.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.internal.view.menu.ActionMenuItem;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.AppConfig;
import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.adapter.DrawerAdapter;
import com.sanshisoft.degreeweather.bean.City;
import com.sanshisoft.degreeweather.db.CityDB;
import com.sanshisoft.degreeweather.model.Category;
import com.sanshisoft.degreeweather.model.EventDesc;
import com.sanshisoft.degreeweather.ui.fragment.LiveFragment;
import com.sanshisoft.degreeweather.ui.fragment.TrendsFragment;
import com.sanshisoft.degreeweather.util.LogUtil;
import com.umeng.update.UmengUpdateAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.trinea.android.common.util.PreferencesUtils;
import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivity {

    public static final String LOCATION_CITY = "location_city";
    //主动定位（首次启动）模式为1，被动读取数据库（非首次启动）模式为2
    public static final String TYPE = "type";

    private String[] mDrawerTitles;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<Category> mCategoriesList;
    private DrawerAdapter mAdapter;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ImageView mTodayWeather;

    private String mCityName;
    private int mType;

    public static void launch(Activity activity,String city,int type){
        Intent intent = new Intent(activity,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(LOCATION_CITY,city);
        bundle.putInt(TYPE,type);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolBar = (Toolbar)findViewById(R.id.toolbar);
        if (mToolBar != null) setSupportActionBar(mToolBar);

        mDrawerTitles = getResources().getStringArray(R.array.drawer_titles);
        mDrawerList = (ListView)findViewById(R.id.left_drawer);

        mTitle = mDrawerTitle = getTitle();

        mCategoriesList = new ArrayList<Category>();
        for (int i = 0;i<mDrawerTitles.length;i++){
            mCategoriesList.add(new Category(mDrawerTitles[i]));
        }
        mAdapter = new DrawerAdapter(this,mCategoriesList);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,mToolBar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        LayoutInflater inflater = getLayoutInflater();
        final ViewGroup head = (ViewGroup)inflater.inflate(R.layout.header,mDrawerList,false);
        mTodayWeather = (ImageView)head.findViewById(R.id.today_weather);
        mDrawerList.addHeaderView(head,null,false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) selectItem(1);

        initDB();

        Bundle b = getIntent().getExtras();
        mCityName = b.getString(LOCATION_CITY);
        mType = b.getInt(TYPE);
        switch (mType){
            case 1:
            {
                CityDB db = App.getInstance().getCityDB();
                if (db != null){
                    City city = db.getCity(mCityName);
                    App.getInstance().setCityNumber(city.getNumber());
                    PreferencesUtils.putString(App.getContext(), AppConfig.CITY_NUMBER,city.getNumber());
                }
            }

                 break;
            case 2:
                App.getInstance().setCityNumber(mCityName);
                PreferencesUtils.putString(App.getContext(), AppConfig.CITY_NUMBER,mCityName);
                break;
        }


        EventBus.getDefault().register(this);

        UmengUpdateAgent.update(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return false;
        }else if (id == R.id.action_location){
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean bIsOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_refresh).setVisible(!bIsOpen);
        menu.findItem(R.id.action_location).setVisible(!bIsOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position){
        Fragment f = null;
        switch (position){
            case 0:
                //header
                break;
            case 1:
                f = new LiveFragment();
                break;
            case 2:
                f = new TrendsFragment();
                break;
            case 3:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
        }
        if (f != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content,f).commit();
        }
        mAdapter.setPos(position);
        mDrawerList.setItemChecked(position, true);
        if (position != 3) {
            mDrawerLayout.closeDrawer(mDrawerList);
            setTitle(mDrawerTitles[position - 1]);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private void initDB(){
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "com.sanshisoft.degreeweather" + File.separator
                + CityDB.CITY_DB_NAME;
        CityDB db = new CityDB(getApplicationContext(),path);
        App.getInstance().setCityDB(db);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EventDesc event) {
        LogUtil.d("event----" + event.getDesc());
        if (event.getDesc().equals("晴")){
            mTodayWeather.setImageResource(R.drawable.qing);
        }else if (event.getDesc().contains("多云")){
            mTodayWeather.setImageResource(R.drawable.qingzhuanduoyun);
        }else if (event.getDesc().contains("雨")){
            mTodayWeather.setImageResource(R.drawable.yu);
        }else if (event.getDesc().contains("雪")){
            mTodayWeather.setImageResource(R.drawable.xue);
        }else if (event.getDesc().contains("雾") || event.getDesc().contains("霾")){
            mTodayWeather.setImageResource(R.drawable.wu);
        }else if (event.getDesc().contains("雷")){
            mTodayWeather.setImageResource(R.drawable.leizhenyu);
        }else {
            mTodayWeather.setImageResource(R.drawable.qing);
        }
    }

    private int keyBackClickCount = 0;

    @Override
    protected void onResume() {
        super.onResume();
        keyBackClickCount = 0;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawers();
        }else {
            switch (keyBackClickCount++) {
                case 0:
                    Toast.makeText(this,
                            getResources().getString(R.string.press_again_exit),
                            Toast.LENGTH_SHORT).show();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 3000);
                    break;
                case 1:
                    finish();
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
    }
}
