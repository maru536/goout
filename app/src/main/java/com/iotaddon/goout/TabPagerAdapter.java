package com.iotaddon.goout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by junhan on 2017-06-01.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 1:
                FragmentSearchAroundBusStation fragmentSearchAroundBusStation = FragmentSearchAroundBusStation.newInstance();
                return fragmentSearchAroundBusStation;
            case 0:
                FragmentSearchBusStation fragmentSearchBusStation = FragmentSearchBusStation.newInstance();
                return fragmentSearchBusStation;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
