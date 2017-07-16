package com.door.Service;

import com.door.Database.Entity.UserConfig;

import java.io.UnsupportedEncodingException;

/**
 * Created by wj on 17. 6. 30.
 */
public interface WarningService {
    public boolean isWarningSky (UserConfig userConfig);
    public boolean isWarningTemp (UserConfig userConfig);
    public boolean isWarningHumidity (UserConfig userConfig);
    public boolean isWarningRain (UserConfig userConfig);
    public boolean isWarningBus (UserConfig userConfig) throws UnsupportedEncodingException;
    public boolean isLEDOn (int info, UserConfig userConfig) throws UnsupportedEncodingException;
}
