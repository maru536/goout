package com.door.Service;

import com.door.VO.WeatherResponseVO;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface WeatherService {
    public Object getWeatherData ();
    public Object getDustData ();
    public Object getWeatherDataFromURL(String longitude , String latitude);
    public void setWeatherData(String latitude, String longitude);
    public Object getDustData(String longitude , String latitude);
    public void setDustData (String latitude, String longitude);

    public String getSkyValue();
    public String getTempValue();
    public String getRainValue();
    public String getHumidityValue();
    public String getDustValue();
    public String getDustGrade();

    public Object setHeader(HttpHeaders httpHeaders);

}
