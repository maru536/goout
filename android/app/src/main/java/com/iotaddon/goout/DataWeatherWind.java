package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherWind {
    private double wdir;
    private double wspd;
    private boolean selectVoice;
    private double warningValue;
    private boolean isHigher = false;
    public final static int MAX_VALUE_WEATHER_WIND = 10000;
    public final static String UNIT_WEATHER_WIND = "m/s";

    public double getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(double warningValue) {
        this.warningValue = warningValue;
    }

    public boolean isHigher() {
        return isHigher;
    }

    public void setHigher(boolean higher) {
        isHigher = higher;
    }

    public DataWeatherWind(double wdir, double wspd) {
        this.wdir = wdir;
        this.wspd = wspd;
        this.selectVoice = false;
        this.warningValue = MAX_VALUE_WEATHER_WIND/100;
        this.isHigher = true;
    }

    public boolean isSelectVoice() {
        return selectVoice;
    }

    public void setSelectVoice(boolean selectVoice) {
        this.selectVoice = selectVoice;
    }

    public double getWdir() {
        return wdir;
    }

    public void setWdir(double wdir) {
        this.wdir = wdir;
    }

    public double getWspd() {
        return wspd;
    }

    public void setWspd(double wspd) {
        this.wspd = wspd;
    }
}
