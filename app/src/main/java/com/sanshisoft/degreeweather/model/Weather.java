package com.sanshisoft.degreeweather.model;

/**
 * Created by chenleicpp on 2014/12/15.
 */
public class Weather {
    private Forecast forecast;
    private Aqi aqi;
    private Yestoday yestoday;

    public Weather() {
        super();
    }

    public Weather(Forecast forecast, Aqi aqi, Yestoday yestoday) {
        this.forecast = forecast;
        this.aqi = aqi;
        this.yestoday = yestoday;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public Aqi getAqi() {
        return aqi;
    }

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }

    public Yestoday getYestoday() {
        return yestoday;
    }

    public void setYestoday(Yestoday yestoday) {
        this.yestoday = yestoday;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "forecast=" + forecast +
                ", aqi=" + aqi +
                ", yestoday=" + yestoday +
                '}';
    }
}
