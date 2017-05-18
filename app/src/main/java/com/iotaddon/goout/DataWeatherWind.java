package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherWind {
    private double wdir;
    private double wspd;

    public DataWeatherWind(double wdir, double wspd) {
        this.wdir = wdir;
        this.wspd = wspd;
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
