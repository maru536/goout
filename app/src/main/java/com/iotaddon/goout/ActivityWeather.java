package com.iotaddon.goout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

        chkTemp = (CheckBox) findViewById(R.id.activity_weather_chkbox_temp);
        chkDust = (CheckBox) findViewById(R.id.activity_weather_chkbox_dust);
        chkHumidity = (CheckBox) findViewById(R.id.activity_weather_chkbox_humidity);
        chkSky = (CheckBox) findViewById(R.id.activity_weather_chkbox_sky);
        chkPrecipitation = (CheckBox) findViewById(R.id.activity_weather_chkbox_precipitation);
        chkWind = (CheckBox) findViewById(R.id.activity_weather_chkbox_wind);

/*        itemBox[0] = (RelativeLayout) findViewById(R.id.activity_weather_item0);
        itemBox[1] = (RelativeLayout) findViewById(R.id.activity_weather_item1);
        itemBox[2] = (RelativeLayout) findViewById(R.id.activity_weather_item2);
        itemBox[3] = (RelativeLayout) findViewById(R.id.activity_weather_item3);
        itemBox[4] = (RelativeLayout) findViewById(R.id.activity_weather_item4);
        itemBox[5] = (RelativeLayout) findViewById(R.id.activity_weather_item5);
        itemBox[6] = (RelativeLayout) findViewById(R.id.activity_weather_item6);*/


        chkTemp.setOnCheckedChangeListener(this);
        chkDust.setOnCheckedChangeListener(this);
        chkHumidity.setOnCheckedChangeListener(this);
        chkSky.setOnCheckedChangeListener(this);
        chkPrecipitation.setOnCheckedChangeListener(this);
        chkWind.setOnCheckedChangeListener(this);

        setCheckBox();
        /*setItemBox();*/



    }

    /*private void setItemBox() {
        int itemNum = 0;
        for (int i = 0; i <= ITEM_NUM_MAXIMUM; i++) {
            itemBox[i].setVisibility(View.INVISIBLE);
            if (dataManager.getSelectedWeather(i)) {
                itemNum++;
            }
        }

        switch (itemNum) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
        itemBox[itemNum].setVisibility(View.VISIBLE);
    }*/

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
