package com.iotaddon.goout;

import org.json.JSONObject;
import java.util.HashMap;

/**
 * Created by maru5 on 2017-05-20.
 */

public class ServerComm {
    public static JSONObject regist(String deviceID, String macAddr) {
        HashMap id = new HashMap();

        id.put("deviceId", deviceID);
        id.put("id", macAddr);

        JSONObject body = new JSONObject(id);
        return ServerProtocol.post("/signup", body);
    }

    /*public static JSONObject memo(String deviceID, String content) {
        HashMap id = new HashMap();

        id.put("Device_ID", deviceID);
        id.put("Memo", content);

        JSONObject body = new JSONObject(id);
        return ServerProtocol.post("http://addr", body);
    }*/

    public static JSONObject weather(double longitude, double latitude) {
        return ServerProtocol.get("/weather?lon="+longitude+"&lat="+latitude);
    }

    public static JSONObject dust(double longitude, double latitude) {
        return ServerProtocol.get("/dust?lon="+longitude+"&lat="+latitude);
    }

    /*public static JSONObject traffic(double src_latitude, double src_longitude, double dst_latitude, double dst_longitude) {
        HashMap biPosition = new HashMap();

        biPosition.put("src_latitude", src_latitude);
        biPosition.put("src_longitude", src_longitude);
        biPosition.put("dst_latitude", dst_latitude);
        biPosition.put("dst_longitude", dst_longitude);

        JSONObject header = new JSONObject(biPosition);

        return ServerProtocol.get("http://", header);
    }*/

    /*public static JSONObject transportation(String station, int number) {
        HashMap position = new HashMap();

        position.put("station", station);
        position.put("number", number);

        JSONObject header = new JSONObject(position);

        return ServerProtocol.get("http://", header);
    }*/
}
