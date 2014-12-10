package com.sanshisoft.degreeweather.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.adapter.CitiesAdapter;
import com.sanshisoft.degreeweather.bean.City;
import com.sanshisoft.degreeweather.db.CityDB;
import com.sanshisoft.degreeweather.util.LogUtil;
import com.sanshisoft.degreeweather.view.BladeView;
import com.sanshisoft.degreeweather.view.PinnedHeaderListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenleicpp on 2014/12/3.
 */
public class SelectActivity extends ActionBarActivity {

    private static final String FORMAT = "^[a-z,A-Z].*$";
    private PinnedHeaderListView mListView;
    private BladeView mLetter;
    private CitiesAdapter mAdapter;
    private List<City> datas;
    // 首字母集
    private List<String> mSections;
    // 根据首字母存放数据
    private Map<String, List<City>> mMap;
    // 首字母位置集
    private List<Integer> mPositions;
    // 首字母对应的位置
    private Map<String, Integer> mIndexer;

    private CityDB mCityDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Toolbar mToolBar = (Toolbar)findViewById(R.id.toolbar);
        if (mToolBar != null) setSupportActionBar(mToolBar);
        //getSupportAction的setTitle解决设置无效问题
        getSupportActionBar().setTitle(getResources().getString(R.string.activity_select));
        mToolBar.setNavigationIcon(R.drawable.ic_back);
        mToolBar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initData();
        initView();
    }

    private void initData(){
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "com.sanshisoft.degreeweather" + File.separator
                + CityDB.CITY_DB_NAME;
        mCityDB = new CityDB(this,path);
        datas = new ArrayList<City>();
        mSections = new ArrayList<String>();
        mMap = new HashMap<String, List<City>>();
        mPositions = new ArrayList<Integer>();
        mIndexer = new HashMap<String, Integer>();
        datas = mCityDB.getAllCity();
        for (City city : datas) {
            String firstName = city.getPy().substring(0, 1).toUpperCase();// 第一个字拼音的第一个字母
            if (firstName.matches(FORMAT)) {
                if (mSections.contains(firstName)) {
                    mMap.get(firstName).add(city);
                } else {
                    mSections.add(firstName);
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put(firstName, list);
                }
            } else {
                if (mSections.contains("#")) {
                    mMap.get("#").add(city);
                } else {
                    mSections.add("#");
                    List<City> list = new ArrayList<City>();
                    list.add(city);
                    mMap.put("#", list);
                }
            }
        }
        Collections.sort(mSections);// 按照字母重新排序
        int position = 0;
        for (int i = 0; i < mSections.size(); i++) {
            mIndexer.put(mSections.get(i), position);// 存入map中，key为首字母字符串，value为首字母在listview中位置
            mPositions.add(position);// 首字母在listview中位置，存入list中
            position += mMap.get(mSections.get(i)).size();// 计算下一个首字母在listview的位置
        }
    }

    private void initView(){
        mListView = (PinnedHeaderListView) findViewById(R.id.cities_display);
        mLetter = (BladeView) findViewById(R.id.cities_myletterlistview);
        mLetter.setOnItemClickListener(new BladeView.OnItemClickListener() {

            @Override
            public void onItemClick(String s) {
                if (mIndexer.get(s) != null) {
                    mListView.setSelection(mIndexer.get(s));
                }
            }
        });
        mAdapter = new CitiesAdapter(this, datas, mMap, mSections, mPositions);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mAdapter);
        mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(
                R.layout.phl_list_group_item, mListView, false));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LogUtil.d(mAdapter.getItem(i).toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCityDB != null && mCityDB.isOpen())
            mCityDB.close();
    }
}
