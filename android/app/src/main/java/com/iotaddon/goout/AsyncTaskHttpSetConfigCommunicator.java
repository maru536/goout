package com.iotaddon.goout;

import android.os.AsyncTask;
import android.util.Log;

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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by junhan on 2017-06-29.
 */

public class AsyncTaskHttpSetConfigCommunicator extends AsyncTask<Void, Void, Void> {
    private String strUrl = "http://52.78.126.50:8080/setconfig";
    private HttpResponseDataUpdateListener listener = null;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    boolean isFirst = true;


    public void setListener(HttpResponseDataUpdateListener listener) {
        this.listener = listener;
    }

    public AsyncTaskHttpSetConfigCommunicator() {
    }

    @Override
    protected Void doInBackground(Void... params) {

        DataManager dm = DataManager.getInstance();


        try {
            JSONObject json = new JSONObject();
            json.put("deviceId", dm.getFcmToken());
            json.put("ip", "");
            json.put("tempLimit", dm.getDataWeather().getDataWeatherTemperature().getWarningValue() + "");
            json.put("humidityLimit", dm.getDataWeather().getDataWeatherHumidity().getWarningValue() + "");
            json.put("skyLimit", dm.getDataWeather().getDataWeatherSky().getWarningValue());
            json.put("rainLimit", "");
            json.put("dustLimit", dm.getDataWeather().getDataWeatherDust().getWarningValue());
            json.put("transportLimit", dm.getDataBusInfo().getWarningValue());
            json.put("memo", dm.getSavedMemo());
            json.put("isEnableSkyInfo", dm.getSelectedWeather(dm.TYPE_WEATHER_SKY));
            json.put("isEnableHumidityInfo", dm.getSelectedWeather(dm.TYPE_WEATHER_HUMIDITY));
            json.put("isEnableTempInfo", dm.getSelectedWeather(dm.TYPE_WEATHER_TEMP));
            json.put("isEnableDustInfo", dm.getSelectedWeather(dm.TYPE_WEATHER_DUST));
            json.put("isEnableBusInfo", dm.getSelectedTransportation(dm.TYPE_TRANSPORTATION_BUS_SELECTED));
            json.put("isEnableTrainInfo", false);
            json.put("isEnableMemoInfo", false);
            json.put("firstLEDInfo", dm.getSelectedLed(DataManager.TYPE_LED_1));
            json.put("secondLEDInfo", dm.getSelectedLed(DataManager.TYPE_LED_2));
            json.put("thirdLEDInfo", dm.getSelectedLed(DataManager.TYPE_LED_3));
            json.put("latitude", dm.getUserAddress().getLatitude());
            json.put("longitude", dm.getUserAddress().getLongitude());
            json.put("name", dm.getUserAddress().getName());
            json.put("alarm", dm.isOutAlarm());
            json.put("busRouteId", dm.getDataBusInfo().getBusRouteId());
            json.put("stId", dm.getDataBusInfo().getStId());
            json.put("rtNm", dm.getDataBusInfo().getRtNm());
            json.put("isHigher_Temp", dm.getDataWeather().getDataWeatherTemperature().isHigher());
            json.put("isHigher_Humidity", dm.getDataWeather().getDataWeatherHumidity().isHigher());
            json.put("isHigher_rainLimit", true);
            json.put("isHigher_dustLimit", dm.getDataWeather().getDataWeatherDust().isHigher());
            json.put("isHigher_transportLimit", dm.getDataBusInfo().isHigher());
            Log.e("json log###", json.toString());
            Log.e("setconfig######", "setconfig####################");
            post(strUrl, json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (listener != null)
            listener.doUpdate("");
    }

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
