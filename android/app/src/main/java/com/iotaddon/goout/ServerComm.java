package com.iotaddon.goout;

/**
 * Created by maru5 on 2017-05-20.
 */

public class ServerComm {
    private volatile static ServerComm mUniqueInstance;
    private HttpProtocol mHttpProtocol;
    private final static String mApAddress = "http://192.168.10.1";

    private ServerComm() {
        mHttpProtocol = new HttpProtocol();
    }

    public void setListener(HttpResponseDataUpdateListener listener){
        mHttpProtocol.setListener(listener);
    }

    public static ServerComm getInstance() {
        if (mUniqueInstance == null) {
            synchronized (ServerComm.class) {
                if (mUniqueInstance == null) {
                    mUniqueInstance = new ServerComm();
                }
            }
        }

        return mUniqueInstance;
    }

    public void regist(String deviceID, String macAddr) {
        mHttpProtocol.apGet(mApAddress);

        /*HashMap id = new HashMap();

        id.put("deviceId", deviceID);
        id.put("id", macAddr);

        JSONObject body = new JSONObject(id);
        mHttpProtocol.post("/signup", body);*/
    }

    /*public static JSONObject memo(String deviceID, String content) {
        HashMap id = new HashMap();

        id.put("Device_ID", deviceID);
        id.put("Memo", content);

        JSONObject body = new JSONObject(id);
        return HttpProtocol.post("http://addr", body);
    }*/

    public void weather(double longitude, double latitude) {
        mHttpProtocol.get("/weather?lon="+longitude+"&lat="+latitude);
    }



    public void dust(double longitude, double latitude) {
        mHttpProtocol.get("/dust?lon="+longitude+"&lat="+latitude);
    }

    public void getConfig(String deviceId) {
        mHttpProtocol.get("/getconfig?deviceId="+deviceId);
    }
    /*public static JSONObject traffic(double src_latitude, double src_longitude, double dst_latitude, double dst_longitude) {
        HashMap biPosition = new HashMap();

        biPosition.put("src_latitude", src_latitude);
        biPosition.put("src_longitude", src_longitude);
        biPosition.put("dst_latitude", dst_latitude);
        biPosition.put("dst_longitude", dst_longitude);

        JSONObject header = new JSONObject(biPosition);

        return HttpProtocol.get("http://", header);
    }*/

    /*public static JSONObject transportation(String station, int number) {
        HashMap position = new HashMap();

        position.put("station", station);
        position.put("number", number);

        JSONObject header = new JSONObject(position);

        return HttpProtocol.get("http://", header);
    }*/
}
