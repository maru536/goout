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
    private String param1 = "";
    private HttpResponseDataUpdateListener listener = null;

    //public static final String HTTP_URL_WEATHER = "http://13.124.126.90:8080/weather?lon=126.9658000000&lat=37.5714000000";
    public static final String HTTP_URL_WEATHER = "http://13.124.126.90:8080/weather?";
    public static final String HTTP_URL_WEATHER_DUST = "http://13.124.126.90:8080/dust?";
    public static final String HTTP_URL_TRANSPORTATION_BUS_STATION = "http://13.124.126.90:8080/bus/busstop?number=";
    public static final String HTTP_URL_TRANSPORTATION_BUS_AROUND_STATION = "http://13.124.126.90:8080/bus/near?";
    public static final String HTTP_URL_TRANSPORTATION_BUS_LIST = "http://13.124.126.90:8080/bus/arrival/all?stId=";





    public void setListener(HttpResponseDataUpdateListener listener) {
        this.listener = listener;
    }

    public AsyncTaskHttpCommunicator(String strUrl, String param1) {
        this.strUrl = strUrl;
        this.param1 = param1;
    }

    @Override
    protected String doInBackground(Void[] params) {
        String result = "";
        String addr = strUrl;
        double lat, lon;
        URL url;
        try {
            if(strUrl.equals(HTTP_URL_WEATHER) || strUrl.equals(HTTP_URL_TRANSPORTATION_BUS_AROUND_STATION) || strUrl.equals(HTTP_URL_WEATHER_DUST)){
                lat = DataManager.getInstance().getUserAddress().getLatitude();
                lon = DataManager.getInstance().getUserAddress().getLongitude();
                String pos = "lon="+lon+"&lat="+lat;
                addr = strUrl+pos;
            }else if(strUrl.equals(HTTP_URL_TRANSPORTATION_BUS_STATION) || strUrl.equals(HTTP_URL_TRANSPORTATION_BUS_LIST)){
                addr = strUrl+param1;
            }
            Log.e("str url", addr);
            url = new URL(addr); // URL화 한다.
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
        if(listener!=null){
            listener.doUpdate(res);
        }
    }
}
