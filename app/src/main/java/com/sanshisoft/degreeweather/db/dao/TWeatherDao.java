package com.sanshisoft.degreeweather.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.table.TableUtils;
import com.sanshisoft.degreeweather.db.DatabaseHelper;
import com.sanshisoft.degreeweather.model.TWeather;
import com.sanshisoft.degreeweather.model.Weather;

import java.sql.SQLException;
import java.util.concurrent.Callable;

/**
 * Created by chenleicpp on 2014/12/17.
 */
public class TWeatherDao {

    private static final String TABLENAME = "tb_weather";

    private Context context;
    private DatabaseHelper dbHelper;
    private Dao<TWeather,Integer> weatherDao;

    public TWeatherDao(Context context){
        this.context = context;
        try {
            dbHelper = DatabaseHelper.getHelper(context);
            weatherDao = dbHelper.getDao(TWeather.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertNew(final Weather weather) throws SQLException {
        TransactionManager.callInTransaction(dbHelper.getConnectionSource(),new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                TableUtils.clearTable(dbHelper.getConnectionSource(), TWeather.class);
                TWeather tWeather = new TWeather();
                tWeather.setCity(weather.getForecast().getCity());
                tWeather.setCityid(weather.getForecast().getCityid());
                tWeather.setDate(weather.getForecast().getDate_y());
                tWeather.setTemp1(weather.getForecast().getTemp1());
                tWeather.setTemp2(weather.getForecast().getTemp2());
                tWeather.setTemp3(weather.getForecast().getTemp3());
                tWeather.setTemp4(weather.getForecast().getTemp4());
                tWeather.setTemp5(weather.getForecast().getTemp5());
                tWeather.setWeather1(weather.getForecast().getWeather1());
                tWeather.setWeather2(weather.getForecast().getWeather2());
                tWeather.setWeather3(weather.getForecast().getWeather3());
                tWeather.setWeather4(weather.getForecast().getWeather4());
                tWeather.setWeather5(weather.getForecast().getWeather5());
                tWeather.setTime(weather.getRealtime().getTime());
                tWeather.setWeather(weather.getRealtime().getWeather());
                tWeather.setYtempMax(weather.getYestoday().getTempMax());
                tWeather.setYtempMin(weather.getYestoday().getTempMin());
                tWeather.setWeatherStart(weather.getYestoday().getWeatherStart());
                tWeather.setWeatherEnd(weather.getYestoday().getWeatherEnd());
                weatherDao.create(tWeather);
                return null;
            }
        });
    }

    public long getRowCount() {
        long count = -1;
        try {
            count = weatherDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public TWeather getTWeather(){
        TWeather tWeather = null;
        try {
            tWeather = weatherDao.queryForAll().get(0);
        }catch (Exception e){
            e.printStackTrace();
        }
        return tWeather;
    }
}
