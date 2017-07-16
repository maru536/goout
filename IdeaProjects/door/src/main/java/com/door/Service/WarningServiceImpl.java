package com.door.Service;

import com.door.Config.Constants;
import com.door.Database.Entity.UserConfig;
import com.door.Database.Repository.UserConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WarningServiceImpl implements WarningService {
    @Autowired
    UserConfigRepository userConfigRepository;
    @Autowired
    WeatherService weatherService;
    @Autowired
    BusService busService;

    public boolean isWarningSky (UserConfig userConfig) {
        String skyValue = weatherService.getSkyValue();

        int skyIntValue = 0;

        if(skyValue.indexOf("맑음") >= 0){
            skyIntValue = 0;
        } else if(skyValue.indexOf("흐림") >= 0) {
            skyIntValue = 1;
        } else if(skyValue.indexOf("비") >= 0) {
            skyIntValue = 3;
        }

        int limitValue = Integer.parseInt(userConfig.getSkyLimit());

        if(skyIntValue >= limitValue) return true;
        else return false;
    }

    public boolean isWarningTemp (UserConfig userConfig){

        boolean isHigher = userConfig.getIsHigher_Temp() ;
        double limitValue = Double.valueOf(userConfig.getTempLimit());
        double nowValue = Double.valueOf(weatherService.getTempValue());

        if(isHigher) {
            if(nowValue >= limitValue) return true;
            else return false;
        } else {
            if(nowValue <= limitValue) return true;
            else return false;
        }
    }

    public boolean isWarningHumidity (UserConfig userConfig){

        boolean isHigher = userConfig.getIsHigher_Humidity() ;
        double limitValue = Double.valueOf(userConfig.getHumidityLimit());
        System.out.println("humidity Value: = " + weatherService.getHumidityValue());
        double nowValue = Double.valueOf(weatherService.getHumidityValue());

        if(isHigher) {
            if(nowValue >= limitValue) return true;
            else return false;
        } else {
            if(nowValue <= limitValue) return true;
            else return false;
        }
    }

    public boolean isWarningRain (UserConfig userConfig){

        boolean isHigher = userConfig.getIsHigher_rainLimit() ;
        double limitValue = Double.valueOf(userConfig.getRainLimit());
        double nowValue = Double.valueOf(weatherService.getRainValue());

        if(isHigher) {
            if(nowValue >= limitValue) return true;
            else return false;
        } else {
            if(nowValue <= limitValue) return true;
            else return false;
        }
    }

    public boolean isWarningDust (UserConfig userConfig){

        boolean isHigher = userConfig.getIsHigher_dustLimit() ;
        double limitValue = Double.valueOf(userConfig.getDustLimit());
        double nowValue = Double.valueOf(weatherService.getDustValue());

        if(isHigher) {
            if(nowValue >= limitValue) return true;
            else return false;
        } else {
            if(nowValue <= limitValue) return true;
            else return false;
        }
    }

    public boolean isWarningBus (UserConfig userConfig) throws UnsupportedEncodingException{

        boolean isHigher = userConfig.getIsHigher_transportLimit() ;
        int limitValue = Integer.parseInt(userConfig.getTransportLimit());

        String regexp = "[0-9]+";
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(busService.getArrivalTime(userConfig.getStId(), userConfig.getRtNm()));

        int nowValue = 0;
        if(matcher.find()) {
            nowValue = Integer.parseInt ( matcher.group() ) ;
        }

        if(isHigher) {
            if(nowValue >= limitValue) return true;
            else return false;
        } else {
            if(nowValue <= limitValue) return true;
            else return false;
        }
    }

    public boolean isLEDOn (int info, UserConfig userConfig) throws UnsupportedEncodingException{
        switch(info){
            case Constants.ENABLE_NONE:
                return false;
            case Constants.ENABLE_WEATHER_TEMP:
                return isWarningTemp(userConfig);
            case Constants.ENABLE_WEATHER_DUST:
                return isWarningDust(userConfig);
            case Constants.ENABLE_WEATHER_HUMIDITY:
                return isWarningHumidity(userConfig);
            case Constants.ENABLE_TRANSPORTATION_BUS:
                return isWarningBus(userConfig);
            default:
                return false;
        }

    }
}
