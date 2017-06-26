package com.iotaddon.goout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityTransportationBusInfo extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataBusInfo> arrayList;
    private int STATION_ID = 0;
    private DataManager dataManager = DataManager.getInstance();


    private HttpResponseDataUpdateListener busListListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_bus_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        getSupportActionBar().setElevation(0);

        Intent intent = getIntent();
        STATION_ID = intent.getIntExtra("stationId", 0);
        arrayList = new ArrayList<>();

        busListListener = new HttpResponseDataUpdateListener() {
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
                        DataBusInfo dataBusInfo = new DataBusInfo(jsonObject.getString("stId"), jsonObject.getInt("busRouteId"), jsonObject.getString("rtNm"), jsonObject.getString("arrmsg1"), jsonObject.getString("arrmsg2"), jsonObject.getString("stationNm1"), jsonObject.getString("stationNm2"));
                        arrayList.add(dataBusInfo);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        };

        AsyncTaskHttpCommunicator asyncTaskHttpCommunicator = new AsyncTaskHttpCommunicator(AsyncTaskHttpCommunicator.HTTP_URL_TRANSPORTATION_BUS_LIST, STATION_ID + "");
        asyncTaskHttpCommunicator.setListener(busListListener);
        asyncTaskHttpCommunicator.execute();


        recyclerView = (RecyclerView) findViewById(R.id.activity_transportation_businfo_recycleview);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ActivityTransportationBusInfo.ItemAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        /*final GestureDetector gestureDetector = new GestureDetector(ActivityTransportationBusInfo.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    dataManager.getDataBusInfo().setStId(arrayList.get(position).getStId());
                    dataManager.getDataBusInfo().setBusRouteId(arrayList.get(position).getBusRouteId());
                    dataManager.getDataBusInfo().setRtNm(arrayList.get(position).getRtNm());
                    dataManager.getDataBusInfo().setArrmsg1(arrayList.get(position).getArrmsg1());
                    dataManager.getDataBusInfo().setArrmsg2(arrayList.get(position).getArrmsg2());
                    dataManager.getDataBusInfo().setStationNm1(arrayList.get(position).getStationNm1());
                    dataManager.getDataBusInfo().setStationNm2(arrayList.get(position).getStationNm2());
                    setResult(RESULT_OK);
                    finish();
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });*/


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

    class ItemAdapter extends RecyclerView.Adapter<ActivityTransportationBusInfo.ItemAdapter.ViewHolder> {


        private ArrayList<DataBusInfo> items;
        private Context context;

        public ItemAdapter(ArrayList<DataBusInfo> items, Context context) {
            this.items = items;
            this.context = context;
        }

        @Override
        public ActivityTransportationBusInfo.ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_businfo_contents, parent, false);
            ActivityTransportationBusInfo.ItemAdapter.ViewHolder holder = new ActivityTransportationBusInfo.ItemAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ActivityTransportationBusInfo.ItemAdapter.ViewHolder holder, final int position) {
            final DataBusInfo item = items.get(position);

            holder.txtName.setText(item.getRtNm());

            String info1, info2;

            info1 = "이번 버스 : " + item.getArrmsg1() + "(" + item.getStationNm1() + ")";
            info2 = "다음 버스 : " + item.getArrmsg2() + "(" + item.getStationNm2() + ")";

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dataManager.getDataBusInfo().setStId(item.getStId());
                    dataManager.getDataBusInfo().setBusRouteId(item.getBusRouteId());
                    dataManager.getDataBusInfo().setRtNm(item.getRtNm());
                    dataManager.getDataBusInfo().setArrmsg1(item.getArrmsg1());
                    dataManager.getDataBusInfo().setArrmsg2(item.getArrmsg2());
                    dataManager.getDataBusInfo().setStationNm1(item.getStationNm1());
                    dataManager.getDataBusInfo().setStationNm2(item.getStationNm2());
                    setResult(RESULT_OK);
                    finish();
                }
            });

            holder.txtInfo1.setText(info1);
            holder.txtInfo2.setText(info2);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtName, txtInfo1, txtInfo2;
            LinearLayout linearLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.item_bus_station_contents_linear_container);
                txtName = (TextView) itemView.findViewById(R.id.item_businfo_contents_txt_name);
                txtInfo1 = (TextView) itemView.findViewById(R.id.item_businfo_contents_txt_arrival);
                txtInfo2 = (TextView) itemView.findViewById(R.id.item_businfo_contents_txt_arrival2);
            }
        }
    }
}