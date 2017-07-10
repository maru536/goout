package com.iotaddon.goout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityTransportation extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout txtBus, txtSubway;
    private DataManager dataManager = DataManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        getSupportActionBar().setElevation(0);

        txtBus = (RelativeLayout) findViewById(R.id.activity_transportation_txt_bus);
        txtSubway = (RelativeLayout) findViewById(R.id.activity_transportation_txt_subway);

        txtBus.setOnClickListener(this);
        txtSubway.setOnClickListener(this);

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
        if(requestCode == dataManager.TYPE_TRANSPORTATION_BUS){
            if(resultCode == RESULT_OK){
                finish();
            }
        }else if(requestCode == dataManager.TYPE_TRANSPORTATION_SUBWAY){
            if(resultCode == RESULT_OK){
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if(dataManager.getUserAddress().getLatitude()==0){
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("내 위치 설정하기");
            gsDialog.setMessage("날씨 정보를 사용하기 위해서 위치를 설정해야 합니다. 지금 위치설정을 하시겠습니까?");
            gsDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(getApplicationContext(), ActivityDeviceSettings.class);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return;
        }
        switch (v.getId()) {
            case R.id.activity_transportation_txt_bus:
                intent = new Intent(this, ActivityTransportationBus.class);
                startActivityForResult(intent, dataManager.TYPE_TRANSPORTATION_BUS);
                break;
            case R.id.activity_transportation_txt_subway:
                intent = new Intent(this, ActivityTransportationSubway.class);
                startActivityForResult(intent, dataManager.TYPE_TRANSPORTATION_SUBWAY);
                break;
        }
    }
}
