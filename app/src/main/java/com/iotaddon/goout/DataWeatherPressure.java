package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherPressure {
    private double surface;
    private double sealevel;
    private boolean selectVoice;

    public DataWeatherPressure(double surface, double sealevel) {
        this.surface = surface;
        this.sealevel = sealevel;
        this.selectVoice = false;
    }

    public boolean isSelectVoice() {
        return selectVoice;
    }

    public void setSelectVoice(boolean selectVoice) {
        this.selectVoice = selectVoice;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public double getSealevel() {
        return sealevel;
    }

    public void setSealevel(double sealevel) {
        this.sealevel = sealevel;
    }
}
