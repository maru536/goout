package com.iotaddon.goout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;


public class ActivityTransportationBus extends AppCompatActivity{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_bus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33b5e5")));

        editSearch = (EditText)findViewById(R.id.activity_transportation_bus_edit_search);
        tabLayout = (TabLayout)findViewById(R.id.activity_transportation_bus_tab);
        tabLayout.addTab(tabLayout.newTab().setText("버스"));
        tabLayout.addTab(tabLayout.newTab().setText("정류장"));

        viewPager = (ViewPager)findViewById(R.id.activity_transportation_bus_pager);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.setCurrentItem(0);
        editSearch.setHint("버스번호 검색");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch(tab.getPosition()){
                    case 0:
                        editSearch.setHint("버스번호 검색");
                        break;
                    case 1:
                        editSearch.setHint("정류장 번호 검색");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
