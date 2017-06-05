package com.iotaddon.goout;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by junhan on 2017-06-05.
 */

public class FilterSelectedInfo {
    private static void setContentTransportation(ArrayList<ItemContents> arrayList) {
        DataManager dataManager = DataManager.getInstance();

        if (dataManager.getSelectedTransportation() == dataManager.TYPE_TRANSPORTATION_BUS) {
            ItemContents itemContents = new ItemContents(dataManager.TYPE_TRANSPORTATION_BUS, "버스 도착 정보", "버스 도착까지 5분 남았습니다.");
            arrayList.add(itemContents);
        } else if (dataManager.getSelectedTransportation() == dataManager.TYPE_TRANSPORTATION_SUBWAY) {
            ItemContents itemContents = new ItemContents(dataManager.TYPE_TRANSPORTATION_BUS, "지하철 도착 정보", "지하철 도착까지 5분 남았습니다.");
            arrayList.add(itemContents);
        }
    }

    private static void setContentMemo(ArrayList<ItemContents> arrayList) {

        DataManager dataManager = DataManager.getInstance();
        if (!dataManager.getSavedMemo().equals("")) {
            ItemContents itemContents = new ItemContents(dataManager.TYPE_MEMO, "알림 정보", dataManager.getSavedMemo());
            arrayList.add(itemContents);
        }
    }

    private static void setContentWeather(ArrayList<ItemContents> arrayList) {

        DataManager dataManager = DataManager.getInstance();

        for (int i = 0; i <= DataManager.WEAHTER_ITEM_NUM_MAXIMUM; i++) {
            if (dataManager.getSelectedWeather(i)) {
                if (i == dataManager.TYPE_WEATHER_HUMIDITY) {
                    ItemContents itemContents = new ItemContents(i, "현재습도", dataManager.getDataWeather().getDataWeatherHumidity().getHumidity() + "");
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_WIND) {
                    ItemContents itemContents = new ItemContents(i, "바람정보", "풍향 : " + dataManager.getDataWeather().getDataWeatherWind().getWdir() + " / 풍속 : " + dataManager.getDataWeather().getDataWeatherWind().getWspd());
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_DUST) {
                    ItemContents itemContents = new ItemContents(i, "미세먼지 농도", dataManager.getDataWeather().getDataWeatherHumidity().getHumidity() + "");
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_TEMP) {
                    ItemContents itemContents = new ItemContents(i, "현재온도", dataManager.getDataWeather().getDataWeatherTemperature().getTc() + "");
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_SKY) {
                    ItemContents itemContents = new ItemContents(i, "하늘상태", dataManager.getDataWeather().getDataWeatherSky().getName());
                    arrayList.add(itemContents);
                } else if (i == dataManager.TYPE_WEATHER_PRECIPITATION) {
                    ItemContents itemContents = new ItemContents(i, "강수정보", dataManager.getDataWeather().getDataWeatherPrecipitation().getSinceOntime() + "");
                    arrayList.add(itemContents);
                }
            }
        }
    }

    public static void setSelectedInfo(ArrayList<ItemContents> arrayList){
        arrayList.clear();
        setContentWeather(arrayList);
        setContentTransportation(arrayList);
        setContentMemo(arrayList);
    }
}
