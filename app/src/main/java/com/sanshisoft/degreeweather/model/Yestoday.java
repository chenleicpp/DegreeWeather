package com.sanshisoft.degreeweather.model;

/**
 * Created by chenleicpp on 2014/12/15.
 */
public class Yestoday {
    private String cityCode;
    private String date;
    private String tempMax;
    private String tempMin;

    public Yestoday() {
        super();
    }

    public Yestoday(String cityCode, String date, String tempMax, String tempMin) {
        this.cityCode = cityCode;
        this.date = date;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
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

    @Override
    public String toString() {
        return "Yestoday{" +
                "cityCode='" + cityCode + '\'' +
                ", date='" + date + '\'' +
                ", tempMax='" + tempMax + '\'' +
                ", tempMin='" + tempMin + '\'' +
                '}';
    }
}
