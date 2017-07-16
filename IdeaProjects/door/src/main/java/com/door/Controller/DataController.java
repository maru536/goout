package com.door.Controller;

import com.door.Config.Constants;
import com.door.Service.BusService;
import com.door.Service.WeatherService;
import com.door.VO.WeatherResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
public class DataController {

    @Autowired
    WeatherService weatherService;

    @Autowired
    BusService busService;

    @RequestMapping("/weather")
    public Object getWeatherData(@RequestParam String lon,
                          @RequestParam String lat){
        return weatherService.getWeatherDataFromURL(lon, lat);
    }

    @RequestMapping("/dust")
    public Object getDustData(@RequestParam String lon,
                          @RequestParam String lat){
       return weatherService.getDustData(lon, lat);
    }

    @RequestMapping("/bus/near")
    public Object getNearBusData (@RequestParam String lon,
                                 @RequestParam String lat) throws UnsupportedEncodingException {
        return busService.getNearBusStopData(lon, lat);
    }


    @RequestMapping("/bus/busstop")
    public Object getBusStopData (@RequestParam String number) throws UnsupportedEncodingException {
        return busService.getBusStopData(number);
    }

    @RequestMapping("/bus/stationId")
    public Object getStationId (@RequestParam String number) throws UnsupportedEncodingException{
        return (String) busService.getStationId(number);
    }

    @RequestMapping("/bus/arrival")
    public Object getArrivalData (@RequestParam String stId,
                                  @RequestParam String busRouteId) throws UnsupportedEncodingException{
        return busService.getArrivalData(stId, busRouteId);
    }

    @RequestMapping("/bus/arrival/all")
    public Object getArrivalDataAll (@RequestParam String stId) throws UnsupportedEncodingException{
        return busService.getArrivalDataAll(stId);
    }

}
