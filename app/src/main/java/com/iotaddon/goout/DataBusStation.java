package com.iotaddon.goout;

/**
 * Created by junhan on 2017-06-13.
 */

public class DataBusStation {
    private int districtCd;
    private String regionName;
    private double x;
    private double y;
    private String stationName;
    private String centerYn;
    private String mobileNo;
    private int stationId;

    public DataBusStation(int districtCd, String regionName, double x, double y, String stationName, String centerYn, String mobileNo, int stationId) {
        this.districtCd = districtCd;
        this.regionName = regionName;
        this.x = x;
        this.y = y;
        this.stationName = stationName;
        this.centerYn = centerYn;
        this.mobileNo = mobileNo;
        this.stationId = stationId;
    }

    public DataBusStation(DataBusStation data) {
        this.districtCd = data.getDistrictCd();
        this.regionName = data.getRegionName();
        this.x = data.getX();
        this.y = data.getY();
        this.stationName = data.getStationName();
        this.centerYn = data.getCenterYn();
        this.mobileNo = data.getMobileNo();
        this.stationId = data.getStationId();
    }

    public int getDistrictCd() {
        return districtCd;
    }

    public void setDistrictCd(int districtCd) {
        this.districtCd = districtCd;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCenterYn() {
        return centerYn;
    }

    public void setCenterYn(String centerYn) {
        this.centerYn = centerYn;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }
}
