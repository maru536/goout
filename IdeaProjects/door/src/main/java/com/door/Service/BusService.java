package com.door.Service;

import java.io.UnsupportedEncodingException;

public interface BusService {
    public Object getNearBusStopData(String x, String y) throws UnsupportedEncodingException;
    public Object getBusStopData(String busStopNumber) throws UnsupportedEncodingException;
    public Object getStationId(String number) throws UnsupportedEncodingException;
    public Object getArrivalData(String stId, String busRouteId) throws UnsupportedEncodingException;
    public Object getArrivalDataAll(String stId) throws UnsupportedEncodingException;
    public String getArrivalTime (String stId, String rtNm) throws UnsupportedEncodingException;
}
