package com.sanshisoft.degreeweather.model;

/**
 * Created by chenleicpp on 2014/12/15.
 */
public class Forecast {
    private String city;
    private String city_en;
    private String cityid;
    private String date_y;
    //today
    private String temp1;
    private String temp2;
    private String temp3;
    private String temp4;
    private String temp5;
    private String weather1;
    private String weather2;
    private String weather3;
    private String weather4;
    private String weather5;

    public Forecast() {
        super();
    }

    public Forecast(String city, String city_en, String cityid, String date_y, String temp1, String temp2, String temp3, String temp4, String temp5, String weather1, String weather2, String weather3, String weather4, String weather5) {
        this.city = city;
        this.city_en = city_en;
        this.cityid = cityid;
        this.date_y = date_y;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.temp3 = temp3;
        this.temp4 = temp4;
        this.temp5 = temp5;
        this.weather1 = weather1;
        this.weather2 = weather2;
        this.weather3 = weather3;
        this.weather4 = weather4;
        this.weather5 = weather5;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity_en() {
        return city_en;
    }

    public void setCity_en(String city_en) {
        this.city_en = city_en;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getDate_y() {
        return date_y;
    }

    public void setDate_y(String date_y) {
        this.date_y = date_y;
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

    @Override
    public String toString() {
        return "Forecast{" +
                "city='" + city + '\'' +
                ", city_en='" + city_en + '\'' +
                ", cityid='" + cityid + '\'' +
                ", date_y='" + date_y + '\'' +
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
                '}';
    }
}
