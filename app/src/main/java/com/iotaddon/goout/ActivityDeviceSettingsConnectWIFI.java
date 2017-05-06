package com.iotaddon.goout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ActivityDeviceSettingsConnectWIFI extends AppCompatActivity implements View.OnClickListener{

    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings_connect_wifi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnNext = (Button)findViewById(R.id.activity_device_settings_connect_wifi_btn_next);
        btnNext.setOnClickListener(this);
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
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.activity_device_settings_connect_wifi_btn_next:
                intent = new Intent(this,ActivityDeviceSettingsConfigWIFI.class);
                startActivity(intent);
                break;

        }
    }
}
