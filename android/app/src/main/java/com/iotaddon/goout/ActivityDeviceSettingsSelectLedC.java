package com.iotaddon.goout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityDeviceSettingsSelectLedC extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ItemContents> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_settings_select_led_c);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));
        getSupportActionBar().setElevation(0);

        recyclerView = (RecyclerView) findViewById(R.id.activity_device_settings_select_led_3_recycleview);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        adapter = new ActivityDeviceSettingsSelectLedC.ItemAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        FilterSelectedInfo.setSelectedInfo(arrayList, FilterSelectedInfo.FILTER_TYPE_LED);

        final GestureDetector gestureDetector = new GestureDetector(ActivityDeviceSettingsSelectLedC.this, new GestureDetector.SimpleOnGestureListener() {
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
                    setResult(arrayList.get(position).getContentsType());
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
        });
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

    class ItemAdapter extends RecyclerView.Adapter<ActivityDeviceSettingsSelectLedC.ItemAdapter.ViewHolder> {


        private ArrayList<ItemContents> items;
        private Context context;

        public ItemAdapter(ArrayList<ItemContents> items, Context context) {
            this.items = items;
            this.context = context;
        }

        @Override
        public ActivityDeviceSettingsSelectLedC.ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_led, parent, false);
            ActivityDeviceSettingsSelectLedC.ItemAdapter.ViewHolder holder = new ActivityDeviceSettingsSelectLedC.ItemAdapter.ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ActivityDeviceSettingsSelectLedC.ItemAdapter.ViewHolder holder, final int position) {
            ItemContents item = items.get(position);
            holder.txtItem.setText(item.getTitle());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txtItem;
            public ViewHolder(View itemView) {
                super(itemView);
                txtItem = (TextView)itemView.findViewById(R.id.item_select_led_txt);
            }
        }
    }
}
