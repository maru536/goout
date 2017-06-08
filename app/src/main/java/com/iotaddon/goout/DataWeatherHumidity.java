package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherHumidity {
    private double humidity;
    private boolean selectVoice;
    private int warningValue;
    private boolean isHigher = false;
    public final static int MAX_VALUE_WEATHER_HUMIDITY = 100;
    public final static String UNIT_WEATHER_HUMIDITY = "%";

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

    public boolean isSelectVoice() {
        return selectVoice;
    }

    public void setSelectVoice(boolean selectVoice) {
        this.selectVoice = selectVoice;
    }

    public DataWeatherHumidity(double humidity) {
        this.humidity = humidity;
        this.selectVoice = false;
        this.isHigher = true;
        this.warningValue = MAX_VALUE_WEATHER_HUMIDITY;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
