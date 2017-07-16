package com.door.Controller;

import com.door.Config.Constants;
import com.door.Database.Entity.UserConfig;
import com.door.Database.Repository.UserConfigRepository;
import com.door.Service.BusService;
import com.door.Service.UserConfigService;
import com.door.Service.WeatherService;
import org.apache.catalina.User;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
public class SentenceController {

    @Autowired
    UserConfigRepository userConfigRepository;
    @Autowired
    WeatherService weatherService;
    @Autowired
    BusService busService;
    @Autowired
    UserConfigService userConfigService;

    @RequestMapping(value = "/sentence", consumes = "text/html; charset=utf8")
    public String getSentence(@RequestParam String deviceId) throws UnsupportedEncodingException {

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

        String sentence = "";
        if(userConfig.getIsEnableSkyInfo()) {
            sentence += "날씨는 ";
            sentence += weatherService.getSkyValue();
            sentence += Constants.SEPARATOR;
        }


        if(userConfig.getIsEnableTempInfo()) {
            sentence += "온도는 ";
            sentence += weatherService.getTempValue();
            sentence += Constants.SEPARATOR;
        }

        if(userConfig.getIsEnableHumidityInfo()){
            sentence += "습도는 ";
            sentence += weatherService.getHumidityValue();
            sentence += Constants.SEPARATOR;
        }

        if(userConfig.getIsEnableDustInfo()){
            sentence += "미세먼지는 ";
            sentence += weatherService.getDustValue();
            sentence += Constants.SEPARATOR;
        }

        if(userConfig.getIsEnableBusInfo()) {
            sentence += "버스는 ";
            System.out.println("dd :" + userConfig.getStId() + ", " + userConfig.getRtNm());
            sentence += busService.getArrivalTime(userConfig.getStId(), userConfig.getRtNm());
            sentence += Constants.SEPARATOR;
        }

        if(userConfig.getIsEnableTrainInfo()){

        }

        if(userConfig.getIsEnableMemoInfo()){
            sentence += "메모 ";
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
