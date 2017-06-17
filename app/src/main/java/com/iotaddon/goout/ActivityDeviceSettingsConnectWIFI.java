package com.iotaddon.goout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

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

    final static String TAG = "ConnectWifiActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings_connect_wifi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                                //mServerComm.regist(deviceID, recvMessage);
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
}
