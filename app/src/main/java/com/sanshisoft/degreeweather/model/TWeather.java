package com.sanshisoft.degreeweather.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by chenleicpp on 2014/12/17.
 */
@DatabaseTable(tableName = "tb_weather")
public class TWeather {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String city;
    @DatabaseField
    private String cityid;
    @DatabaseField
    private String date;
    @DatabaseField
    private String temp1;
    @DatabaseField
    private String temp2;
    @DatabaseField
    private String temp3;
    @DatabaseField
    private String temp4;
    @DatabaseField
    private String temp5;
    @DatabaseField
    private String weather1;
    @DatabaseField
    private String weather2;
    @DatabaseField
    private String weather3;
    @DatabaseField
    private String weather4;
    @DatabaseField
    private String weather5;
    @DatabaseField
    private String ytempMax;
    @DatabaseField
    private String ytempMin;
    @DatabaseField
    private String time;
    @DatabaseField
    private String weather;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp1() {
        return temp1;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }

    public String getTemp3() {
        return temp3;
    }

    public void setTemp3(String temp3) {
        this.temp3 = temp3;
    }

    public String getTemp4() {
        return temp4;
    }

    public void setTemp4(String temp4) {
        this.temp4 = temp4;
    }

    public String getTemp5() {
        return temp5;
    }

    public void setTemp5(String temp5) {
        this.temp5 = temp5;
    }

    public String getWeather1() {
        return weather1;
    }

    public void setWeather1(String weather1) {
        this.weather1 = weather1;
    }

    public String getWeather2() {
        return weather2;
    }

    public void setWeather2(String weather2) {
        this.weather2 = weather2;
    }

    public String getWeather3() {
        return weather3;
    }

    public void setWeather3(String weather3) {
        this.weather3 = weather3;
    }

    public String getWeather4() {
        return weather4;
    }

    public void setWeather4(String weather4) {
        this.weather4 = weather4;
    }

    public String getWeather5() {
        return weather5;
    }

    public void setWeather5(String weather5) {
        this.weather5 = weather5;
    }

    public String getYtempMax() {
        return ytempMax;
    }

    public void setYtempMax(String ytempMax) {
        this.ytempMax = ytempMax;
    }

    public String getYtempMin() {
        return ytempMin;
    }

    public void setYtempMin(String ytempMin) {
        this.ytempMin = ytempMin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "TWeather{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", cityid='" + cityid + '\'' +
                ", date='" + date + '\'' +
                ", temp1='" + temp1 + '\'' +
                ", temp2='" + temp2 + '\'' +
                ", temp3='" + temp3 + '\'' +
                ", temp4='" + temp4 + '\'' +
                ", temp5='" + temp5 + '\'' +
                ", weather1='" + weather1 + '\'' +
                ", weather2='" + weather2 + '\'' +
                ", weather3='" + weather3 + '\'' +
                ", weather4='" + weather4 + '\'' +
                ", weather5='" + weather5 + '\'' +
                ", ytempMax='" + ytempMax + '\'' +
                ", ytempMin='" + ytempMin + '\'' +
                ", time='" + time + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }
}
