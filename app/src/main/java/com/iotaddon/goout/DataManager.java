package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-08.
 */
public class DataManager {

    public static final int INDEX_TEMP = 1;
    public static final int INDEX_DUST = 2;
    public static final int INDEX_WET = 3;
    public static final int INDEX_DISCOMFORT = 4;
    public static final int INDEX_WEATHER = 5;
    public static final int INDEX_BODYTEMP = 6;

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private boolean selectedWeather[] = new boolean[7];
    private int selectedTransportation = 0;
    private String savedMemo = "";
    private InfoUserAddress userAddress= new InfoUserAddress(0,0,"");
    private boolean outAlarm = false;

    private DataManager() {
    }

    public void setUserAddress(double x, double y, String name){
        userAddress.setX(x);
        userAddress.setY(y);
        userAddress.setName(name);
    }

    public InfoUserAddress getUserAddress() {
        return userAddress;
    }

    public void setSelectedWeather(int idx, boolean value){
        selectedWeather[idx] = value;
    }

    public boolean getSelectedWeather(int idx){
        return selectedWeather[idx];
    }

    public int getSelectedTransportation() {
        return selectedTransportation;
    }

    public void setSelectedTransportation(int selectedTransportation) {
        this.selectedTransportation = selectedTransportation;
    }

    public String getSavedMemo() {
        return savedMemo;
    }

    public void setSavedMemo(String savedMemo) {
        this.savedMemo = savedMemo;
    }

    public boolean isOutAlarm() {
        return outAlarm;
    }

    public void setOutAlarm(boolean outAlarm) {
        this.outAlarm = outAlarm;
    }
}
