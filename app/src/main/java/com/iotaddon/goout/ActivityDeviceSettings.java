package com.iotaddon.goout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class ActivityDeviceSettings extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_LOCATION = 1;
    private static final int PLACE_PICKER_REQUEST = 1;


    private RelativeLayout btnVoice, btnWIFI, btnPlace;
    private CheckBox chkAlarm;
    private TextView txtPlace;
    private DataManager dataManager = DataManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        getSupportActionBar().setElevation(0);

        btnVoice = (RelativeLayout) findViewById(R.id.activity_device_settings_relative_select_led);
        btnWIFI = (RelativeLayout) findViewById(R.id.activity_device_settings_relative_wifi_connect);
        btnPlace = (RelativeLayout) findViewById(R.id.activity_device_settings_relative_placepicker);

        txtPlace = (TextView)findViewById(R.id.activity_device_settings_txt_place);

        chkAlarm = (CheckBox)findViewById(R.id.activity_device_settings_chk_alarm);

        chkAlarm.setChecked(dataManager.isOutAlarm());
        chkAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dataManager.setOutAlarm(isChecked);
            }
        });

        btnVoice.setOnClickListener(this);
        btnWIFI.setOnClickListener(this);
        btnPlace.setOnClickListener(this);


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
        switch (v.getId()) {
            case R.id.activity_device_settings_relative_wifi_connect:
                intent = new Intent(this, ActivityDeviceSettingsConnectWIFI.class);
                startActivity(intent);
                break;
            case R.id.activity_device_settings_relative_select_led:
                intent = new Intent(this, ActivityDeviceSettingsSelectLed.class);
                startActivity(intent);
                break;
            case R.id.activity_device_settings_relative_placepicker:
                int permissionLocation = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                } else {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                            try {
                                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                            } catch (GooglePlayServicesRepairableException e) {
                                e.printStackTrace();
                            } catch (GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                txtPlace.setText(place.getName());
                txtPlace.setVisibility(View.VISIBLE);
                dataManager.setUserAddress(place.getLatLng().latitude,place.getLatLng().longitude,place.getName().toString());
            }
        }
    }
}
