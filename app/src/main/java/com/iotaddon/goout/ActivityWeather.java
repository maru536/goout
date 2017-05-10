package com.iotaddon.goout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

public class ActivityWeather extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox chkTemp, chkDust, chkWet, chkDiscomfort, chkWeather, chkBodytemp;
    private RelativeLayout itemBox[] = new RelativeLayout[7];
    private DataManager dataManager = DataManager.getInstance();
    private final int ITEM_NUM_MAXIMUM = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chkTemp = (CheckBox) findViewById(R.id.activity_weather_chkbox_temper);
        chkDust = (CheckBox) findViewById(R.id.activity_weather_chkbox_dust);
        chkWet = (CheckBox) findViewById(R.id.activity_weather_chkbox_wet);
        chkWeather = (CheckBox) findViewById(R.id.activity_weather_chkbox_weather);
        chkDiscomfort = (CheckBox) findViewById(R.id.activity_weather_chkbox_discomfort);
        chkBodytemp = (CheckBox) findViewById(R.id.activity_weather_chkbox_bodytemp);

        itemBox[0] = (RelativeLayout) findViewById(R.id.activity_weather_item0);
        itemBox[1] = (RelativeLayout) findViewById(R.id.activity_weather_item1);
        itemBox[2] = (RelativeLayout) findViewById(R.id.activity_weather_item2);
        itemBox[3] = (RelativeLayout) findViewById(R.id.activity_weather_item3);
        itemBox[4] = (RelativeLayout) findViewById(R.id.activity_weather_item4);
        itemBox[5] = (RelativeLayout) findViewById(R.id.activity_weather_item5);
        itemBox[6] = (RelativeLayout) findViewById(R.id.activity_weather_item6);


        chkTemp.setOnCheckedChangeListener(this);
        chkDust.setOnCheckedChangeListener(this);
        chkWeather.setOnCheckedChangeListener(this);
        chkWet.setOnCheckedChangeListener(this);
        chkDiscomfort.setOnCheckedChangeListener(this);
        chkBodytemp.setOnCheckedChangeListener(this);

        setCheckBox();
        setItemBox();


    }

    private void setItemBox() {
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
    }

    private void setCheckBox() {
        chkTemp.setChecked(dataManager.getSelectedWeather(dataManager.WEATHER_TEMP));
        chkDust.setChecked(dataManager.getSelectedWeather(dataManager.WEATHER_DUST));
        chkWet.setChecked(dataManager.getSelectedWeather(dataManager.WEATHER_WET));
        chkWeather.setChecked(dataManager.getSelectedWeather(dataManager.WEATHER_WEATHER));
        chkDiscomfort.setChecked(dataManager.getSelectedWeather(dataManager.WEATHER_DISCOMFORT));
        chkBodytemp.setChecked(dataManager.getSelectedWeather(dataManager.WEATHER_BODYTEMP));
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
            case R.id.activity_weather_chkbox_temper:
                dataManager.setSelectedWeather(DataManager.WEATHER_TEMP, isChecked);
                setItemBox();
                break;
            case R.id.activity_weather_chkbox_dust:
                dataManager.setSelectedWeather(DataManager.WEATHER_DUST, isChecked);
                setItemBox();
                break;
            case R.id.activity_weather_chkbox_wet:
                dataManager.setSelectedWeather(DataManager.WEATHER_WET, isChecked);
                setItemBox();
                break;
            case R.id.activity_weather_chkbox_weather:
                dataManager.setSelectedWeather(DataManager.WEATHER_WEATHER, isChecked);
                setItemBox();
                break;
            case R.id.activity_weather_chkbox_discomfort:
                dataManager.setSelectedWeather(DataManager.WEATHER_DISCOMFORT, isChecked);
                setItemBox();
                break;
            case R.id.activity_weather_chkbox_bodytemp:
                dataManager.setSelectedWeather(DataManager.WEATHER_BODYTEMP, isChecked);
                setItemBox();
                break;

        }
    }
}
