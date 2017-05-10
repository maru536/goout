package com.iotaddon.goout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class ActivityTransportation extends AppCompatActivity implements View.OnClickListener {

    private RadioButton radioNone, radioBus, radioSubway;
    private boolean radioArr[] = new boolean[3];
    private DataManager dataManager = DataManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioNone = (RadioButton) findViewById(R.id.activity_transportation_radio_none);
        radioBus = (RadioButton) findViewById(R.id.activity_transportation_radio_bus);
        radioSubway = (RadioButton) findViewById(R.id.activity_transportation_radio_subway);

        radioNone.setOnClickListener(this);
        radioBus.setOnClickListener(this);
        radioSubway.setOnClickListener(this);

        setRadioButton();
    }

    private void setRadioButton(){
        if(dataManager.getSelectedTransportation() == dataManager.TRANSPORTATION_NONE){
            radioNone.setChecked(true);
            radioBus.setChecked(false);
            radioSubway.setChecked(false);
        }else if(dataManager.getSelectedTransportation() == dataManager.TRANSPORTATION_BUS){
            radioNone.setChecked(false);
            radioBus.setChecked(true);
            radioSubway.setChecked(false);
        }else if(dataManager.getSelectedTransportation() == dataManager.TRANSPORTATION_SUBWAY){
            radioNone.setChecked(false);
            radioBus.setChecked(false);
            radioSubway.setChecked(true);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == dataManager.TRANSPORTATION_BUS){
            if (resultCode == RESULT_OK) {
                radioArr[dataManager.TRANSPORTATION_BUS] = true;
                radioArr[dataManager.TRANSPORTATION_SUBWAY] = false;
                radioArr[dataManager.TRANSPORTATION_NONE] = false;
                radioSubway.setChecked(false);
                radioBus.setChecked(true);
                radioNone.setChecked(false);

                dataManager.setSelectedTransportation(requestCode);
            }

        }else if(requestCode == dataManager.TRANSPORTATION_SUBWAY){
            if (resultCode == RESULT_OK) {
                radioArr[dataManager.TRANSPORTATION_BUS] = false;
                radioArr[dataManager.TRANSPORTATION_SUBWAY] = true;
                radioArr[dataManager.TRANSPORTATION_NONE] = false;
                radioSubway.setChecked(true);
                radioBus.setChecked(false);
                radioNone.setChecked(false);

                dataManager.setSelectedTransportation(requestCode);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.activity_transportation_radio_none:
                radioArr[dataManager.TRANSPORTATION_BUS] = false;
                radioArr[dataManager.TRANSPORTATION_SUBWAY] = false;
                radioArr[dataManager.TRANSPORTATION_NONE] = true;
                radioSubway.setChecked(false);
                radioBus.setChecked(false);
                radioNone.setChecked(true);
                dataManager.setSelectedTransportation(dataManager.TRANSPORTATION_NONE);
                break;
            case R.id.activity_transportation_radio_bus:
                intent = new Intent(this, ActivityTransportationBus.class);
                startActivityForResult(intent, dataManager.TRANSPORTATION_BUS);
                break;
            case R.id.activity_transportation_radio_subway:
                intent = new Intent(this, ActivityTransportationSubway.class);
                startActivityForResult(intent, dataManager.TRANSPORTATION_SUBWAY);
                break;
        }
    }
}
