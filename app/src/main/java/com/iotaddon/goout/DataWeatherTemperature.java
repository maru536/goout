package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherTemperature {
    private double tc;
    private double tmax;
    private double tmin;
    private boolean selectVoice;

    public DataWeatherTemperature(double tc, double tmax, double tmin) {
        this.tc = tc;
        this.tmax = tmax;
        this.tmin = tmin;
        this.selectVoice = false;
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
