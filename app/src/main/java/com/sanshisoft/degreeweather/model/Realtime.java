package com.sanshisoft.degreeweather.model;

/**
 * Created by chenleicpp on 2014/12/16.
 */
public class Realtime {

    private String cityid;
    private String temp;
    private String time;
    private String weather;

    public Realtime(){
        super();
    }

    public Realtime(String cityid, String temp, String time, String weather) {
        this.cityid = cityid;
        this.temp = temp;
        this.time = time;
        this.weather = weather;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
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
        return "Realtime{" +
                "cityid='" + cityid + '\'' +
                ", temp='" + temp + '\'' +
                ", time='" + time + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }
}
