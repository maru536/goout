package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherTemperature {
    private double tc;
    private double tmax;
    private double tmin;

    public DataWeatherTemperature(double tc, double tmax, double tmin) {
        this.tc = tc;
        this.tmax = tmax;
        this.tmin = tmin;
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
