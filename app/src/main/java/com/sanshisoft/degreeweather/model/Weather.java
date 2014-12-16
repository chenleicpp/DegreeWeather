package com.sanshisoft.degreeweather.model;

/**
 * Created by chenleicpp on 2014/12/15.
 */
public class Weather {
    private Forecast forecast;
    private Aqi aqi;
    private Yestoday yestoday;
    private Realtime realtime;

    public Weather() {
        super();
    }

    public Weather(Forecast forecast, Aqi aqi, Yestoday yestoday, Realtime realtime) {
        this.forecast = forecast;
        this.aqi = aqi;
        this.yestoday = yestoday;
        this.realtime = realtime;
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

    public Realtime getRealtime() {
        return realtime;
    }

    public void setRealtime(Realtime realtime) {
        this.realtime = realtime;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "forecast=" + forecast +
                ", aqi=" + aqi +
                ", yestoday=" + yestoday +
                ", realtime=" + realtime +
                '}';
    }
}
