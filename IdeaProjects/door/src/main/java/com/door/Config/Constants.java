package com.door.Config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Constants {
    public String CITY_SEOUL = "서울";

	//SK PLANET API header constants
    public static final String ACCEPT = "application/json";
    public static final String ACCEPT_LANGUAGE = "ko";
    public static final String CACHE_CONTROL = "no-cache";
    public static final String CONTENT_LENGTH = "320";
    public static final String CONTENT_TYPE = "application_xml";
    public static final String HOST = "www.skplanetx.com";

    //Weather API
    public static final String WEATHER_API_URL = "http://apis.skplanetx.com//weather/current/minutely";
    //public static final String WEATHER_APP_KEY = "6d7cfd33-b808-37fb-b212-5050ba212449";w
    public static final String WEATHER_APP_KEY = "2712c63b-a907-38b9-8a56-bd10cfb71f8a";

    //DUST API
    public static final String DUST_API_URL = "http://apis.skplanetx.com/weather/dust";

    //BUS API
    public static final String BUS_NEAR_BUS_STOP_API_URL = "http://openapi.gbis.go.kr/ws/rest/busstationservice/searcharound";
    public static final String BUS_BUS_STOP_API_URL = "http://openapi.gbis.go.kr/ws/rest/busstationservice";
    public static final String BUS_ARRIVAL_ALL_URL = "http://ws.bus.go.kr/api/rest/arrive/getLowArrInfoByStId";
    public static final String BUS_APP_KEY = "XQC5wcVhW9NE1sRHoSALP5jFjCfmF01AvadXsBU9NqdKOq6Qz0m1p8XG6u%2BIUKqbtQozmFM4ATxoSdGXc4XJzQ%3D%3D";

    //isEnableVariable
    public static final int ENABLE_WEATHER_TEMP = 1;
    public static final int ENABLE_WEATHER_DUST = 2;
    public static final int ENABLE_WEATHER_HUMIDITY = 3;
    public static final int ENABLE_WEATHER_SKY = 4;
    public static final int ENABLE_WEATHER_WIND = 5;
    public static final int ENABLE_WEATHER_PRECIPITATION = 6;
    public static final int ENABLE_NONE = 0;
    public static final int ENABLE_TRANSPORTATION_BUS = 10;
    public static final int ENABLE_TRANSPORTATION_SUBWAY = 11;

    // SKY Constants
    public final int WARNING_SKY_SUNNY = 0;
    public final int WARNING_SKY_BLUE = 1;
    public final int WARNING_SKY_RAIN_SNOW = 2;
    public static final String SEPARATOR = ",";
}
