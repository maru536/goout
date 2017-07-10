package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherTemperature {
    private double tc;
    private double tmax;
    private double tmin;
    private boolean selectVoice;
    private int warningValue;
    private boolean isHigher = false;
    public final static int MAX_VALUE_WEATHER_TEMP = 200;
    public final static String UNIT_WEATHER_TEMP = "℃";

    public int getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(int warningValue) {
        this.warningValue = warningValue;
    }

    public boolean isHigher() {
        return isHigher;
    }

    public void setHigher(boolean higher) {
        isHigher = higher;
    }

    public DataWeatherTemperature(double tc, double tmax, double tmin) {
        this.tc = tc;
        this.tmax = tmax;
        this.tmin = tmin;
        this.selectVoice = false;
        this.isHigher = true;
        this.warningValue = MAX_VALUE_WEATHER_TEMP-100;
    }

    public boolean isSelectVoice() {
        return selectVoice;
    }

    public void setSelectVoice(boolean selectVoice) {
        this.selectVoice = selectVoice;
    }

    public double getTc() {
        return tc;
    }

    public void setTc(double tc) {
        this.tc = tc;
    }

    public double getTmax() {
        return tmax;
    }

    public void setTmax(double tmax) {
        this.tmax = tmax;
    }

    public double getTmin() {
        return tmin;
    }

    public void setTmin(double tmin) {
        this.tmin = tmin;
    }
}
