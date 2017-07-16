package com.door.VO;

import lombok.Data;

import java.util.Map;

@Data
public class DustResponseVO {
    private Map result;
    private Map common;
    private Map weather;
}
