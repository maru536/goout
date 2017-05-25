package com.iotaddon.goout;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by junhan on 2017-05-16.
 */

public class AsyncTaskHttpCommunicator extends AsyncTask<Void, Void, String> {

    private String strUrl = "";
    private WeatherDataUpdateListener listener = null;

    public static final String HTTP_URL_WEATHER = "http://13.124.126.90:8080/weather?lon=126.9658000000&lat=37.5714000000";
    public static final String HTTP_URL_TRANSPORTATION = "";
    public static final String HTTP_URL_WEATHER_DUST = "http://13.124.126.90:8080/dust?lon=126.9658000000&lat=37.5714000000";


    public void setListener(WeatherDataUpdateListener listener) {
        this.listener = listener;
    }

    public AsyncTaskHttpCommunicator(String strUrl) {
        this.strUrl = strUrl;
    }

    @Override
    protected String doInBackground(Void[] params) {
        String result = "";
        URL url;
        try {
            url = new URL(strUrl); // URL화 한다.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // URL을 연결한 객체 생성.
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestMethod("GET"); // get방식 통신
            conn.setDoOutput(true); // 쓰기모드 지정
            conn.setDoInput(true); // 읽기모드 지정
            conn.setUseCaches(false); // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false); // 캐싱데이터 디폴트 값 설정
            InputStream is = conn.getInputStream(); //input스트림 개방

            StringBuilder builder = new StringBuilder(); //문자열을 담기 위한 객체
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }

            result = builder.toString();

        } catch (MalformedURLException | ProtocolException exception) {
            exception.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String res) {
        if(strUrl == HTTP_URL_WEATHER){
            DataManager dataManager = DataManager.getInstance();
            DataWeather dataWeather = dataManager.getDataWeather();
            try {
                JSONObject json = new JSONObject(res);
                if(json.has("weather")){
                    JSONObject jsonWeather = json.getJSONObject("weather");
                    JSONArray jsonMinutely = jsonWeather.getJSONArray("minutely");
                    Log.e("ㅁㄴㅇㅁㄴㅇ",jsonMinutely.toString()+"   /  "+ jsonMinutely.length());
                    JSONObject jsonStation = jsonMinutely.getJSONObject(0).getJSONObject("station");
                    dataWeather.getDataWeatherStation().setLongitude(jsonStation.getDouble("longitude"));
                    dataWeather.getDataWeatherStation().setLatitude(jsonStation.getDouble("latitude"));
                    dataWeather.getDataWeatherStation().setName(jsonStation.getString("name"));
                    dataWeather.getDataWeatherStation().setId(jsonStation.getInt("id"));
                    dataWeather.getDataWeatherStation().setType(jsonStation.getString("type"));

                    JSONObject jsonWind = jsonMinutely.getJSONObject(0).getJSONObject("wind");
                    dataWeather.getDataWeatherWind().setWdir(jsonWind.getDouble("wdir"));
                    dataWeather.getDataWeatherWind().setWdir(jsonWind.getDouble("wspd"));

                    JSONObject jsonPrecipitation = jsonMinutely.getJSONObject(0).getJSONObject("precipitation");
                    dataWeather.getDataWeatherPrecipitation().setSinceOntime(jsonPrecipitation.getDouble("sinceOntime"));
                    dataWeather.getDataWeatherPrecipitation().setType(jsonPrecipitation.getInt("type"));

                    JSONObject jsonSky = jsonMinutely.getJSONObject(0).getJSONObject("sky");
                    dataWeather.getDataWeatherSky().setName(jsonSky.getString("name"));
                    dataWeather.getDataWeatherSky().setName(jsonSky.getString("code"));

                    JSONObject jsonRain = jsonMinutely.getJSONObject(0).getJSONObject("rain");
                    dataWeather.getDataWeatherRain().setLast6hour(jsonRain.getDouble("last6hour"));
                    dataWeather.getDataWeatherRain().setLast12hour(jsonRain.getDouble("last12hour"));
                    dataWeather.getDataWeatherRain().setLast24hour(jsonRain.getDouble("last24hour"));
                    dataWeather.getDataWeatherRain().setSinceMidnight(jsonRain.getDouble("sinceMidnight"));
                    dataWeather.getDataWeatherRain().setLast10min(jsonRain.getDouble("last10min"));
                    dataWeather.getDataWeatherRain().setLast15min(jsonRain.getDouble("last15min"));
                    dataWeather.getDataWeatherRain().setLast30min(jsonRain.getDouble("last30min"));
                    dataWeather.getDataWeatherRain().setLast1hour(jsonRain.getDouble("last1hour"));
                    dataWeather.getDataWeatherRain().setSinceOntime(jsonRain.getDouble("sinceOntime"));

                    JSONObject jsonTemperature = jsonMinutely.getJSONObject(0).getJSONObject("temperature");
                    dataWeather.getDataWeatherTemperature().setTc(jsonTemperature.getDouble("tc"));
                    dataWeather.getDataWeatherTemperature().setTmax(jsonTemperature.getDouble("tmax"));
                    dataWeather.getDataWeatherTemperature().setTmin(jsonTemperature.getDouble("tmin"));

                    dataWeather.getDataWeatherHumidity().setHumidity(jsonMinutely.getJSONObject(0).getDouble("humidity"));

                    JSONObject jsonPressure = jsonMinutely.getJSONObject(0).getJSONObject("pressure");
                    dataWeather.getDataWeatherPressure().setSealevel(jsonPressure.getDouble("seaLevel"));
                    dataWeather.getDataWeatherPressure().setSurface(jsonPressure.getDouble("surface"));

                    dataWeather.getDataWeatherLightning().setLightning(jsonMinutely.getJSONObject(0).getInt("lightning"));

                    dataWeather.getDataWeatherTimeObservation().setDate(jsonMinutely.getJSONObject(0).getString("timeObservation"));
                }else{
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(strUrl == HTTP_URL_WEATHER_DUST){

        }else if(strUrl == HTTP_URL_TRANSPORTATION){

        }

        if(listener!=null){
            listener.doUpdate();
        }
    }
}
