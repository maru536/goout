package com.iotaddon.goout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

public class ActivityDeviceSettingsConnectWIFI extends AppCompatActivity implements View.OnClickListener{

    private Button btnNext;
    private ProgressBar mProgressBar;
    private WifiManager mWifiManager;
    final static String TAG = "ConnectWifiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings_connect_wifi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        btnNext = (Button)findViewById(R.id.activity_device_settings_connect_wifi_btn_next);

        wifiAutoConnect(initWifiConfig("Wiznet_TestAP", "12345678"));

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

    private void wifiAutoConnect(WifiConfiguration wifiConfig) {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
            while (!mWifiManager.isWifiEnabled()) {

            }
        }

        int networkID = mWifiManager.addNetwork(wifiConfig);
        if (networkID >= 0) {
            mWifiManager.enableNetwork(networkID, true);
        }
    }

    private WifiConfiguration initWifiConfig(String ssid, String pwd) {
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = "\"".concat(ssid).concat("\"");
        wifiConfig.status = WifiConfiguration.Status.DISABLED;
        wifiConfig.priority = 40;
        wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfig.preSharedKey = "\"".concat(pwd).concat("\"");

        return wifiConfig;
    }
}
