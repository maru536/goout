package com.iotaddon.goout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

public class ActivityWeather extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox chkTemp, chkDust, chkHumidity, chkSky, chkPrecipitation, chkWind;
    private RelativeLayout itemBox[] = new RelativeLayout[7];
    private DataManager dataManager = DataManager.getInstance();
    private final int ITEM_NUM_MAXIMUM = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        getSupportActionBar().setElevation(0);

        chkTemp = (CheckBox) findViewById(R.id.activity_weather_chkbox_temp);
        chkDust = (CheckBox) findViewById(R.id.activity_weather_chkbox_dust);
        chkHumidity = (CheckBox) findViewById(R.id.activity_weather_chkbox_humidity);
        chkSky = (CheckBox) findViewById(R.id.activity_weather_chkbox_sky);
        chkPrecipitation = (CheckBox) findViewById(R.id.activity_weather_chkbox_precipitation);
        chkWind = (CheckBox) findViewById(R.id.activity_weather_chkbox_wind);

        chkTemp.setOnCheckedChangeListener(this);
        chkDust.setOnCheckedChangeListener(this);
        chkHumidity.setOnCheckedChangeListener(this);
        chkSky.setOnCheckedChangeListener(this);
        chkPrecipitation.setOnCheckedChangeListener(this);
        chkWind.setOnCheckedChangeListener(this);

        setCheckBox();

    }


    private void setCheckBox() {
        chkTemp.setChecked(dataManager.getSelectedWeather(dataManager.TYPE_WEATHER_TEMP));
        chkDust.setChecked(dataManager.getSelectedWeather(dataManager.TYPE_WEATHER_DUST));
        chkHumidity.setChecked(dataManager.getSelectedWeather(dataManager.TYPE_WEATHER_HUMIDITY));
        chkSky.setChecked(dataManager.getSelectedWeather(dataManager.TYPE_WEATHER_SKY));
        chkPrecipitation.setChecked(dataManager.getSelectedWeather(dataManager.TYPE_WEATHER_PRECIPITATION));
        chkWind.setChecked(dataManager.getSelectedWeather(dataManager.TYPE_WEATHER_WIND));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (dataManager.getUserAddress().getLatitude() == 0) {
            ((CheckBox)buttonView).setChecked(false);
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("내 위치 설정하기");
            gsDialog.setMessage("날씨 정보를 사용하기 위해서 위치를 설정해야 합니다. 지금 위치설정을 하시겠습니까?");
            gsDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(getApplicationContext(), ActivityDeviceSettings.class);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return;
        } else {
            switch (buttonView.getId()) {
                case R.id.activity_weather_chkbox_temp:
                    dataManager.setSelectedWeather(DataManager.TYPE_WEATHER_TEMP, isChecked);
                    break;
                case R.id.activity_weather_chkbox_dust:
                    dataManager.setSelectedWeather(DataManager.TYPE_WEATHER_DUST, isChecked);
                    break;
                case R.id.activity_weather_chkbox_humidity:
                    dataManager.setSelectedWeather(DataManager.TYPE_WEATHER_HUMIDITY, isChecked);
                    break;
                case R.id.activity_weather_chkbox_sky:
                    dataManager.setSelectedWeather(DataManager.TYPE_WEATHER_SKY, isChecked);
                    break;
                case R.id.activity_weather_chkbox_wind:
                    dataManager.setSelectedWeather(DataManager.TYPE_WEATHER_WIND, isChecked);
                    break;
                case R.id.activity_weather_chkbox_precipitation:
                    dataManager.setSelectedWeather(DataManager.TYPE_WEATHER_PRECIPITATION, isChecked);
                    break;

            }
        }
    }
}
