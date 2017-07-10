package com.iotaddon.goout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
    private HttpResponseDataUpdateListener weatherListener, weatherDustListener, transportaitionBusListener, userGetConfigListener;

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

        chkGpsService();

        FirebaseMessaging.getInstance().subscribeToTopic("out_alarm");
        dataManager.setFcmToken(FirebaseInstanceId.getInstance().getToken());


        recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycleview);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter = new MainActivity.ItemAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);


        userGetConfigListener = new HttpResponseDataUpdateListener() {
            @Override
            public void doUpdate(String res) {
                DataManager dm = DataManager.getInstance();
                DataWeather dw = dm.getDataWeather();

                Log.e("config res##########", res);

                try {
                    JSONObject json = new JSONObject(res);

                    dm.setFcmToken(json.getString("deviceId"));
                    dw.getDataWeatherTemperature().setWarningValue(Integer.parseInt(json.getString("tempLimit")));
                    dw.getDataWeatherHumidity().setWarningValue(Integer.parseInt(json.getString("humidityLimit")));
                    dw.getDataWeatherSky().setWarningValue(json.getInt("skyLimit"));
                    dw.getDataWeatherDust().setWarningValue(Integer.parseInt(json.getString("dustLimit")));
                    dm.getDataBusInfo().setWarningValue(Integer.parseInt(json.getString("transportLimit")));
                    dm.setSavedMemo(json.getString("memo"));
                    dm.setSelectedWeather(dm.TYPE_WEATHER_SKY, json.getBoolean("isEnableSkyInfo"));
                    dm.setSelectedWeather(dm.TYPE_WEATHER_HUMIDITY, json.getBoolean("isEnableHumidityInfo"));
                    dm.setSelectedWeather(dm.TYPE_WEATHER_TEMP, json.getBoolean("isEnableTempInfo"));
                    dm.setSelectedWeather(dm.TYPE_WEATHER_DUST, json.getBoolean("isEnableDustInfo"));
                    dm.setSelectedTransportation(dm.TYPE_TRANSPORTATION_BUS_SELECTED,json.getBoolean("isEnableBusInfo"));

                    dm.setSelectedLed(DataManager.TYPE_LED_1, json.getInt("firstLEDInfo"));
                    dm.setSelectedLed(DataManager.TYPE_LED_2, json.getInt("secondLEDInfo"));
                    dm.setSelectedLed(DataManager.TYPE_LED_3, json.getInt("thirdLEDInfo"));
                    dm.getUserAddress().setLatitude(Double.parseDouble(json.getString("latitude")));
                    dm.getUserAddress().setLongitude(Double.parseDouble(json.getString("longitude")));
                    dm.getUserAddress().setName(json.getString("name"));
                    dm.setOutAlarm(json.getBoolean("alarm"));
                    dm.getDataBusInfo().setBusRouteId(json.getInt("busRouteId"));
                    dm.getDataBusInfo().setStId(json.getString("stId"));
                    dm.getDataBusInfo().setRtNm(json.getString("rtNm"));
                    dw.getDataWeatherTemperature().setHigher(json.getBoolean("isHigher_Temp"));
                    dw.getDataWeatherHumidity().setHigher(json.getBoolean("isHigher_Humidity"));
                    dm.getDataBusInfo().setHigher(json.getBoolean("isHigher_transportLimit"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                requestWeatherInfo();
                requestDustInfo();
                requestBusInfo();

            }
        };

        requestGetConfig();

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

        transportaitionBusListener = new HttpResponseDataUpdateListener() {
            @Override
            public void doUpdate(String res) {
                try {
                    Log.e("bus info res", res);
                    JSONObject json = new JSONObject(res);
                    JSONObject jsonServiceResult = json.getJSONObject("ServiceResult");
                    JSONObject jsonMsgBody = jsonServiceResult.getJSONObject("msgBody");
                    JSONArray jsonItemList = jsonMsgBody.getJSONArray("itemList");

                    for (int i = 0; i < jsonItemList.length(); i++) {
                        JSONObject jsonObject = jsonItemList.getJSONObject(i);
                        if (dataManager.getDataBusInfo().getBusRouteId() == jsonObject.getInt("busRouteId")) {
                            DataBusInfo dataBusInfo = new DataBusInfo(jsonObject.getString("stId"), jsonObject.getInt("busRouteId"), jsonObject.getString("rtNm"), jsonObject.getString("arrmsg1"), jsonObject.getString("arrmsg2"), jsonObject.getString("stationNm1"), jsonObject.getString("stationNm2"));
                            dataManager.getDataBusInfo().setStId(jsonObject.getString("stId"));
                            dataManager.getDataBusInfo().setRtNm(jsonObject.getString("rtNm"));
                            dataManager.getDataBusInfo().setBusRouteId(jsonObject.getInt("busRouteId"));
                            dataManager.getDataBusInfo().setArrmsg1(jsonObject.getString("arrmsg1"));
                            dataManager.getDataBusInfo().setArrmsg2(jsonObject.getString("arrmsg2"));
                            dataManager.getDataBusInfo().setStationNm1(jsonObject.getString("stationNm1"));
                            dataManager.getDataBusInfo().setStationNm2(jsonObject.getString("stationNm2"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestWeatherInfo();
                requestDustInfo();
                requestBusInfo();
            }
        });

        txtGuide = (TextView) findViewById(R.id.activity_main_txt);

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
        requestWeatherInfo();
        requestDustInfo();
        requestBusInfo();
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
                holder.icon.setImageResource(R.drawable.humidity);
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_WIND) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                holder.icon.setImageResource(R.drawable.ic_wind);
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_DUST) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                if (dataManager.getDataWeather().getDataWeatherDust().getValue() > 0)
                    SelectWeatherIcon.setWeatherDustIcon(holder.icon, dataManager.getDataWeather().getDataWeatherDust().getGrade(), context);
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
                holder.icon.setImageResource(R.drawable.bus_ic);
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
            } else if (item.getContentsType() == dataManager.TYPE_TRANSPORTATION_SUBWAY) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
            } else if (item.getContentsType() == dataManager.TYPE_MEMO) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                holder.icon.setImageResource(R.drawable.memo_ic);
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

    private void changeContentsState() {
        if (arrayList.size() > 0)
            txtGuide.setVisibility(View.GONE);
        else
            txtGuide.setVisibility(View.VISIBLE);
    }

    private boolean chkGpsService() {

        String gps = android.provider.Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        Log.d(gps, "aaaa");

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("본 어플리케이션의 서비스 이용을 위해 위치서비스 기능이 필요합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            return true;
        }
    }

    void requestWeatherInfo() {
        AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER, "");
        asyncTaskHttpCommunicator.setListener(weatherListener);
        asyncTaskHttpCommunicator.execute();
    }

    void requestGetConfig() {
        AsyncTaskHttpCommunicator asyncTaskHttpCommunicatorConfig = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_USER_GET_CONFIG, dataManager.getFcmToken());
        asyncTaskHttpCommunicatorConfig.setListener(userGetConfigListener);
        asyncTaskHttpCommunicatorConfig.execute();
    }

    void requestDustInfo() {
        AsyncTaskHttpCommunicator asyncTaskHttpCommunicatorDust = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER_DUST, "");
        asyncTaskHttpCommunicatorDust.setListener(weatherDustListener);
        asyncTaskHttpCommunicatorDust.execute();
    }

    void requestBusInfo() {
        if (!dataManager.getDataBusInfo().getStId().equals("") && dataManager.getDataBusInfo().getBusRouteId() != 0) {
            AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_TRANSPORTATION_BUS_LIST, dataManager.getDataBusInfo().getStId());
            asyncTaskHttpCommunicator.setListener(transportaitionBusListener);
            asyncTaskHttpCommunicator.execute();
        }
    }
}
