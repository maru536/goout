package com.iotaddon.goout;

/**
 * Created by junhan on 2017-06-18.
 */

public class DataBusInfo {
    private int busRouteId;
    private String rtNm;
    private String arrmsg1,arrmsg2;
    private String stId;
    private String stationNm1, stationNm2;
    private boolean selectVoice = false;
    public final static int MAX_VALUE_TRANSPORTATION_BUS = 100;
    private boolean isHigher = true;
    private int warningValue = MAX_VALUE_TRANSPORTATION_BUS;
    public final static String UNIT_TRANSPORTATION_BUS = "분";

    public DataBusInfo(String stId, int busRouteId, String rtNm, String arrmsg1, String arrmsg2, String stationNm1, String stationNm2) {
        this.stId = stId;
        this.busRouteId = busRouteId;
        this.rtNm = rtNm;
        this.arrmsg1 = arrmsg1;
        this.arrmsg2 = arrmsg2;
        this.stationNm1 = stationNm1;
        this.stationNm2 = stationNm2;
    }

    public boolean isSelectVoice() {
        return selectVoice;
    }

    public void setSelectVoice(boolean selectVoice) {
        this.selectVoice = selectVoice;
    }

    public boolean isHigher() {
        return isHigher;
    }

    public void setHigher(boolean higher) {
        isHigher = higher;
    }

    public int getWarningValue() {
        return warningValue;
    }

    public void setWarningValue(int warningValue) {
        this.warningValue = warningValue;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getArrmsg1() {
        return arrmsg1;
    }

    public void setArrmsg1(String arrmsg1) {
        this.arrmsg1 = arrmsg1;
    }

    public String getArrmsg2() {
        return arrmsg2;
    }

    public void setArrmsg2(String arrmsg2) {
        this.arrmsg2 = arrmsg2;
    }

    public String getStationNm1() {
        return stationNm1;
    }

    public void setStationNm1(String stationNm1) {
        this.stationNm1 = stationNm1;
    }

    public String getStationNm2() {
        return stationNm2;
    }

    public void setStationNm2(String stationNm2) {
        this.stationNm2 = stationNm2;
    }

    public int getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(int busRouteId) {
        this.busRouteId = busRouteId;
    }

    public String getRtNm() {
        return rtNm;
    }

    public void setRtNm(String rtNm) {
        this.rtNm = rtNm;
    }
}
