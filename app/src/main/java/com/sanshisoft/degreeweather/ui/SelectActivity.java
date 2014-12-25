package com.sanshisoft.degreeweather.ui;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.sanshisoft.degreeweather.App;
import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.adapter.CitiesAdapter;
import com.sanshisoft.degreeweather.adapter.SearchCityAdapter;
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
public class SelectActivity extends ActionBarActivity implements TextWatcher,
        View.OnClickListener {

    private static final String FORMAT = "^[a-z,A-Z].*$";
    private PinnedHeaderListView mListView;
    private BladeView mLetter;
    private CitiesAdapter mAdapter;
    private SearchCityAdapter mSearchCityAdapter;
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

    private EditText mSearchEditText;

    private ImageButton mClearSearchBtn;

    private View mCityContainer;

    private View mSearchContainer;

    private ListView mSearchListView;

    private InputMethodManager mInputMethodManager;

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

        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        initData();
        initView();
    }

    private void initData(){
        mCityDB = App.getInstance().getCityDB();
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
        mSearchEditText = (EditText) findViewById(R.id.search_edit);
        mSearchEditText.addTextChangedListener(this);
        mClearSearchBtn = (ImageButton) findViewById(R.id.ib_clear_text);
        mClearSearchBtn.setOnClickListener(this);

        mCityContainer = findViewById(R.id.city_content_container);
        mSearchContainer = findViewById(R.id.search_content_container);

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
        mListView.setEmptyView(findViewById(R.id.citys_list_empty));
        mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(
                R.layout.phl_list_group_item, mListView, false));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.launch(SelectActivity.this,mAdapter.getItem(i).getName(),1);
            }
        });

        mSearchListView = (ListView) findViewById(R.id.search_list);
        mSearchListView.setEmptyView(findViewById(R.id.search_empty));
        mSearchContainer.setVisibility(View.GONE);
        mSearchListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mInputMethodManager.hideSoftInputFromWindow(
                        mSearchEditText.getWindowToken(), 0);
                return false;
            }
        });

        mSearchListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                MainActivity.launch(SelectActivity.this,mSearchCityAdapter.getItem(position).getName(),1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCityDB != null && mCityDB.isOpen())
            mCityDB.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_clear_text:
                if (!TextUtils.isEmpty(mSearchEditText.getText().toString())) {
                    mSearchEditText.setText("");
                    mInputMethodManager.hideSoftInputFromWindow(
                            mSearchEditText.getWindowToken(), 0);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mSearchCityAdapter = new SearchCityAdapter(SelectActivity.this,
                datas);
        mSearchListView.setAdapter(mSearchCityAdapter);
        mSearchListView.setTextFilterEnabled(true);
        if (datas.size() < 1 || TextUtils.isEmpty(s)) {
            mCityContainer.setVisibility(View.VISIBLE);
            mSearchContainer.setVisibility(View.INVISIBLE);
            mClearSearchBtn.setVisibility(View.GONE);
        } else {
            mClearSearchBtn.setVisibility(View.VISIBLE);
            mCityContainer.setVisibility(View.INVISIBLE);
            mSearchContainer.setVisibility(View.VISIBLE);
            mSearchCityAdapter.getFilter().filter(s);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
