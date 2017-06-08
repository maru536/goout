package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-08.
 */
public class DataManager {

    public static final int WEAHTER_ITEM_NUM_MAXIMUM = 6;
    public static final int TYPE_WEATHER_TEMP = 1;
    public static final int TYPE_WEATHER_DUST = 2;
    public static final int TYPE_WEATHER_HUMIDITY = 3;
    public static final int TYPE_WEATHER_SKY = 4;
    public static final int TYPE_WEATHER_WIND = 5;
    public static final int TYPE_WEATHER_PRECIPITATION = 6;
    public static final int TYPE_NONE = 0;


    public static final int TYPE_TRANSPORTATION_NONE = 10;
    public static final int TYPE_TRANSPORTATION_BUS = 11;
    public static final int TYPE_TRANSPORTATION_SUBWAY = 12;

    public static final int TYPE_LED_1 = 0;
    public static final int TYPE_LED_2 = 1;
    public static final int TYPE_LED_3 = 2;


    public static final int TYPE_MEMO = 20;

    public static final int MAX_TRANSPORTATION_VALUE = 30;
    public static final String UNIT_TRANSPORTATION = "분";

    //temp data type

    private static DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private boolean selectedWeather[] = new boolean[7];
    private int selectedLEDs[] = new int[3];
    private int selectedTransportation = 0;
    private String savedMemo = "";
    private InfoUserAddress userAddress= new InfoUserAddress(0,0,"");
    private boolean outAlarm = false;
    private DataWeather dataWeather;

    private DataManager() {
        dataWeather = new DataWeather();
        selectedLEDs[0] = TYPE_NONE;
        selectedLEDs[1] = TYPE_NONE;
        selectedLEDs[2] = TYPE_NONE;
    }

    public void setSelectedLed(int led, int type){
        selectedLEDs[led] = type;
    }

    public int getSelectedLed(int led){
        return selectedLEDs[led];
    }

    public DataWeather getDataWeather() {
        return dataWeather;
    }

    public void setUserAddress(double latitude, double longitude, String name){
        userAddress.setLatitude(latitude);
        userAddress.setLongitude(longitude);
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
