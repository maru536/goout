package com.iotaddon.goout;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by junhan on 2017-06-05.
 */

public class FilterSelectedInfo {

    public static final String TITLE_WEATHER_HUMIDITY = "현재습도";
    public static final String TITLE_WEATHER_WIND = "바람정보";
    public static final String TITLE_WEATHER_SKY = "하늘상태";
    public static final String TITLE_WEATHER_DUST = "미세먼지 농도";
    public static final String TITLE_WEATHER_PRECIPITATION = "강수정보";
    public static final String TITLE_WEATHER_TEMP = "현재온도";
    public static final String TITLE_TRANSPORTATION_BUS = "버스 도착 정보";
    public static final String TITLE_TRANSPORTATION_SUBWAY = "지하철 도착 정보";
    public static final String TITLE_MEMO = "알림 정보";

    public static final int FILTER_TYPE_MAIN = 0;
    public static final int FILTER_TYPE_LED = 1;


    private static void setContentTransportation(ArrayList<ItemContents> arrayList, int type) {
        DataManager dataManager = DataManager.getInstance();

        if (dataManager.getSelectedTransportation(dataManager.TYPE_TRANSPORTATION_BUS_SELECTED)) {
            if (type == FILTER_TYPE_LED) {
                for (int led = 0; led < 3; led++) {
                    if (dataManager.getSelectedLed(led) == dataManager.TYPE_TRANSPORTATION_BUS) {
                        return;
                    }
                }
            }
            String busInfo = "";
            busInfo = dataManager.getDataBusInfo().getRtNm() + "버스 " + dataManager.getDataBusInfo().getArrmsg1();
            ItemContents itemContents = new ItemContents(dataManager.TYPE_TRANSPORTATION_BUS, TITLE_TRANSPORTATION_BUS, busInfo);
            arrayList.add(itemContents);
        }
        if (dataManager.getSelectedTransportation(dataManager.TYPE_TRANSPORTATION_SUBWAY_SELECTED)) {
            if (type == FILTER_TYPE_LED) {
                for (int led = 0; led < 3; led++) {
                    if (dataManager.getSelectedLed(led) == dataManager.TYPE_TRANSPORTATION_SUBWAY) {
                        return;
                    }
                }
            }
            ItemContents itemContents = new ItemContents(dataManager.TYPE_TRANSPORTATION_BUS, TITLE_TRANSPORTATION_SUBWAY, "지하철 도착까지 5분 남았습니다.");
            arrayList.add(itemContents);
        }
    }

    private static void setContentMemo(ArrayList<ItemContents> arrayList) {

        DataManager dataManager = DataManager.getInstance();
        if (!dataManager.getSavedMemo().equals("")) {
            ItemContents itemContents = new ItemContents(dataManager.TYPE_MEMO, TITLE_MEMO, dataManager.getSavedMemo());
            arrayList.add(itemContents);
        }
    }

    private static void setContentWeather(ArrayList<ItemContents> arrayList, int type) {

        DataManager dataManager = DataManager.getInstance();

        for (int i = 0; i <= DataManager.WEAHTER_ITEM_NUM_MAXIMUM; i++) {
            if (dataManager.getSelectedWeather(i)) {
                if (i == dataManager.TYPE_WEATHER_HUMIDITY) {
                    boolean selected = false;
                    if (type == FILTER_TYPE_LED) {
                        for (int led = 0; led < 3; led++) {
                            if (dataManager.getSelectedLed(led) == dataManager.TYPE_WEATHER_HUMIDITY)
                                selected = true;
                        }
                    }
                    if (selected)
                        continue;
                    ItemContents itemContents = new ItemContents(i, TITLE_WEATHER_HUMIDITY, dataManager.getDataWeather().getDataWeatherHumidity().getHumidity() + dataManager.getDataWeather().getDataWeatherHumidity().UNIT_WEATHER_HUMIDITY);
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_WIND) {
                    boolean selected = false;
                    if (type == FILTER_TYPE_LED) {
                        for (int led = 0; led < 3; led++) {
                            if (dataManager.getSelectedLed(led) == dataManager.TYPE_WEATHER_WIND)
                                selected = true;
                        }
                    }
                    if (selected)
                        continue;
                    ItemContents itemContents = new ItemContents(i, TITLE_WEATHER_WIND, "풍향 : " + dataManager.getDataWeather().getDataWeatherWind().getWdir() + " / 풍속 : " + dataManager.getDataWeather().getDataWeatherWind().getWspd());
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_DUST) {
                    boolean selected = false;
                    if (type == FILTER_TYPE_LED) {
                        for (int led = 0; led < 3; led++) {
                            if (dataManager.getSelectedLed(led) == dataManager.TYPE_WEATHER_DUST)
                                selected = true;
                        }
                    }
                    if (selected)
                        continue;
                    ItemContents itemContents = new ItemContents(i, TITLE_WEATHER_DUST, dataManager.getDataWeather().getDataWeatherDust().getGrade());
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_TEMP) {
                    boolean selected = false;
                    if (type == FILTER_TYPE_LED) {
                        for (int led = 0; led < 3; led++) {
                            if (dataManager.getSelectedLed(led) == dataManager.TYPE_WEATHER_TEMP)
                                selected = true;
                        }
                    }
                    if (selected)
                        continue;
                    ItemContents itemContents = new ItemContents(i, TITLE_WEATHER_TEMP, dataManager.getDataWeather().getDataWeatherTemperature().getTc() + dataManager.getDataWeather().getDataWeatherTemperature().UNIT_WEATHER_TEMP);
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_SKY) {
                    boolean selected = false;
                    if (type == FILTER_TYPE_LED) {
                        for (int led = 0; led < 3; led++) {
                            if (dataManager.getSelectedLed(led) == dataManager.TYPE_WEATHER_SKY)
                                selected = true;
                        }
                    }
                    if (selected)
                        continue;
                    ItemContents itemContents = new ItemContents(i, TITLE_WEATHER_SKY, dataManager.getDataWeather().getDataWeatherSky().getName());
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_PRECIPITATION) {
                    boolean selected = false;
                    if (type == FILTER_TYPE_LED) {
                        for (int led = 0; led < 3; led++) {
                            if (dataManager.getSelectedLed(led) == dataManager.TYPE_WEATHER_PRECIPITATION)
                                selected = true;
                        }
                    }
                    if (selected)
                        continue;
                    ItemContents itemContents = new ItemContents(i, TITLE_WEATHER_PRECIPITATION, dataManager.getDataWeather().getDataWeatherPrecipitation().getSinceOntime() + dataManager.getDataWeather().getDataWeatherPrecipitation().UNIT_WEATHER_PRECIPITATION);
                    arrayList.add(itemContents);
                }
            }
        }
    }

    public static void setSelectedInfo(ArrayList<ItemContents> arrayList, int type) {
        arrayList.clear();
        if (type == FILTER_TYPE_LED)
            arrayList.add(new ItemContents(DataManager.TYPE_NONE, "선택 안함", "선택 안함"));
        setContentWeather(arrayList, type);
        setContentTransportation(arrayList, type);
        setContentMemo(arrayList);
    }
}
