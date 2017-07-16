package com.door.Controller;

import com.door.Database.Entity.UserConfig;
import com.door.Database.Repository.UserConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;

@RestController
public class UserDataController {

    @Autowired
    UserConfigRepository userConfigRepository;

    @PostMapping("/enroll")
    public String setUser(@RequestBody UserConfig userConfig)
    {
         if( userConfigRepository.save(userConfig) != null) return "SUCCESS";
         else return "FAIL";
    }

    @PostMapping(value = "/setconfig", consumes = "application/json; charset=utf-8")
    public UserConfig setConfig(@RequestBody UserConfig newUserConfig)
    {

        System.out.println(newUserConfig);

        String deviceId = newUserConfig.getDeviceId();
        if(deviceId == null) return null;

        UserConfig oldUserConfig = null;


        if(userConfigRepository.exists(deviceId)) {
            oldUserConfig = userConfigRepository.getOne(deviceId);
        }

        userConfigRepository.save(newUserConfig);

        return newUserConfig;
    }


    @GetMapping("/getconfig")
    public UserConfig getConfig(@RequestParam String deviceId){
        if(userConfigRepository.exists(deviceId)) {
            return userConfigRepository.getOne(deviceId);
        }

        else return null;
    }
}
