package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherHumidity {
    private double humidity;
    private boolean selectVoice;

    public boolean isSelectVoice() {
        return selectVoice;
    }

    public void setSelectVoice(boolean selectVoice) {
        this.selectVoice = selectVoice;
    }

    public DataWeatherHumidity(double humidity) {
        this.humidity = humidity;
        this.selectVoice = false;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
