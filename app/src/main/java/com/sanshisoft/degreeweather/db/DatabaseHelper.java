package com.sanshisoft.degreeweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sanshisoft.degreeweather.model.TWeather;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenleicpp on 2014/12/17.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DB_NAME = "degreeweather.db";

    private Map<String,Dao> daos = new HashMap<String,Dao>();

    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, TWeather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        try{
            TableUtils.dropTable(connectionSource, TWeather.class,true);
            onCreate(sqLiteDatabase,connectionSource);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static DatabaseHelper mInstance;

    public static synchronized DatabaseHelper getHelper(Context context)
    {
        context = context.getApplicationContext();
        if (mInstance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (mInstance == null)
                    mInstance = new DatabaseHelper(context);
            }
        }

        return mInstance;
    }

    public synchronized Dao getDao(Class clazz) throws SQLException
    {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className))
        {
            dao = daos.get(className);
        }
        if (dao == null)
        {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        for (String key : daos.keySet())
        {
            Dao dao = daos.get(key);
            dao = null;
        }
    }
}
