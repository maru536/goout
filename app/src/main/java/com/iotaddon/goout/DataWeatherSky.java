package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherSky {
    private String name;
    private String code;
    private boolean selectVoice;
    private int warningValue;
    public final int WARNING_SKY_SUNNY = 0;
    public final int WARNING_SKY_BLUE = 1;
    public final int WARNING_SKY_RAIN_SNOW = 2;

    public int getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(int warningValue) {
        this.warningValue = warningValue;
    }

    public DataWeatherSky(String name, String code) {
        this.name = name;
        this.code = code;
        this.selectVoice = false;
        this.warningValue = WARNING_SKY_RAIN_SNOW;
    }

    public boolean isSelectVoice() {
        return selectVoice;
    }

    public void setSelectVoice(boolean selectVoice) {
        this.selectVoice = selectVoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
