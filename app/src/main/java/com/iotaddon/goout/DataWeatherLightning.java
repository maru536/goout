package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeatherLightning {
    private int lightning;
    private boolean selectVoice;

    public boolean isSelectVoice() {
        return selectVoice;
    }

    public void setSelectVoice(boolean selectVoice) {
        this.selectVoice = selectVoice;
    }

    public DataWeatherLightning(int lightning) {
        this.lightning = lightning;
        this.selectVoice = false;
    }

    public int getLightning() {
        return lightning;
    }

    public void setLightning(int lightning) {
        this.lightning = lightning;
    }
}
