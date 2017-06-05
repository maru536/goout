package com.iotaddon.goout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

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
}
