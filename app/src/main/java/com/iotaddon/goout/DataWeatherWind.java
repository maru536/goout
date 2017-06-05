package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherWind {
    private double wdir;
    private double wspd;
    private boolean selectVoice;

    public DataWeatherWind(double wdir, double wspd) {
        this.wdir = wdir;
        this.wspd = wspd;
        this.selectVoice = false;
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
