package com.door.Service;

import com.door.Config.Constants;
import com.door.Database.Entity.UserConfig;
import com.door.Database.Repository.UserConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

/**
 * Created by wj on 17. 7. 1.
 */
@Service
public class SentenceServiceImpl implements SentenceService{

    @Autowired
    UserConfigRepository userConfigRepository;
    @Autowired
    WeatherService weatherService;
    @Autowired
    BusService busService;
    @Autowired
    UserConfigService userConfigService;


    public String getSentence(String deviceId) throws UnsupportedEncodingException {

        UserConfig userConfig;
        if (userConfigRepository.exists(deviceId)) {
            userConfig = userConfigRepository.getOne(deviceId);
        } else {
            return "no Device in here: " + deviceId;
        }

        if(userConfigService.isHavingNull(userConfig)) {
            return "it has null configs.";
        }

        weatherService.setWeatherData(userConfig.getLatitude(), userConfig.getLongitude());
        weatherService.setDustData(userConfig.getLatitude(), userConfig.getLongitude());
        System.out.println(weatherService.getWeatherData());

        String sentence = "";
        if(userConfig.getIsEnableSkyInfo()) {
            sentence += "날씨 ";
            sentence += weatherService.getSkyValue();
            sentence += Constants.SEPARATOR;
        }


        if(userConfig.getIsEnableTempInfo()) {
            sentence += "온도 ";
            sentence += Double.valueOf(weatherService.getTempValue()).intValue();
            sentence += "도";
            sentence += Constants.SEPARATOR;
        }

        if(userConfig.getIsEnableHumidityInfo()){
            sentence += "습도 ";
            sentence += Double.valueOf(weatherService.getHumidityValue()).intValue();
            sentence += Constants.SEPARATOR;
        }

        if(userConfig.getIsEnableDustInfo()){
            sentence += "미세먼지 ";
            sentence += weatherService.getDustGrade();
            sentence += Constants.SEPARATOR;
        }

        if(userConfig.getIsEnableBusInfo()) {
            sentence += "버스 ";
            System.out.println("dd :" + userConfig.getStId() + ", " + userConfig.getRtNm());
            sentence += busService.getArrivalTime(userConfig.getStId(), userConfig.getRtNm());
            sentence += Constants.SEPARATOR;
        }

        if(userConfig.getIsEnableTrainInfo()){

        }

        if(userConfig.getIsEnableMemoInfo()){
            sentence += "메모는 ";
            sentence += userConfig.getMemo();
            sentence += Constants.SEPARATOR;
        }

        if(sentence.endsWith(Constants.SEPARATOR)){
            sentence = sentence.subSequence(0, sentence.length()-1).toString();
        }
        sentence += "입니다";

        return sentence;
    }
}
