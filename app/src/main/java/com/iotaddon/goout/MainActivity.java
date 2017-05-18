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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RelativeLayout itemBox[] = new RelativeLayout[7];
    private DataManager dataManager = DataManager.getInstance();
    private final int ITEM_NUM_MAXIMUM = 6;

    private RelativeLayout contentWeather, contentTransportation, contentMemo;
    private TextView txtGuide, txtMemoContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER){
                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        String res = (String)o;

                        Log.e("RES", res);
                    }
                };
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

        setWeatherContent();
        setContentTransportation();
        setContentMemo();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        //TextView txtLogin = (TextView) headerView.findViewById(R.id.nav_login);
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


        switch (itemNum) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }
        itemBox[itemNum].setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setWeatherContent();
        setContentTransportation();
        setContentMemo();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_weather) {
            // Handle the camera action
            Intent intent = new Intent(this, ActivityWeather.class);
            startActivity(intent);
        } else if (id == R.id.nav_transportation) {
            Intent intent = new Intent(this, ActivityTransportation.class);
            startActivity(intent);
        } else if (id == R.id.nav_memo) {
            Intent intent = new Intent(this, ActivityMemo.class);
            startActivity(intent);
        } else if (id == R.id.nav_device_settings) {
            Intent intent = new Intent(this, ActivityDeviceSettings.class);
            startActivity(intent);
        } else if (id == R.id.nav_information) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
