package com.sanshisoft.degreeweather.model;

/**
 * Created by chenleicpp on 2014/12/15.
 */
public class Yestoday {
    private String cityCode;
    private String date;
    private String tempMax;
    private String tempMin;
    private String weatherStart;
    private String weatherEnd;

    public Yestoday() {
        super();
    }

    public Yestoday(String cityCode, String date, String tempMax, String tempMin, String weatherStart, String weatherEnd) {
        this.cityCode = cityCode;
        this.date = date;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
        this.weatherStart = weatherStart;
        this.weatherEnd = weatherEnd;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getWeatherStart() {
        return weatherStart;
    }

    public void setWeatherStart(String weatherStart) {
        this.weatherStart = weatherStart;
    }

    public String getWeatherEnd() {
        return weatherEnd;
    }

    public void setWeatherEnd(String weatherEnd) {
        this.weatherEnd = weatherEnd;
    }

    @Override
    public String toString() {
        return "Yestoday{" +
                "cityCode='" + cityCode + '\'' +
                ", date='" + date + '\'' +
                ", tempMax='" + tempMax + '\'' +
                ", tempMin='" + tempMin + '\'' +
                ", weatherStart='" + weatherStart + '\'' +
                ", weatherEnd='" + weatherEnd + '\'' +
                '}';
    }
}
