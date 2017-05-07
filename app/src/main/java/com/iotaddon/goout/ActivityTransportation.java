package com.iotaddon.goout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class ActivityTransportation extends AppCompatActivity implements View.OnClickListener{

    private RadioButton radioNone, radioBus, radioSubway;
    private boolean radioArr[] = new boolean[3];

    private final int idx_none = 0;
    private final int idx_bus = 1;
    private final int idx_subway = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        radioNone = (RadioButton)findViewById(R.id.activity_transportation_radio_none);
        radioBus = (RadioButton)findViewById(R.id.activity_transportation_radio_bus);
        radioSubway = (RadioButton)findViewById(R.id.activity_transportation_radio_subway);

        radioNone.setOnClickListener(this);
        radioBus.setOnClickListener(this);
        radioSubway.setOnClickListener(this);
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

        switch(requestCode){
            case idx_bus:
                if(resultCode==RESULT_OK){
                    radioArr[idx_bus] = true;
                    radioArr[idx_subway] = false;
                    radioArr[idx_none] = false;
                    radioSubway.setChecked(false);
                    radioBus.setChecked(true);
                    radioNone.setChecked(false);
                }
                break;
            case idx_subway:
                if(resultCode==RESULT_OK){
                    radioArr[idx_bus] = false;
                    radioArr[idx_subway] = true;
                    radioArr[idx_none] = false;
                    radioSubway.setChecked(true);
                    radioBus.setChecked(false);
                    radioNone.setChecked(false);
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.activity_transportation_radio_none:
                radioArr[idx_bus] = false;
                radioArr[idx_subway] = false;
                radioArr[idx_none] = true;
                radioSubway.setChecked(false);
                radioBus.setChecked(false);
                radioNone.setChecked(true);
                Log.e("check","none");
                break;
            case R.id.activity_transportation_radio_bus:
                intent = new Intent(this,ActivityTransportationBus.class);
                startActivityForResult(intent,idx_bus);
                break;
            case R.id.activity_transportation_radio_subway:
                intent = new Intent(this,ActivityTransportationSubway.class);
                startActivityForResult(intent,idx_subway);
                break;
        }
    }
}
