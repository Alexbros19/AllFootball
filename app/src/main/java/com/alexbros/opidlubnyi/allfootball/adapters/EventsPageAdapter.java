package com.alexbros.opidlubnyi.allfootball.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.alexbros.opidlubnyi.allfootball.CollapseEventsFragment;
import com.alexbros.opidlubnyi.allfootball.EventsFragment;

public class EventsPageAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;

    public EventsPageAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new EventsFragment();
            case 1: return new EventsFragment();
            case 2: return new EventsFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
