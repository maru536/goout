package com.iotaddon.goout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    //private RelativeLayout itemBox[] = new RelativeLayout[7];
    private LinearLayout drawerMenu[] = new LinearLayout[4];
    //private TextView itemTxt[][] = new TextView[7][7];
    private DataManager dataManager = DataManager.getInstance();
    private WeatherDataUpdateListener weatherListener;

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

        recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycleview);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter = new MainActivity.ItemAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        weatherListener = new WeatherDataUpdateListener() {
            @Override
            public void doUpdate() {
                /*setWeatherContent();
                setContentTransportation();
                setContentMemo();*/
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

        txtGuide = (TextView) findViewById(R.id.activity_main_txt);

        AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_WEATHER);
        asyncTaskHttpCommunicator.setListener(weatherListener);
        asyncTaskHttpCommunicator.execute();

        FilterSelectedInfo.setSelectedInfo(arrayList);
        if(arrayList.size()>0)
            txtGuide.setVisibility(View.GONE);
        else
            txtGuide.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

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
        FilterSelectedInfo.setSelectedInfo(arrayList);
        if(arrayList.size()>0)
            txtGuide.setVisibility(View.GONE);
        else
            txtGuide.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
        super.onResume();
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
        Intent intent;

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
            ItemContents item = items.get(position);
            holder.more.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.mipmap.ic_launcher);
            if (item.getContentsType() == dataManager.TYPE_WEATHER_HUMIDITY) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                intent = new Intent(context, ActivityMoreConfiguration.class);
                intent.addFlags(dataManager.TYPE_WEATHER_HUMIDITY);
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_WIND) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                intent = new Intent(context, ActivityMoreConfiguration.class);
                intent.addFlags(dataManager.TYPE_WEATHER_WIND);
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_DUST) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                intent = new Intent(context, ActivityMoreConfiguration.class);
                intent.addFlags(dataManager.TYPE_WEATHER_DUST);
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_TEMP) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                intent = new Intent(context, ActivityMoreConfiguration.class);
                intent.addFlags(dataManager.TYPE_WEATHER_TEMP);
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_SKY) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                //holder.container.setBackground(getDrawable(R.drawable.border_red));
                SelectWeatherIcon.setWeatherSkyIcon(holder.icon, dataManager.getDataWeather().getDataWeatherSky().getCode(),context);
                intent = new Intent(context, ActivityMoreConfiguration.class);
                intent.addFlags(dataManager.TYPE_WEATHER_SKY);
            } else if (item.getContentsType() == dataManager.TYPE_WEATHER_PRECIPITATION) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                intent = new Intent(context, ActivityMoreConfiguration.class);
                intent.addFlags(dataManager.TYPE_WEATHER_PRECIPITATION);
            } else if (item.getContentsType() == dataManager.TYPE_TRANSPORTATION_BUS) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                intent = new Intent(context, ActivityMoreConfiguration.class);
                intent.addFlags(dataManager.TYPE_TRANSPORTATION_BUS);
            } else if (item.getContentsType() == dataManager.TYPE_TRANSPORTATION_SUBWAY) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                intent = new Intent(context, ActivityMoreConfiguration.class);
                intent.addFlags(dataManager.TYPE_TRANSPORTATION_SUBWAY);
            } else if (item.getContentsType() == dataManager.TYPE_MEMO) {
                holder.txtTitle.setText(item.getTitle());
                holder.txtContents.setText(item.getContents());
                holder.more.setVisibility(View.GONE);
            }

            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

}
