package com.alexbros.opidlubnyi.allfootball;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.alexbros.opidlubnyi.allfootball.adapters.EventsPageAdapter;

public class EventsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    TabItem firstTabItem;
    TabItem secondTabItem;
    TabItem thirdTabItem;
    ViewPager viewPager;
    EventsPageAdapter eventsPageAdapter;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        toolbar = findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.eventsActivityTabLayout);
        firstTabItem = findViewById(R.id.firstTabItem);
        secondTabItem = findViewById(R.id.secondTabItem);
        thirdTabItem = findViewById(R.id.thirdTabItem);

        viewPager = findViewById(R.id.eventsActivityViewPager);
        eventsPageAdapter = new EventsPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(eventsPageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(ContextCompat.getColor(EventsActivity.this, R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
