package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherPrecipitation {
    private double sinceOntime;
    private int type;
    private boolean selectVoice;
    private double warningValue;
    private boolean isHigher = false;
    public final static int MAX_VALUE_WEATHER_PRECIPITATION = 10000;
    public final static String UNIT_WEATHER_PRECIPITATION = "mm";


    public double getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(double warningValue) {
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

    public DataWeatherPrecipitation(double sinceOntime, int type) {
        this.sinceOntime = sinceOntime;
        this.type = type;
        this.selectVoice = false;
        this.isHigher = true;
        this.warningValue = MAX_VALUE_WEATHER_PRECIPITATION/100;
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
