package com.sanshisoft.degreeweather.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sanshisoft.degreeweather.R;
import com.sanshisoft.degreeweather.adapter.DrawerAdapter;
import com.sanshisoft.degreeweather.model.Category;
import com.sanshisoft.degreeweather.ui.fragment.LiveFragment;
import com.sanshisoft.degreeweather.ui.fragment.SettingsFragment;
import com.sanshisoft.degreeweather.ui.fragment.TrendsFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private String[] mDrawerTitles;

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<Category> mCategoriesList;
    private DrawerAdapter mAdapter;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

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
        mDrawerList.addHeaderView(head,null,false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (savedInstanceState == null) selectItem(1);
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
            return true;
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
                f = new SettingsFragment();
                break;
        }
        if (f != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_content,f).commit();
        }
        mAdapter.setPos(position);
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
        setTitle(mDrawerTitles[position - 1]);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
