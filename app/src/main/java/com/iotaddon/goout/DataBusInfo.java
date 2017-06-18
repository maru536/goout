package com.iotaddon.goout;

/**
 * Created by junhan on 2017-06-18.
 */

public class DataBusInfo {
    private int busRouteId;
    private String rtNm;

    public DataBusInfo(int busRouteId, String rtNm) {
        this.busRouteId = busRouteId;
        this.rtNm = rtNm;
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
