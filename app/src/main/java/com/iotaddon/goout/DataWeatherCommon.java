package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherCommon {
    private String alertYn;
    private String stormYn;

    public DataWeatherCommon(String alertYn, String stormYn) {
        this.alertYn = alertYn;
        this.stormYn = stormYn;
    }

    public String getAlertYn() {
        return alertYn;
    }

    public void setAlertYn(String alertYn) {
        this.alertYn = alertYn;
    }

    public String getStormYn() {
        return stormYn;
    }

    public void setStormYn(String stormYn) {
        this.stormYn = stormYn;
    }
}
