package com.door.Database.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserConfig {
    @Id
    private String deviceId;
    private String ip;

    // Limit value
    private String tempLimit;
    private String humidityLimit;
    private String rainLimit;
    private String transportLimit;
    private String skyLimit;
    private String dustLimit;

    //Memo
    private String memo;

    //enable
    private Boolean isEnableSkyInfo;
    private Boolean isEnableHumidityInfo;
    private Boolean isEnableTempInfo;
    private Boolean isEnableDustInfo;
    private Boolean isEnableBusInfo;
    private Boolean isEnableTrainInfo;
    private Boolean isEnableMemoInfo;

    //selectLED
    private int firstLEDInfo;
    private int secondLEDInfo;
    private int thirdLEDInfo;

    //address
    private String latitude;
    private String longitude;
    private String name;

    //Alarm
    private Boolean alarm;

    //bus
    private int busRouteId;
    private String stId;
    private String rtNm;

    //isHigher
    private Boolean isHigher_Temp;
    private Boolean isHigher_Humidity;
    private Boolean isHigher_rainLimit;
    private Boolean isHigher_transportLimit;
    private Boolean isHigher_dustLimit;
}
