package com.door.Service;

import com.door.Database.Entity.UserConfig;
import org.springframework.stereotype.Service;

@Service
public class UserConfigServiceImpl implements UserConfigService {
    public boolean isHavingNull(UserConfig userConfig) {
        if(userConfig.getIsEnableMemoInfo() == null) return true;
        return false;
    }
}
