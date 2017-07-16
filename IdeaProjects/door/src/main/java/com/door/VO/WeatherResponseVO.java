package com.door.VO;

import lombok.Data;

import java.util.Map;

@Data
public class WeatherResponseVO {
    private Map weather;
    private Map common;
    private Map result;
}
