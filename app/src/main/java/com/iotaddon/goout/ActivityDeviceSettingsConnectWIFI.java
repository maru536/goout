package com.iotaddon.goout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;


public class ActivityDeviceSettingsConnectWIFI extends AppCompatActivity implements View.OnClickListener{

    private Button btnNext;
    private WifiManager mWifiManager;
    private EditText mETSsid;
    private EditText mETPwd;
    private String mServerIP = null;
    private Socket mSocket = null;
    private PrintWriter mOut;
    private BufferedReader mIn;
    private Thread mReceiverThread = null;
    private boolean isConnected = false;
    private ServerComm mServerComm = ServerComm.getInstance();
    private String deviceID;
    private int mWifiID = -1;

    private static final int INTERNET_REQUEST_CODE = 101;
    private static final int ACCESS_NETWORK_STATE_REQUEST_CODE = 102;
    private static final int CHANGE_WIFI_STATE_REQUEST_CODE = 103;
    private static final int ACCESS_WIFI_STATE_REQUEST_CODE = 104;
    private static final int READ_PHONE_STATE_REQUEST_CODE = 105;
    private static final int READ_CONTACTS_REQUEST_CODE = 106;
    private static final int WIFI_CONNECT = 2;
    private static final String WIFI_SSID = "Wiznet_TestAP";
    final static String TAG = "ConnectWifiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings_connect_wifi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("tag", "Permission to record denied");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_REQUEST_CODE);
        }

        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("tag", "Permission to record denied");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, ACCESS_NETWORK_STATE_REQUEST_CODE);
        }

        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("tag", "Permission to record denied");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, CHANGE_WIFI_STATE_REQUEST_CODE);
        }

        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("tag", "Permission to record denied");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, ACCESS_WIFI_STATE_REQUEST_CODE);
        }

        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("tag", "Permission to record denied");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_REQUEST_CODE);
        }

        permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("tag", "Permission to record denied");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
        }


        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        mWifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mETSsid = (EditText)findViewById(R.id.et_ssid);
        mETPwd = (EditText)findViewById(R.id.et_pwd);
        btnNext = (Button)findViewById(R.id.activity_device_settings_connect_wifi_btn_connect);

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
        switch(v.getId()){
            case R.id.activity_device_settings_connect_wifi_btn_connect:
                List<WifiConfiguration> listWifiConfig = mWifiManager.getConfiguredNetworks();
                if (listWifiConfig != null) {
                    WifiConfiguration connectedWifi = new WifiConfiguration();
                    if (listWifiConfig != null) {
                        for (int i = 0; i < listWifiConfig.size(); i++) {
                            if(listWifiConfig.get(i).SSID.equals("\""+WIFI_SSID+"\""))
                                connectedWifi = listWifiConfig.get(i);
                        }
                        Log.d("KimDC", "\""+WIFI_SSID+"\"");
                        Log.d("KimDC", "SSID: "+connectedWifi.SSID);
                        Log.d("KimDC", "status: "+Integer.toString(connectedWifi.status));
                        Log.d("KimDC", "wifiStatus: "+Integer.toString(mWifiManager.getWifiState()));
                        Log.d("KimDC", "wifiEnable: "+Boolean.toString(mWifiManager.isWifiEnabled()));
                        Log.d("KimDC", "pingsup: "+Boolean.toString(mWifiManager.pingSupplicant()));
                    }

                    if (connectedWifi.SSID.equals("\""+WIFI_SSID+"\"")) {
                        //Device AP의 Mac 주소 획득
                        String ssid = mETSsid.getText().toString();
                        String pwd = mETPwd.getText().toString();

                        HashMap apInfo = new HashMap();

                        apInfo.put("ssid", ssid);
                        apInfo.put("pwd", pwd);

                        JSONObject jsonAp = new JSONObject(apInfo);

                        new Thread(new ConnectThread("192.168.10.1", 8080)).start();

                        while (!isConnected) {
                            //wait socket open
                        }
                        Log.d(TAG, jsonAp.toString());

                        new Thread(new SenderThread(jsonAp.toString())).start();
                    }
                    else {

                    }
                }

                Toast toast = Toast.makeText(getApplicationContext(),
                        "화려한 외출 준비 완료", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                this.finish();

                break;

        }
    }

    private void wifiAutoConnect(WifiConfiguration wifiConfig) {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
            while (!mWifiManager.isWifiEnabled()) {

            }
        }

        mWifiID = -1;
        mWifiID = mWifiManager.addNetwork(wifiConfig);
        if (mWifiID >= 0) {
            Log.d("KimDC", Integer.toString(mWifiID));
            mWifiManager.enableNetwork(mWifiID, true);
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

    private class ConnectThread implements Runnable {

        private String serverIP;
        private int serverPort;

        ConnectThread(String ip, int port) {
            serverIP = ip;
            serverPort = port;
        }

        @Override
        public void run() {

            try {
                mSocket = new Socket(serverIP, serverPort);
                //ReceiverThread: java.net.SocketTimeoutException: Read timed out 때문에 주석처리
                //mSocket.setSoTimeout(3000);

                mServerIP = mSocket.getRemoteSocketAddress().toString();

            } catch( UnknownHostException e )
            {
                Log.d(TAG,  "ConnectThread: can't find host");
            }
            catch( SocketTimeoutException e )
            {
                Log.d(TAG, "ConnectThread: timeout");
            }
            catch (Exception e) {

                Log.e(TAG, ("ConnectThread:" + e.getMessage()));
            }


            if (mSocket != null) {
                try {
                    mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream(), "UTF-8")), true);
                    mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF-8"));
                    isConnected = true;
                } catch (IOException e) {

                    Log.e(TAG, ("ConnectThread:" + e.getMessage()));
                }
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isConnected) {
                        Log.d(TAG, "connected to " + serverIP);
                        Log.d(TAG, "ReceiverThread Start");

                        mReceiverThread = new Thread(new ReceiverThread());
                        mReceiverThread.start();
                    }else{

                        Log.d(TAG, "failed to connect to server " + serverIP);
                    }

                }
            });
        }
    }

    private class SenderThread implements Runnable {

        private String msg;

        SenderThread(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {

            mOut.println(this.msg);
            mOut.flush();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "send message: " + msg);
                }
            });
        }
    }


    private class ReceiverThread implements Runnable {

        @Override
        public void run() {

            try {
                Log.d(TAG, "ReveiverThread run!");

                while (isConnected) {
                    final String recvMessage =  mIn.readLine();

                    if (recvMessage != null) {
                        Log.d(TAG, "ReceiverThread: mIn isn't null");
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                Log.d(TAG, "recv message: "+recvMessage);
                                mServerComm.regist(deviceID, recvMessage);
                            }
                        });
                    }
                }

                Log.d(TAG, "ReceiverThread: thread has exited");
                if (mOut != null) {
                    mOut.flush();
                    mOut.close();
                }

                mIn = null;
                mOut = null;

                if (mSocket != null) {
                    try {
                        mSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "ReceiverThread: "+ e);
            }
            catch (Exception ee) {
                ee.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case INTERNET_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("tag", "INTERNET Permission has been denied by user");
                }
                else {
                    Log.i("tag", "INTERNET Permission has been granted by user");
                }
            }

            case ACCESS_NETWORK_STATE_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("tag", "ACCESS_NETWORK_STATE Permission has been denied by user");
                }
                else {
                    Log.i("tag", "ACCESS_NETWORK_STATE Permission has been granted by user");
                }
            }

            case CHANGE_WIFI_STATE_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("tag", "CHANGE_WIFI_STATE Permission has been denied by user");
                }
                else {
                    Log.i("tag", "CHANGE_WIFI_STATE Permission has been granted by user");
                }
            }

            case ACCESS_WIFI_STATE_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("tag", "ACCESS_WIFI_STATE Permission has been denied by user");
                }
                else {
                    Log.i("tag", "ACCESS_WIFI_STATE Permission has been granted by user");
                }
            }

            case READ_PHONE_STATE_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("tag", "READ_PHONE_STATE Permission has been denied by user");
                }
                else {
                    Log.i("tag", "READ_PHONE_STATE Permission has been granted by user");
                }
            }

            case READ_CONTACTS_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i("tag", "READ_CONTACTS Permission has been denied by user");
                }
                else {
                    Log.i("tag", "READ_CONTACTS Permission has been granted by user");
                }
            }
        }
    }
}
