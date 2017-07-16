package com.door.Controller;

import com.door.Database.Entity.UserConfig;
import com.door.Database.Repository.UserConfigRepository;
import com.door.Service.SentenceService;
import com.door.Service.WarningService;
import com.door.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@RestController
public class LEDController {

    @Autowired
    UserConfigRepository userConfigRepository;

    @Autowired
    WeatherService weatherService;

    @Autowired
    SentenceService sentenceService;
    @Autowired
    WarningService warningService;

    @RequestMapping(value = "/led_sentence", produces = "application/json; charset=utf-8")
    public Object getLEDInfo (@RequestParam String deviceId) throws UnsupportedEncodingException {

        UserConfig userConfig = userConfigRepository.getOne(deviceId);
        weatherService.setWeatherData(userConfig.getLatitude(), userConfig.getLongitude());
        weatherService.setDustData(userConfig.getLatitude(), userConfig.getLongitude());
        HashMap<String, Object> ledInfo = new HashMap<>();
        ledInfo.put("firstLED", warningService.isLEDOn(userConfig.getFirstLEDInfo(), userConfig));
        ledInfo.put("secondLED", warningService.isLEDOn(userConfig.getSecondLEDInfo(), userConfig));
        ledInfo.put("thirdLED", warningService.isLEDOn(userConfig.getThirdLEDInfo(), userConfig));
        ledInfo.put("sentence", sentenceService.getSentence(deviceId));
        return ledInfo;
    }

}
