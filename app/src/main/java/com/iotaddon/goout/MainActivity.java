package com.iotaddon.goout;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    private RelativeLayout itemBox[] = new RelativeLayout[7];
    private LinearLayout drawerMenu[] = new LinearLayout[4];
    private TextView itemTxt[][] = new TextView[7][7];
    private DataManager dataManager = DataManager.getInstance();
    private WeatherDataUpdateListener weatherListener;
    private final int ITEM_NUM_MAXIMUM = 6;

    private RelativeLayout contentWeather, contentTransportation, contentMemo;
    private TextView txtGuide, txtMemoContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        weatherListener = new WeatherDataUpdateListener() {
            @Override
            public void doUpdate() {
                Log.e("update log","log check");
                setWeatherContent();
                setContentTransportation();
                setContentMemo();
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER);
                asyncTaskHttpCommunicator.setListener(weatherListener);
                asyncTaskHttpCommunicator.execute();
            }
        });

        contentWeather = (RelativeLayout) findViewById(R.id.content_weather_container);
        contentTransportation = (RelativeLayout) findViewById(R.id.content_transportation_container);
        contentMemo = (RelativeLayout) findViewById(R.id.content_memo_container);

        txtGuide = (TextView) findViewById(R.id.activity_main_txt);
        txtMemoContent = (TextView) findViewById(R.id.content_memo_txt);

        itemBox[0] = (RelativeLayout) findViewById(R.id.activity_weather_item0);
        itemBox[1] = (RelativeLayout) findViewById(R.id.activity_weather_item1);
        itemBox[2] = (RelativeLayout) findViewById(R.id.activity_weather_item2);
        itemBox[3] = (RelativeLayout) findViewById(R.id.activity_weather_item3);
        itemBox[4] = (RelativeLayout) findViewById(R.id.activity_weather_item4);
        itemBox[5] = (RelativeLayout) findViewById(R.id.activity_weather_item5);
        itemBox[6] = (RelativeLayout) findViewById(R.id.activity_weather_item6);

        itemTxt[1][1] = (TextView)findViewById(R.id.content_weather_txt_1_1);
        itemTxt[2][1] = (TextView)findViewById(R.id.content_weather_txt_2_1);
        itemTxt[2][2] = (TextView)findViewById(R.id.content_weather_txt_2_1);
        itemTxt[3][1] = (TextView)findViewById(R.id.content_weather_txt_3_1);
        itemTxt[3][2] = (TextView)findViewById(R.id.content_weather_txt_3_2);
        itemTxt[3][3] = (TextView)findViewById(R.id.content_weather_txt_3_3);
        itemTxt[4][1] = (TextView)findViewById(R.id.content_weather_txt_4_1);
        itemTxt[4][2] = (TextView)findViewById(R.id.content_weather_txt_4_2);
        itemTxt[4][3] = (TextView)findViewById(R.id.content_weather_txt_4_3);
        itemTxt[4][4] = (TextView)findViewById(R.id.content_weather_txt_4_4);
        itemTxt[5][1] = (TextView)findViewById(R.id.content_weather_txt_5_1);
        itemTxt[5][2] = (TextView)findViewById(R.id.content_weather_txt_5_2);
        itemTxt[5][3] = (TextView)findViewById(R.id.content_weather_txt_5_3);
        itemTxt[5][4] = (TextView)findViewById(R.id.content_weather_txt_5_4);
        itemTxt[5][5] = (TextView)findViewById(R.id.content_weather_txt_5_5);
        itemTxt[6][1] = (TextView)findViewById(R.id.content_weather_txt_6_1);
        itemTxt[6][2] = (TextView)findViewById(R.id.content_weather_txt_6_2);
        itemTxt[6][3] = (TextView)findViewById(R.id.content_weather_txt_6_3);
        itemTxt[6][4] = (TextView)findViewById(R.id.content_weather_txt_6_4);
        itemTxt[6][5] = (TextView)findViewById(R.id.content_weather_txt_6_5);
        itemTxt[6][6] = (TextView)findViewById(R.id.content_weather_txt_6_6);



        AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER);
        asyncTaskHttpCommunicator.setListener(weatherListener);
        asyncTaskHttpCommunicator.execute();
        setContentTransportation();
        setContentMemo();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawerMenu[0] = (LinearLayout)findViewById(R.id.drawer_menu_weather);
        drawerMenu[1] = (LinearLayout)findViewById(R.id.drawer_menu_transportation);
        drawerMenu[2] = (LinearLayout)findViewById(R.id.drawer_menu_memo);
        drawerMenu[3] = (LinearLayout)findViewById(R.id.drawer_menu_settings);

        drawerMenu[0].setOnClickListener(this);
        drawerMenu[1].setOnClickListener(this);
        drawerMenu[2].setOnClickListener(this);
        drawerMenu[3].setOnClickListener(this);

    }

    private void setContentTransportation() {
        if (dataManager.getSelectedTransportation() != dataManager.TRANSPORTATION_NONE) {
            contentTransportation.setVisibility(View.VISIBLE);
            txtGuide.setVisibility(View.INVISIBLE);
        } else {
            contentTransportation.setVisibility(View.GONE);
            if (contentWeather.getVisibility() == View.GONE && contentMemo.getVisibility() == View.GONE) {
                txtGuide.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setContentMemo() {
        if (!dataManager.getSavedMemo().equals("")) {
            txtMemoContent.setText(dataManager.getSavedMemo());
            contentMemo.setVisibility(View.VISIBLE);
            txtGuide.setVisibility(View.INVISIBLE);
        } else {
            contentMemo.setVisibility(View.GONE);
            if (contentWeather.getVisibility() == View.GONE && contentTransportation.getVisibility() == View.GONE) {
                txtGuide.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setWeatherContent() {
        int itemNum = 0;
        for (int i = 0; i <= ITEM_NUM_MAXIMUM; i++) {
            itemBox[i].setVisibility(View.INVISIBLE);
            if (dataManager.getSelectedWeather(i)) {
                itemNum++;
            }
        }

        if (itemNum > 0) {
            contentWeather.setVisibility(View.VISIBLE);
            txtGuide.setVisibility(View.INVISIBLE);
        } else {
            contentWeather.setVisibility(View.GONE);
            if (contentMemo.getVisibility() == View.GONE && contentTransportation.getVisibility() == View.GONE) {
                txtGuide.setVisibility(View.VISIBLE);
            }
        }
        checkWeatherIndex(itemNum);
        itemBox[itemNum].setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setWeatherContent();
        setContentTransportation();
        setContentMemo();
    }

    public void checkWeatherIndex(int m){
        int c = 0;
        for(int i=0;i<=ITEM_NUM_MAXIMUM;i++){
            if(c>m)
                break;
            if(dataManager.getSelectedWeather(i)){
                c++;
                if(i==dataManager.WEATHER_BODYTEMP){
                    itemTxt[m][c].setText(dataManager.getDataWeather().getDataWeatherTemperature().getTc()+"");
                }else if(i==dataManager.WEATHER_DISCOMFORT){
                    itemTxt[m][c].setText(dataManager.getDataWeather().getDataWeatherRain().getSinceOntime()+"");
                }else if(i==dataManager.WEATHER_DUST){
                    itemTxt[m][c].setText(dataManager.getDataWeather().getDataWeatherSky().getName()+"");
                }else if(i==dataManager.WEATHER_TEMP){
                    itemTxt[m][c].setText(dataManager.getDataWeather().getDataWeatherTemperature().getTc()+"");
                }else if(i==dataManager.WEATHER_WEATHER){
                    itemTxt[m][c].setText(dataManager.getDataWeather().getDataWeatherSky().getName()+"");
                }else if(i==dataManager.WEATHER_WET){
                    itemTxt[m][c].setText(dataManager.getDataWeather().getDataWeatherHumidity().getHumidity()+"");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        DrawerLayout drawer;
        Intent intent;

        switch(v.getId()){
            case R.id.drawer_menu_weather:
                intent = new Intent(this, ActivityWeather.class);
                startActivity(intent);
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.drawer_menu_transportation:
                intent = new Intent(this, ActivityTransportation.class);
                startActivity(intent);
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.drawer_menu_memo:
                intent = new Intent(this, ActivityMemo.class);
                startActivity(intent);
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.drawer_menu_settings:
                intent = new Intent(this, ActivityDeviceSettings.class);
                startActivity(intent);
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
    }

}
