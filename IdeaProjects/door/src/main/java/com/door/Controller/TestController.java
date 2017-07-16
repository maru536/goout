package com.door.Controller;

import com.door.Service.BusService;
import com.door.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

/**
 * Created by wj on 17. 6. 28.
 */

@RestController
public class TestController {

    @Autowired
    WeatherService weatherService;

    @Autowired
    BusService busService;

    @RequestMapping
    public String hi() throws UnsupportedEncodingException{
        weatherService.setWeatherData("126.96580000", "37.5714000");
        weatherService.setDustData("126.96580000", "37.5714000");

        return busService.getArrivalTime("203000028", "M5422광주");

    }
}
