package com.sanshisoft.degreeweather.model;

/**
 * Created by chenleicpp on 2014/12/15.
 */
public class Aqi {
    private String city;
    private String city_id;
    private String pub_time;
    private String aqi;
    private String pm25;
    private String pm10;

    public Aqi(){
        super();
    }

    public Aqi(String city, String city_id, String pub_time, String aqi, String pm25, String pm10) {
        this.city = city;
        this.city_id = city_id;
        this.pub_time = pub_time;
        this.aqi = aqi;
        this.pm25 = pm25;
        this.pm10 = pm10;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getPub_time() {
        return pub_time;
    }

    public void setPub_time(String pub_time) {
        this.pub_time = pub_time;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    @Override
    public String toString() {
        return "Aqi{" +
                "city='" + city + '\'' +
                ", city_id='" + city_id + '\'' +
                ", pub_time='" + pub_time + '\'' +
                ", aqi='" + aqi + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", pm10='" + pm10 + '\'' +
                '}';
    }
}
