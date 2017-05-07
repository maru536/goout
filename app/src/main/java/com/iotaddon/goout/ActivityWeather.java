package com.iotaddon.goout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class ActivityWeather extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private final int idx_temp = 0;
    private final int idx_dust = 1;
    private final int idx_wet = 2;
    private final int idx_discomfort = 3;
    private final int idx_weather = 4;
    private final int idx_bodytemp = 5;

    private boolean checkedArr[] = new boolean[6];
    private CheckBox chkTemp, chkDust, chkWet, chkDiscomfort, chkWeather, chkBodytemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chkTemp = (CheckBox)findViewById(R.id.activity_weather_chkbox_temper);
        chkDust = (CheckBox)findViewById(R.id.activity_weather_chkbox_dust);
        chkWet = (CheckBox)findViewById(R.id.activity_weather_chkbox_wet);
        chkWeather = (CheckBox)findViewById(R.id.activity_weather_chkbox_weather);
        chkDiscomfort = (CheckBox)findViewById(R.id.activity_weather_chkbox_discomfort);
        chkBodytemp = (CheckBox)findViewById(R.id.activity_weather_chkbox_bodytemp);

        chkTemp.setOnCheckedChangeListener(this);
        chkDust.setOnCheckedChangeListener(this);
        chkWeather.setOnCheckedChangeListener(this);
        chkWet.setOnCheckedChangeListener(this);
        chkDiscomfort.setOnCheckedChangeListener(this);
        chkBodytemp.setOnCheckedChangeListener(this);


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
        switch(buttonView.getId()){
            case R.id.activity_weather_chkbox_temper:
                checkedArr[idx_temp] = isChecked;
                break;
            case R.id.activity_weather_chkbox_dust:
                checkedArr[idx_temp] = isChecked;
                break;
            case R.id.activity_weather_chkbox_wet:
                checkedArr[idx_temp] = isChecked;
                break;
            case R.id.activity_weather_chkbox_weather:
                checkedArr[idx_temp] = isChecked;
                break;
            case R.id.activity_weather_chkbox_discomfort:
                checkedArr[idx_temp] = isChecked;
                break;
            case R.id.activity_weather_chkbox_bodytemp:
                checkedArr[idx_temp] = isChecked;
                break;

        }
    }
}
