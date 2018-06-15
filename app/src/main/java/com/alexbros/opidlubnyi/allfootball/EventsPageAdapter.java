package com.alexbros.opidlubnyi.allfootball;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class EventsPageAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public EventsPageAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new CollapseEventsFragment();
            case 1: return new EventsFragment();
            case 2: return new CollapseEventsFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
