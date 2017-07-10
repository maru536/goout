package com.iotaddon.goout;

/**
 * Created by junhan on 2017-06-07.
 */

public class DataWeatherDust {
    private String grade;
    private double value;
    private boolean isHigher = false;
    private boolean selectVoice;
    private int warningValue;
    public final static int MAX_VALUE_WEATHER_DUST = 100;
    public final static String UNIT_WEATHER_DUST = "㎍/㎥";

    public DataWeatherDust(String grade, double value) {
        this.grade = grade;
        this.value = value;
        this.isHigher = true;
        this.selectVoice = false;
        this.warningValue = MAX_VALUE_WEATHER_DUST;

    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getValue() {
        return value;

    }

    public void setValue(double value) {
        this.value = value;
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

    public int getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(int warningValue) {
        this.warningValue = warningValue;
    }
}
