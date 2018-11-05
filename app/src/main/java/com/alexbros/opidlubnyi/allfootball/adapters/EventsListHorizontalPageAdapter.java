package com.alexbros.opidlubnyi.allfootball.adapters;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.alexbros.opidlubnyi.allfootball.helpers.DateHelper;

import java.util.ArrayList;

public class EventsListHorizontalPageAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<View> views;
    private boolean[] addedCache;

    public EventsListHorizontalPageAdapter(ArrayList<View> views, Activity activity) {
        super();
        this.activity = activity;
        this.views = views;
        this.addedCache = new boolean[views.size()];
    }

    @Override
    public void destroyItem(View viewPager, int position, Object view) {
        //FIXME: this method is ugly set of patches
        //FIXME: problems here happen on Android 7.0
        //FIXME: all this class needs investigation and refactoring...

        if (position >= views.size()) {
//            Crashlytics.log("TVScheduleHorizontalPageAdapter: position " + position + ", addedCache.length " + addedCache.length + ", views.size " + views.size());
            return;
        }

        addedCache[position] = false;

        try {
            ((ViewPager) viewPager).removeView((View) view);
        } catch (IndexOutOfBoundsException e) {
//            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public String getPageTitle(int position) {
        return DateHelper.getEventsListShortDayString(position, activity);
    }

    @Override
    public Object instantiateItem(View viewPager, int position) {
        View v = views.get(position);

        if (!addedCache[position]) {
            // FIXME:  this is ugly quickfix for problem:
            // FIXME:  Fatal Exception: java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
            try {
                ((ViewPager) viewPager).removeView(v);
            } catch (Exception e) {
                e.printStackTrace();
            }

            ((ViewPager) viewPager).addView(v);
            addedCache[position] = true;
        }

        return v;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }
}
