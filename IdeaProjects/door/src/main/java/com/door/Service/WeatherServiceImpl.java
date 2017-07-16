package com.door.Service;

import com.door.Config.Constants;
import com.door.VO.DustResponseVO;
import com.door.VO.WeatherResponseVO;
import lombok.Data;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Data
public class WeatherServiceImpl implements WeatherService{

    private static final Logger log = LoggerFactory.getLogger(WeatherServiceImpl.class);

    @Autowired
    Constants constants;

    private Object weatherData;
    private Object dustData;

    public Object getWeatherData () {
        return weatherData;
    }

    public Object getDustData () {
        return dustData;
    }
    public Object getWeatherDataFromURL(String longitude, String latitude){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        setHeader(httpHeaders);

        String version = "1";

        //http://apis.skplanetx.com//weather/current/minutely?lon=126.9658000000&village=&cellAWS=&lat=37.5714000000&country=&city=&version=1
        String url = Constants.WEATHER_API_URL
                + "?lon=" + longitude
                + "&lat=" + latitude
                + "&version=" + version;

        ResponseEntity<WeatherResponseVO> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), WeatherResponseVO.class);

        WeatherResponseVO weatherResponseVO = responseEntity.getBody();
        return weatherResponseVO;
    }


    public Object getDustData(String longitude, String latitude){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        setHeader(httpHeaders);

        String version = "1";

        String url = Constants.DUST_API_URL
                + "?lon=" + longitude
                + "&lat=" + latitude
                + "&version=" + version;
        System.out.println("1");
        ResponseEntity<DustResponseVO> responseEntity
                = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity(httpHeaders), DustResponseVO.class);

        System.out.println("2");
        DustResponseVO dustResponseVO = responseEntity.getBody();

        System.out.println("3");
        return dustResponseVO;
    }


    public void setWeatherData (String latitude, String longitude) {
        weatherData = getWeatherDataFromURL(longitude, latitude);
    }

    public void setDustData (String latitude, String longitude) {
        dustData = getDustData(latitude ,longitude);
    }

    public String getSkyValue() {
        JSONObject jsonWeatherData = new JSONObject(weatherData);
        return (String) jsonWeatherData.getJSONObject("weather").getJSONArray("minutely").getJSONObject(0).getJSONObject("sky").get("name");
    }

    public String getTempValue() {
        JSONObject jsonWeatherData = new JSONObject(weatherData);
        return (String) jsonWeatherData.getJSONObject("weather").getJSONArray("minutely").getJSONObject(0).getJSONObject("temperature").get("tc");
    }


    public String getRainValue() {
        JSONObject jsonWeatherData = new JSONObject(weatherData);
        return (String) jsonWeatherData.getJSONObject("weather").getJSONArray("minutely").getJSONObject(0).getJSONObject("rain").get("sinceOntime");
    }

    public String getHumidityValue() {
        JSONObject jsonWeatherData = new JSONObject(weatherData);
        System.out.println(weatherData);
        return (String) jsonWeatherData.getJSONObject("weather").getJSONArray("minutely").getJSONObject(0).get("humidity");
    }

    public String getDustValue() {
        JSONObject jsonWeatherData = new JSONObject(dustData);
        return (String) jsonWeatherData.getJSONObject("weather").getJSONArray("dust").getJSONObject(0).getJSONObject("pm10").get("value");
    }

    public String getDustGrade(){
        JSONObject jsonWeatherData = new JSONObject(dustData);
        return (String) jsonWeatherData.getJSONObject("weather").getJSONArray("dust").getJSONObject(0).getJSONObject("pm10").get("grade");
    }

    public HttpHeaders setHeader(HttpHeaders httpHeaders) {
        httpHeaders.add(HttpHeaders.ACCEPT, constants.ACCEPT);
        httpHeaders.add(HttpHeaders.ACCEPT_LANGUAGE, constants.ACCEPT_LANGUAGE);
        httpHeaders.add("appkey", constants.WEATHER_APP_KEY);
        httpHeaders.add(HttpHeaders.CACHE_CONTROL, Constants.CACHE_CONTROL);
        httpHeaders.add(HttpHeaders.CONTENT_LENGTH, Constants.CONTENT_LENGTH);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, Constants.CONTENT_TYPE);
        httpHeaders.add("data", new Date().toString());
        httpHeaders.add(HttpHeaders.HOST, Constants.HOST);
        return httpHeaders;
    }

}
