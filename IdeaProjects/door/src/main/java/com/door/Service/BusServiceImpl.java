package com.door.Service;


import com.door.Config.Constants;
import com.door.Config.type.URL;
import com.door.VO.DustResponseVO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class BusServiceImpl implements BusService {

    RestTemplate restTemplate = new RestTemplate();

    public Object getNearBusStopData(String x, String y) throws UnsupportedEncodingException {
        URL url = new URL(Constants.BUS_NEAR_BUS_STOP_API_URL);
        url.addParameter("serviceKey", URLDecoder.decode(Constants.BUS_APP_KEY, "UTF-8"));
        url.addParameter("x", x);
        url.addParameter("y", y);

        String resultStr = restTemplate.getForObject(url.toString(), String.class);

        Map result = XML.toJSONObject(resultStr).toMap();
        return result;
    }


    public Object getBusStopData(String number) throws UnsupportedEncodingException {
        URL url = new URL(Constants.BUS_BUS_STOP_API_URL);
        url.addParameter("serviceKey", URLDecoder.decode(Constants.BUS_APP_KEY, "UTF-8"));
        url.addParameter("keyword", number);

        String resultStr = restTemplate.getForObject(url.toString(), String.class);
        return XML.toJSONObject(resultStr).toMap();

    }

    public Object getStationId(String number) throws UnsupportedEncodingException {
        Map busStopData = (Map) getBusStopData(number);
        Object map = busStopData.get("response");
        map = ((Map)map).get("msgBody");
        map = ((Map)map).get("busStationList");
        return ((Map)map).get("stationId").toString();
    }

    public Object getArrivalData(String stId, String busRouteId) throws UnsupportedEncodingException {
        return null;
    }

    public Object getArrivalDataAll(String stId) throws UnsupportedEncodingException {
        URL url = new URL(Constants.BUS_ARRIVAL_ALL_URL);
        url.addParameter("serviceKey", URLDecoder.decode(Constants.BUS_APP_KEY, "UTF-8"));
        url.addParameter("stId", stId);

        String resultStr = restTemplate.getForObject(url.toString(), String.class);
        return XML.toJSONObject(resultStr).toMap();
    }

    public String getArrivalTime (String stId, String rtNm) throws UnsupportedEncodingException {
        HashMap<String, Object> busData = (HashMap) getArrivalDataAll(stId);
        JSONObject jsonBusData = new JSONObject(busData);

        JSONArray jsonBusArrivalDatas = jsonBusData.getJSONObject("ServiceResult").getJSONObject("msgBody").getJSONArray("itemList");
        ArrayList busArrivalDatas = new ArrayList();

        for (int i=0; i<jsonBusArrivalDatas.length(); i++){
            busArrivalDatas.add(jsonBusArrivalDatas.get(i));
        }

        for( Object busArrivalData : busArrivalDatas){
            JSONObject jsonBusArrivalData = (JSONObject) busArrivalData;
            if( jsonBusArrivalData.get("rtNm").equals(rtNm) ) {
                System.out.println(jsonBusArrivalData.get("rtNm") + ", " + rtNm);
                return (String) jsonBusArrivalData.get("arrmsg1");
            }
        }
        return "No Bus";
    }

}