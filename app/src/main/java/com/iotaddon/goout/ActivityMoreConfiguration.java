package com.iotaddon.goout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class ActivityMoreConfiguration extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView imgArrow;
    private TextView txtValue, txtUnit;
    private LinearLayout containerSky, containerOther;
    private RadioButton radioSunny, radioBlue, radioRain;
    private int type;
    private DataManager dataManager = DataManager.getInstance();
    private boolean isHigher;
    private double warningValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_configuration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        getSupportActionBar().setElevation(0);

        containerOther = (LinearLayout)findViewById(R.id.activity_more_config_container_seekbar);
        containerSky = (LinearLayout)findViewById(R.id.activity_more_config_container_weather);

        radioSunny = (RadioButton)findViewById(R.id.activity_more_config_weather_radio_sunny);
        radioBlue = (RadioButton)findViewById(R.id.activity_more_config_weather_radio_blue);
        radioRain = (RadioButton)findViewById(R.id.activity_more_config_weather_radio_rain);

        seekBar = (SeekBar)findViewById(R.id.activity_more_config_seekbar);
        imgArrow = (TextView)findViewById(R.id.activity_more_config_arrow);
        txtUnit = (TextView)findViewById(R.id.activity_more_config_txt_unit);
        txtValue = (TextView)findViewById(R.id.activity_more_config_txt_value);

        Intent intent = getIntent();
        type = intent.getIntExtra("INFO_TYPE",0);

        if(type==dataManager.TYPE_WEATHER_SKY){
            containerSky.setVisibility(View.VISIBLE);
            containerOther.setVisibility(View.GONE);
            setRadioChecked(dataManager.getDataWeather().getDataWeatherSky().getWarningValue());
        }else{
            containerSky.setVisibility(View.GONE);
            containerOther.setVisibility(View.VISIBLE);
            setSeekBarProperty(type);
        }

        radioSunny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioChecked(dataManager.getDataWeather().getDataWeatherSky().WARNING_SKY_SUNNY);
            }
        });
        radioRain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioChecked(dataManager.getDataWeather().getDataWeatherSky().WARNING_SKY_RAIN_SNOW);
            }
        });
        radioBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioChecked(dataManager.getDataWeather().getDataWeatherSky().WARNING_SKY_BLUE);
            }
        });

        imgArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHigher = !isHigher;
                setWarningValue(type, warningValue, isHigher);
                setArrowImage(isHigher);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch(type){
                    case DataManager.TYPE_WEATHER_HUMIDITY:
                        txtValue.setText(progress+"");
                        warningValue = progress;
                        break;
                    case DataManager.TYPE_WEATHER_SKY:

                        break;
                    case DataManager.TYPE_WEATHER_PRECIPITATION:
                        warningValue = convertIntToDouble(progress);
                        txtValue.setText(warningValue+"");
                        break;
                    case DataManager.TYPE_WEATHER_WIND:
                        warningValue = convertIntToDouble(progress);
                        txtValue.setText(warningValue+"");
                        break;
                    case DataManager.TYPE_WEATHER_DUST:
                        txtValue.setText(progress+"");
                        warningValue = progress;
                        break;
                    case DataManager.TYPE_WEATHER_TEMP:
                        warningValue = convertPlusToMinus(progress);
                        txtValue.setText((int)warningValue+"");
                        break;
                    case DataManager.TYPE_TRANSPORTATION_BUS:
                        break;
                    case DataManager.TYPE_TRANSPORTATION_SUBWAY:
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setWarningValue(type, warningValue, isHigher);
            }
        });

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

    private void setRadioChecked(int what){
        DataWeather dataWeather = dataManager.getDataWeather();
        switch(what){
            case 0:
                radioSunny.setChecked(true);
                radioRain.setChecked(false);
                radioBlue.setChecked(false);
                dataWeather.getDataWeatherSky().setWarningValue(dataWeather.getDataWeatherSky().WARNING_SKY_SUNNY);
                break;
            case 1:
                radioSunny.setChecked(false);
                radioRain.setChecked(false);
                radioBlue.setChecked(true);
                dataWeather.getDataWeatherSky().setWarningValue(dataWeather.getDataWeatherSky().WARNING_SKY_BLUE);
                break;
            case 2:
                radioSunny.setChecked(false);
                radioRain.setChecked(true);
                radioBlue.setChecked(false);
                dataWeather.getDataWeatherSky().setWarningValue(dataWeather.getDataWeatherSky().WARNING_SKY_RAIN_SNOW);
                break;
        }
    }

    private void setSeekBarProperty(int type){
        DataWeather dataWeather = dataManager.getDataWeather();
        switch(type){
            case DataManager.TYPE_WEATHER_HUMIDITY:
                seekBar.setMax((dataWeather.getDataWeatherHumidity().MAX_VALUE_WEATHER_HUMIDITY));
                txtUnit.setText(dataWeather.getDataWeatherHumidity().UNIT_WEATHER_HUMIDITY);
                seekBar.setProgress(dataWeather.getDataWeatherHumidity().getWarningValue());
                isHigher = dataWeather.getDataWeatherHumidity().isHigher();
                txtValue.setText(dataWeather.getDataWeatherHumidity().getWarningValue()+"");
                setArrowImage(isHigher);
                break;
            case DataManager.TYPE_WEATHER_PRECIPITATION:
                seekBar.setMax((dataWeather.getDataWeatherPrecipitation().MAX_VALUE_WEATHER_PRECIPITATION));
                txtUnit.setText(dataWeather.getDataWeatherPrecipitation().UNIT_WEATHER_PRECIPITATION);
                seekBar.setProgress(convertDoubleToInt(dataWeather.getDataWeatherPrecipitation().getWarningValue()));
                isHigher = dataWeather.getDataWeatherPrecipitation().isHigher();
                txtValue.setText(dataWeather.getDataWeatherPrecipitation().getWarningValue()+"");
                setArrowImage(isHigher);
                break;
            case DataManager.TYPE_WEATHER_WIND:
                seekBar.setMax((dataWeather.getDataWeatherWind().MAX_VALUE_WEATHER_WIND));
                txtUnit.setText(dataWeather.getDataWeatherWind().UNIT_WEATHER_WIND);
                seekBar.setProgress(convertDoubleToInt(dataWeather.getDataWeatherWind().getWarningValue()));
                isHigher = dataWeather.getDataWeatherWind().isHigher();
                txtValue.setText(dataWeather.getDataWeatherWind().getWarningValue()+"");
                setArrowImage(isHigher);
                break;
            case DataManager.TYPE_WEATHER_DUST:
                seekBar.setMax((dataWeather.getDataWeatherDust().MAX_VALUE_WEATHER_DUST));
                txtUnit.setText(dataWeather.getDataWeatherDust().UNIT_WEATHER_DUST);
                seekBar.setProgress(dataWeather.getDataWeatherDust().getWarningValue());
                isHigher = dataWeather.getDataWeatherHumidity().isHigher();
                txtValue.setText(dataWeather.getDataWeatherDust().getWarningValue()+"");
                setArrowImage(isHigher);
                break;
            case DataManager.TYPE_WEATHER_TEMP:
                seekBar.setMax((dataWeather.getDataWeatherTemperature().MAX_VALUE_WEATHER_TEMP));
                txtUnit.setText(dataWeather.getDataWeatherTemperature().UNIT_WEATHER_TEMP);
                seekBar.setProgress(convertMinusToPlus(dataWeather.getDataWeatherTemperature().getWarningValue()));
                txtValue.setText(dataWeather.getDataWeatherTemperature().getWarningValue()+"");
                isHigher = dataWeather.getDataWeatherTemperature().isHigher();
                setArrowImage(isHigher);
                break;
            case DataManager.TYPE_TRANSPORTATION_BUS:
                break;
            case DataManager.TYPE_TRANSPORTATION_SUBWAY:
                break;
        }
    }



    private double convertIntToDouble(int value){
        return value/(double)100;
    }

    private int convertDoubleToInt(double value){
        return ((int)(value*100));
    }

    private int convertMinusToPlus(int value){
        return value + 100;
    }

    private int convertPlusToMinus(int value){
        return value - 100;
    }

    private void setWarningValue(int type, double value, boolean isHigher){
        DataWeather dataWeather = dataManager.getDataWeather();
        switch(type){
            case DataManager.TYPE_WEATHER_HUMIDITY:
                dataWeather.getDataWeatherHumidity().setWarningValue((int)value);
                dataWeather.getDataWeatherHumidity().setHigher(isHigher);
                break;
            case DataManager.TYPE_WEATHER_PRECIPITATION:
                dataWeather.getDataWeatherPrecipitation().setWarningValue(value);
                dataWeather.getDataWeatherPrecipitation().setHigher(isHigher);
                break;
            case DataManager.TYPE_WEATHER_WIND:
                dataWeather.getDataWeatherWind().setWarningValue(value);
                dataWeather.getDataWeatherWind().setHigher(isHigher);
                break;
            case DataManager.TYPE_WEATHER_DUST:
                dataWeather.getDataWeatherDust().setWarningValue((int)value);
                dataWeather.getDataWeatherDust().setHigher(isHigher);
                break;
            case DataManager.TYPE_WEATHER_TEMP:
                dataWeather.getDataWeatherTemperature().setWarningValue((int)value);
                dataWeather.getDataWeatherTemperature().setHigher(isHigher);
                break;
            case DataManager.TYPE_TRANSPORTATION_BUS:
                break;
            case DataManager.TYPE_TRANSPORTATION_SUBWAY:
                break;
        }
    }

    private void setArrowImage(boolean isHigher){
        if(isHigher){
            imgArrow.setText("높을 때 ");
        }else{
            imgArrow.setText("낮을 때 ");
        }
    }
}
