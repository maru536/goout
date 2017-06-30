package com.iotaddon.goout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityDeviceSettingsSelectLed extends AppCompatActivity implements View.OnClickListener{

    private RelativeLayout led1,led2,led3;
    private TextView txt1, txt2, txt3;
    private DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings_select_led);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        getSupportActionBar().setElevation(0);

        led1 = (RelativeLayout)findViewById(R.id.activity_device_settings_select_led_1);
        led2 = (RelativeLayout)findViewById(R.id.activity_device_settings_select_led_2);
        led3 = (RelativeLayout)findViewById(R.id.activity_device_settings_select_led_3);

        txt1 = (TextView)findViewById(R.id.activity_device_settings_select_led_1_txt);
        txt2 = (TextView)findViewById(R.id.activity_device_settings_select_led_2_txt);
        txt3 = (TextView)findViewById(R.id.activity_device_settings_select_led_3_txt);

        led1.setOnClickListener(this);
        led2.setOnClickListener(this);
        led3.setOnClickListener(this);

        setSelectedText(DataManager.TYPE_LED_1, dataManager.getSelectedLed(DataManager.TYPE_LED_1), txt1);
        setSelectedText(DataManager.TYPE_LED_2, dataManager.getSelectedLed(DataManager.TYPE_LED_2), txt2);
        setSelectedText(DataManager.TYPE_LED_3, dataManager.getSelectedLed(DataManager.TYPE_LED_3), txt3);
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
    protected void onResume() {
        super.onResume();
        setSelectedText(DataManager.TYPE_LED_1, dataManager.getSelectedLed(DataManager.TYPE_LED_1), txt1);
        setSelectedText(DataManager.TYPE_LED_2, dataManager.getSelectedLed(DataManager.TYPE_LED_2), txt2);
        setSelectedText(DataManager.TYPE_LED_3, dataManager.getSelectedLed(DataManager.TYPE_LED_3), txt3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case DataManager.TYPE_LED_1:
                setSelectedText(DataManager.TYPE_LED_1, resultCode, txt1);
                dataManager.setSelectedLed(DataManager.TYPE_LED_1, resultCode);
                break;
            case DataManager.TYPE_LED_2:
                setSelectedText(DataManager.TYPE_LED_2, resultCode, txt2);
                dataManager.setSelectedLed(DataManager.TYPE_LED_2, resultCode);
                break;
            case DataManager.TYPE_LED_3:
                setSelectedText(DataManager.TYPE_LED_3, resultCode, txt3);
                dataManager.setSelectedLed(DataManager.TYPE_LED_3, resultCode);
                break;
        }
    }

    private void setSelectedText(int led, int res, TextView view){
        switch(res){
            case DataManager.TYPE_WEATHER_DUST:
                view.setText(FilterSelectedInfo.TITLE_WEATHER_DUST);
                view.setVisibility(View.VISIBLE);
                break;
            case DataManager.TYPE_WEATHER_SKY:
                view.setText(FilterSelectedInfo.TITLE_WEATHER_SKY);
                view.setVisibility(View.VISIBLE);
                break;
            case DataManager.TYPE_WEATHER_HUMIDITY:
                view.setText(FilterSelectedInfo.TITLE_WEATHER_HUMIDITY);
                view.setVisibility(View.VISIBLE);
                break;
            case DataManager.TYPE_WEATHER_PRECIPITATION:
                view.setText(FilterSelectedInfo.TITLE_WEATHER_PRECIPITATION);
                view.setVisibility(View.VISIBLE);
                break;
            case DataManager.TYPE_WEATHER_TEMP:
                view.setText(FilterSelectedInfo.TITLE_WEATHER_TEMP);
                view.setVisibility(View.VISIBLE);
                break;
            case DataManager.TYPE_WEATHER_WIND:
                view.setText(FilterSelectedInfo.TITLE_WEATHER_WIND);
                view.setVisibility(View.VISIBLE);
                break;
            case DataManager.TYPE_TRANSPORTATION_BUS:
                view.setText(FilterSelectedInfo.TITLE_TRANSPORTATION_BUS);
                view.setVisibility(View.VISIBLE);
                break;
            case DataManager.TYPE_TRANSPORTATION_SUBWAY:
                view.setText(FilterSelectedInfo.TITLE_TRANSPORTATION_SUBWAY);
                view.setVisibility(View.VISIBLE);
                break;
            case DataManager.TYPE_NONE:
                view.setVisibility(View.GONE);
                break;
        }

        DataManager.getInstance().setSelectedLed(led,res);
        AsyncTaskHttpSetConfigCommunicator asyncTaskHttpSetConfigCommunicator = new AsyncTaskHttpSetConfigCommunicator();
        asyncTaskHttpSetConfigCommunicator.execute();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()) {
            case R.id.activity_device_settings_select_led_1:
                intent = new Intent(this, ActivityDeviceSettingsSelectLedA.class);
                startActivityForResult(intent, DataManager.TYPE_LED_1);
                break;
            case R.id.activity_device_settings_select_led_2:
                intent = new Intent(this, ActivityDeviceSettingsSelectLedB.class);
                startActivityForResult(intent, DataManager.TYPE_LED_2);
                break;
            case R.id.activity_device_settings_select_led_3:
                intent = new Intent(this, ActivityDeviceSettingsSelectLedC.class);
                startActivityForResult(intent, DataManager.TYPE_LED_3);
                break;
        }
    }
}
