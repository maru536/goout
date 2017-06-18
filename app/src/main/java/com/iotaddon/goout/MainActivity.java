package com.iotaddon.goout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private LinearLayout drawerMenu[] = new LinearLayout[4];
    private DataManager dataManager = DataManager.getInstance();
    private HttpResponseDataUpdateListener weatherListener, weatherDustListener;

    //private RelativeLayout contentWeather, contentTransportation, contentMemo;
    //private TextView txtGuide, txtMemoContent;
    private TextView txtGuide;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ItemContents> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));

        FirebaseMessaging.getInstance().subscribeToTopic("out_alarm");
        FirebaseInstanceId.getInstance().getToken();

        recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycleview);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter = new MainActivity.ItemAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        weatherListener = new HttpResponseDataUpdateListener() {
            @Override
            public void doUpdate(String res) {
                DataManager dataManager = DataManager.getInstance();
                DataWeather dataWeather = dataManager.getDataWeather();
                try {
                    JSONObject json = new JSONObject(res);
                    if (json.has("weather")) {
                        JSONObject jsonWeather = json.getJSONObject("weather");
                        JSONArray jsonMinutely = jsonWeather.getJSONArray("minutely");
                        Log.e("ㅁㄴㅇㅁㄴㅇ", jsonMinutely.toString() + "   /  " + jsonMinutely.length());
                        JSONObject jsonStation = jsonMinutely.getJSONObject(0).getJSONObject("station");
                        dataWeather.getDataWeatherStation().setLongitude(jsonStation.getDouble("longitude"));
                        dataWeather.getDataWeatherStation().setLatitude(jsonStation.getDouble("latitude"));
                        dataWeather.getDataWeatherStation().setName(jsonStation.getString("name"));
                        dataWeather.getDataWeatherStation().setId(jsonStation.getInt("id"));
                        dataWeather.getDataWeatherStation().setType(jsonStation.getString("type"));

                        JSONObject jsonWind = jsonMinutely.getJSONObject(0).getJSONObject("wind");
                        dataWeather.getDataWeatherWind().setWdir(jsonWind.getDouble("wdir"));
                        dataWeather.getDataWeatherWind().setWdir(jsonWind.getDouble("wspd"));

                        JSONObject jsonPrecipitation = jsonMinutely.getJSONObject(0).getJSONObject("precipitation");
                        dataWeather.getDataWeatherPrecipitation().setSinceOntime(jsonPrecipitation.getDouble("sinceOntime"));
                        dataWeather.getDataWeatherPrecipitation().setType(jsonPrecipitation.getInt("type"));

                        JSONObject jsonSky = jsonMinutely.getJSONObject(0).getJSONObject("sky");
                        dataWeather.getDataWeatherSky().setName(jsonSky.getString("name"));
                        dataWeather.getDataWeatherSky().setCode(jsonSky.getString("code"));

                        JSONObject jsonRain = jsonMinutely.getJSONObject(0).getJSONObject("rain");
                        dataWeather.getDataWeatherRain().setLast6hour(jsonRain.getDouble("last6hour"));
                        dataWeather.getDataWeatherRain().setLast12hour(jsonRain.getDouble("last12hour"));
                        dataWeather.getDataWeatherRain().setLast24hour(jsonRain.getDouble("last24hour"));
                        dataWeather.getDataWeatherRain().setSinceMidnight(jsonRain.getDouble("sinceMidnight"));
                        dataWeather.getDataWeatherRain().setLast10min(jsonRain.getDouble("last10min"));
                        dataWeather.getDataWeatherRain().setLast15min(jsonRain.getDouble("last15min"));
                        dataWeather.getDataWeatherRain().setLast30min(jsonRain.getDouble("last30min"));
                        dataWeather.getDataWeatherRain().setLast1hour(jsonRain.getDouble("last1hour"));
                        dataWeather.getDataWeatherRain().setSinceOntime(jsonRain.getDouble("sinceOntime"));

                        JSONObject jsonTemperature = jsonMinutely.getJSONObject(0).getJSONObject("temperature");
                        dataWeather.getDataWeatherTemperature().setTc(jsonTemperature.getDouble("tc"));
                        dataWeather.getDataWeatherTemperature().setTmax(jsonTemperature.getDouble("tmax"));
                        dataWeather.getDataWeatherTemperature().setTmin(jsonTemperature.getDouble("tmin"));

                        dataWeather.getDataWeatherHumidity().setHumidity(jsonMinutely.getJSONObject(0).getDouble("humidity"));

                        JSONObject jsonPressure = jsonMinutely.getJSONObject(0).getJSONObject("pressure");
                        dataWeather.getDataWeatherPressure().setSealevel(jsonPressure.getDouble("seaLevel"));
                        dataWeather.getDataWeatherPressure().setSurface(jsonPressure.getDouble("surface"));

                        dataWeather.getDataWeatherLightning().setLightning(jsonMinutely.getJSONObject(0).getInt("lightning"));

                        dataWeather.getDataWeatherTimeObservation().setDate(jsonMinutely.getJSONObject(0).getString("timeObservation"));
                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FilterSelectedInfo.setSelectedInfo(arrayList, FilterSelectedInfo.FILTER_TYPE_MAIN);
                changeContentsState();
                adapter.notifyDataSetChanged();
            }

        };

        weatherDustListener = new HttpResponseDataUpdateListener() {
            @Override
            public void doUpdate(String res) {
                DataManager dataManager = DataManager.getInstance();
                DataWeather dataWeather = dataManager.getDataWeather();
                Log.e("dust res", res);
                try {
                    JSONObject json = new JSONObject(res);
                    if (json.has("weather")) {
                        JSONObject jsonWeather = json.getJSONObject("weather");
                        JSONArray jsonDust = jsonWeather.getJSONArray("dust");
                        JSONObject jsonDustObject = jsonDust.getJSONObject(0);
                        JSONObject jsonPm10 = jsonDustObject.getJSONObject("pm10");
                        dataWeather.getDataWeatherDust().setGrade(jsonPm10.getString("grade"));
                        dataWeather.getDataWeatherDust().setValue(jsonPm10.getDouble("value"));
                    } else {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                FilterSelectedInfo.setSelectedInfo(arrayList, FilterSelectedInfo.FILTER_TYPE_MAIN);
                changeContentsState();
                adapter.notifyDataSetChanged();
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER, "");
                asyncTaskHttpCommunicator.setListener(weatherListener);
                asyncTaskHttpCommunicator.execute();
                AsyncTaskHttpCommunicator asyncTaskHttpCommunicatorDust = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER_DUST, "");
                asyncTaskHttpCommunicatorDust.setListener(weatherDustListener);
                asyncTaskHttpCommunicatorDust.execute();
            }
        });

        txtGuide = (TextView) findViewById(R.id.activity_main_txt);

        AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER, "");
        asyncTaskHttpCommunicator.setListener(weatherListener);
        asyncTaskHttpCommunicator.execute();
        AsyncTaskHttpCommunicator asyncTaskHttpCommunicatorDust = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER_DUST, "");
        asyncTaskHttpCommunicatorDust.setListener(weatherDustListener);
        asyncTaskHttpCommunicatorDust.execute();

        changeContentsState();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawerMenu[0] = (LinearLayout) findViewById(R.id.drawer_menu_weather);
        drawerMenu[1] = (LinearLayout) findViewById(R.id.drawer_menu_transportation);
        drawerMenu[2] = (LinearLayout) findViewById(R.id.drawer_menu_memo);
        drawerMenu[3] = (LinearLayout) findViewById(R.id.drawer_menu_settings);

        drawerMenu[0].setOnClickListener(this);
        drawerMenu[1].setOnClickListener(this);
        drawerMenu[2].setOnClickListener(this);
        drawerMenu[3].setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER, "");
        asyncTaskHttpCommunicator.setListener(weatherListener);
        asyncTaskHttpCommunicator.execute();
        AsyncTaskHttpCommunicator asyncTaskHttpCommunicatorDust = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER_DUST, "");
        asyncTaskHttpCommunicatorDust.setListener(weatherDustListener);
        asyncTaskHttpCommunicatorDust.execute();
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
    public void onClick(View v) {
        DrawerLayout drawer;
        Intent intent;

        switch (v.getId()) {
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

    class ItemAdapter extends RecyclerView.Adapter<MainActivity.ItemAdapter.ViewHolder> {


        private ArrayList<ItemContents> items;
        private Context context;

        public ItemAdapter(ArrayList<ItemContents> items, Context context) {
            this.items = items;
            this.context = context;
        }

        @Override
        public MainActivity.ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contents, parent, false);
            MainActivity.ItemAdapter.ViewHolder holder = new MainActivity.ItemAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MainActivity.ItemAdapter.ViewHolder holder, final int position) {
            final ItemContents item = items.get(position);
            holder.more.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.mipmap.ic_launcher);
            SelectWeatherIcon.setWarningBorder(holder.container, item.getContentsType(), context);
            if (item.getContentsType() == dataManager.TYPE_WEATHER_HUMIDITY) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_WIND) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                holder.icon.setImageResource(R.drawable.ic_wind);
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_DUST) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_TEMP) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                holder.icon.setImageResource(R.drawable.ic_temp);
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_SKY) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                //holder.container.setBackground(getDrawable(R.drawable.border_red));
                if (dataManager.getDataWeather().getDataWeatherSky().getCode().length() > 0)
                    SelectWeatherIcon.setWeatherSkyIcon(holder.icon, dataManager.getDataWeather().getDataWeatherSky().getCode(), context);
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_PRECIPITATION) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                holder.icon.setImageResource(R.drawable.ic_umbrella);
            } else if (item.getContentsType() == dataManager.TYPE_TRANSPORTATION_BUS) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
            } else if (item.getContentsType() == dataManager.TYPE_TRANSPORTATION_SUBWAY) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
            } else if (item.getContentsType() == dataManager.TYPE_MEMO) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                holder.icon.setImageResource(R.drawable.ic_note);
                holder.more.setVisibility(View.GONE);
            }

            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    intent = new Intent(context, ActivityMoreConfiguration.class);
                    intent.putExtra("INFO_TYPE", item.getContentsType());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txtTitle;
            public TextView txtContents;
            public ImageView icon, more;
            public LinearLayout container;

            public ViewHolder(View itemView) {
                super(itemView);
                txtTitle = (TextView) itemView.findViewById(R.id.item_contents_txt_title);
                txtContents = (TextView) itemView.findViewById(R.id.item_contents_txt_contents);
                icon = (ImageView) itemView.findViewById(R.id.item_contents_img_icon);
                more = (ImageView) itemView.findViewById(R.id.item_contents_img_more);
                container = (LinearLayout) itemView.findViewById(R.id.item_contents_linear_container);
            }
        }
    }

    private void changeContentsState(){
        if (arrayList.size() > 0)
            txtGuide.setVisibility(View.GONE);
        else
            txtGuide.setVisibility(View.VISIBLE);
    }

}
