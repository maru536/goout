package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherPrecipitation {
    private double sinceOntime;
    private int type;

    public DataWeatherPrecipitation(double sinceOntime, int type) {
        this.sinceOntime = sinceOntime;
        this.type = type;
    }

    public double getSinceOntime() {
        return sinceOntime;
    }

    public void setSinceOntime(double sinceOntime) {
        this.sinceOntime = sinceOntime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
