package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherPressure {
    private double surface;
    private double sealevel;

    public DataWeatherPressure(double surface, double sealevel) {
        this.surface = surface;
        this.sealevel = sealevel;
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
