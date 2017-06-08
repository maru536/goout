package com.iotaddon.goout;

/**
 * Created by junhan on 2017-05-17.
 */

public class DataWeather {
    private DataWeatherRain dataWeatherRain;
    private DataWeatherResult dataWeatherResult;
    private DataWeatherSky dataWeatherSky;
    private DataWeatherStation dataWeatherStation;
    private DataWeatherTemperature dataWeatherTemperature;
    private DataWeatherTimeObservation dataWeatherTimeObservation;
    private DataWeatherWind dataWeatherWind;
    private DataWeatherHumidity dataWeatherHumidity;
    private DataWeatherCommon dataWeatherCommon;
    private DataWeatherPrecipitation dataWeatherPrecipitation;
    private DataWeatherPressure dataWeatherPressure;
    private DataWeatherLightning dataWeatherLightning;
    private DataWeatherDust dataWeatherDust;

    public DataWeather(){
        dataWeatherCommon = new DataWeatherCommon("","");
        dataWeatherHumidity = new DataWeatherHumidity(0);
        dataWeatherLightning = new DataWeatherLightning(0);
        dataWeatherPrecipitation = new DataWeatherPrecipitation(0,0);
        dataWeatherPressure = new DataWeatherPressure(0,0);
        dataWeatherRain = new DataWeatherRain(0,0,0,0,0,0,0,0,0);
        dataWeatherResult = new DataWeatherResult(0,"","");
        dataWeatherSky = new DataWeatherSky("","");
        dataWeatherStation = new DataWeatherStation(0,0,"",0,"");
        dataWeatherTemperature = new DataWeatherTemperature(0,0,0);
        dataWeatherWind = new DataWeatherWind(0,0);
        dataWeatherTimeObservation = new DataWeatherTimeObservation("");
        dataWeatherDust = new DataWeatherDust("",0);

    }

    public DataWeatherDust getDataWeatherDust() {
        return dataWeatherDust;
    }

    public DataWeatherRain getDataWeatherRain() {
        return dataWeatherRain;
    }

    public DataWeatherResult getDataWeatherResult() {
        return dataWeatherResult;
    }

    public DataWeatherSky getDataWeatherSky() {
        return dataWeatherSky;
    }

    public DataWeatherStation getDataWeatherStation() {
        return dataWeatherStation;
    }

    public DataWeatherTemperature getDataWeatherTemperature() {
        return dataWeatherTemperature;
    }

    public DataWeatherTimeObservation getDataWeatherTimeObservation() {
        return dataWeatherTimeObservation;
    }

    public DataWeatherWind getDataWeatherWind() {
        return dataWeatherWind;
    }

    public DataWeatherHumidity getDataWeatherHumidity() {
        return dataWeatherHumidity;
    }

    public DataWeatherCommon getDataWeatherCommon() {
        return dataWeatherCommon;
    }

    public DataWeatherPrecipitation getDataWeatherPrecipitation() {
        return dataWeatherPrecipitation;
    }

    public DataWeatherPressure getDataWeatherPressure() {
        return dataWeatherPressure;
    }

    public DataWeatherLightning getDataWeatherLightning() {
        return dataWeatherLightning;
    }
}
