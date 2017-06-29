package com.iotaddon.goout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by junhan on 2017-06-04.
 */

public class SelectWeatherIcon {
    public static void setWeatherSkyIcon(ImageView imageView, String code, Context context){
        int codeNum = Integer.valueOf(code.substring(5));
        switch(codeNum){
            case 0:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_38));
                break;
            case 1:
                if(isDay()){
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_01));
                }else{
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_08));
                }
                break;
            case 2:
                if(isDay()){
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_02));
                }else{
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_09));
                }
                break;
            case 3:
                if(isDay()){
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_03));
                }else{
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_10));
                }
                break;
            case 4:
                if(isDay()){
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_12));
                }else{
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_40));
                }
                break;
            case 5:
                if(isDay()){
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_13));
                }else{
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_41));
                }
                break;
            case 6:
                if(isDay()){
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_14));
                }else{
                    imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_42));
                }
                break;
            case 7:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_18));
                break;
            case 8:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_21));
                break;
            case 9:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_32));
                break;
            case 10:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_04));
                break;
            case 11:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_29));
                break;
            case 12:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_26));
                break;
            case 13:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_27));
                break;
            case 14:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_28));
                break;
            default:
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_sky_38));
        }
    }

    public static void setWeatherDustIcon(ImageView imageView, String code, Context context){
        if(code.equals("좋음")){
            imageView.setImageDrawable(context.getDrawable(R.drawable.dust100));
        }else if(code.equals("보통")){
            imageView.setImageDrawable(context.getDrawable(R.drawable.dust75));
        }else if(code.equals("나쁨")){
            imageView.setImageDrawable(context.getDrawable(R.drawable.dust50));
        }else if(code.equals("매우나쁨")){
            imageView.setImageDrawable(context.getDrawable(R.drawable.dust0));
        }
    }

    public static boolean isDay(){
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if(calendar.get(Calendar.HOUR_OF_DAY) >= 17){
            return false;
        }else{
            return true;
        }
    }

    public static void setWarningBorder(LinearLayout container, int type, Context context){
        DataManager dataManager = DataManager.getInstance();
        if (type == DataManager.TYPE_WEATHER_HUMIDITY) {
            if(dataManager.getDataWeather().getDataWeatherHumidity().isHigher()){
                if(dataManager.getDataWeather().getDataWeatherHumidity().getWarningValue() < dataManager.getDataWeather().getDataWeatherHumidity().getHumidity()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }else{
                if(dataManager.getDataWeather().getDataWeatherHumidity().getWarningValue() > dataManager.getDataWeather().getDataWeatherHumidity().getHumidity()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }
        } else if (type == DataManager.TYPE_WEATHER_WIND) {
            if(dataManager.getDataWeather().getDataWeatherWind().isHigher()){
                if(dataManager.getDataWeather().getDataWeatherWind().getWarningValue() < dataManager.getDataWeather().getDataWeatherWind().getWspd()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }else{
                if(dataManager.getDataWeather().getDataWeatherWind().getWarningValue() > dataManager.getDataWeather().getDataWeatherWind().getWspd()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }
        } else if (type == DataManager.TYPE_WEATHER_DUST) {
            if(dataManager.getDataWeather().getDataWeatherDust().isHigher()){
                if(dataManager.getDataWeather().getDataWeatherDust().getWarningValue() < dataManager.getDataWeather().getDataWeatherDust().getValue()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }else{
                if(dataManager.getDataWeather().getDataWeatherDust().getWarningValue() > dataManager.getDataWeather().getDataWeatherDust().getValue()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }
        } else if (type == DataManager.TYPE_WEATHER_TEMP) {
            if(dataManager.getDataWeather().getDataWeatherTemperature().isHigher()){
                if(dataManager.getDataWeather().getDataWeatherTemperature().getWarningValue() < dataManager.getDataWeather().getDataWeatherTemperature().getTc()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }else{
                if(dataManager.getDataWeather().getDataWeatherTemperature().getWarningValue() > dataManager.getDataWeather().getDataWeatherTemperature().getTc()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }
        } else if (type == DataManager.TYPE_WEATHER_SKY) {

        } else if (type == DataManager.TYPE_WEATHER_PRECIPITATION) {
            if(dataManager.getDataWeather().getDataWeatherPrecipitation().isHigher()){
                if(dataManager.getDataWeather().getDataWeatherPrecipitation().getWarningValue() < dataManager.getDataWeather().getDataWeatherPrecipitation().getSinceOntime()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }else{
                if(dataManager.getDataWeather().getDataWeatherPrecipitation().getWarningValue() > dataManager.getDataWeather().getDataWeatherPrecipitation().getSinceOntime()){
                    container.setBackground(context.getDrawable(R.drawable.border_red));
                }else{
                    container.setBackgroundColor(context.getColor(android.R.color.transparent));
                }
            }
        } else if (type == DataManager.TYPE_TRANSPORTATION_BUS) {

        } else if (type == DataManager.TYPE_TRANSPORTATION_SUBWAY) {

        }else{
            container.setBackgroundColor(context.getColor(android.R.color.transparent));
        }
    }
}
