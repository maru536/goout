package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherRain {
    private double last6hour;
    private double last12hour;
    private double last24hour;
    private double sinceMidnight;
    private double last10min;
    private double last15min;
    private double last30min;
    private double last1hour;
    private double sinceOntime;
    private boolean selectVoice;

    public boolean isSelectVoice() {
        return selectVoice;
    }

    public void setSelectVoice(boolean selectVoice) {
        this.selectVoice = selectVoice;
    }

    public DataWeatherRain(double last6hour, double last12hour, double last24hour, double sinceMidnight, double last10min, double last15min, double last30min, double last1hour, double sinceOntime) {
        this.last6hour = last6hour;
        this.last12hour = last12hour;
        this.last24hour = last24hour;
        this.sinceMidnight = sinceMidnight;
        this.last10min = last10min;
        this.last15min = last15min;
        this.last30min = last30min;
        this.last1hour = last1hour;
        this.sinceOntime = sinceOntime;
        this.selectVoice = false;
    }

    public double getLast6hour() {
        return last6hour;
    }

    public void setLast6hour(double last6hour) {
        this.last6hour = last6hour;
    }

    public double getLast12hour() {
        return last12hour;
    }

    public void setLast12hour(double last12hour) {
        this.last12hour = last12hour;
    }

    public double getLast24hour() {
        return last24hour;
    }

    public void setLast24hour(double last24hour) {
        this.last24hour = last24hour;
    }

    public double getSinceMidnight() {
        return sinceMidnight;
    }

    public void setSinceMidnight(double sinceMidnight) {
        this.sinceMidnight = sinceMidnight;
    }

    public double getLast10min() {
        return last10min;
    }

    public void setLast10min(double last10min) {
        this.last10min = last10min;
    }

    public double getLast15min() {
        return last15min;
    }

    public void setLast15min(double last15min) {
        this.last15min = last15min;
    }

    public double getLast30min() {
        return last30min;
    }

    public void setLast30min(double last30min) {
        this.last30min = last30min;
    }

    public double getLast1hour() {
        return last1hour;
    }

    public void setLast1hour(double last1hour) {
        this.last1hour = last1hour;
    }

    public double getSinceOntime() {
        return sinceOntime;
    }

    public void setSinceOntime(double sinceOntime) {
        this.sinceOntime = sinceOntime;
    }
}
