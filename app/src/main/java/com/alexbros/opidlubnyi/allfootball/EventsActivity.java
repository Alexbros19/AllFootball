package com.alexbros.opidlubnyi.allfootball;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class EventsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    TabItem firstTabItem;
    TabItem secondTabItem;
    TabItem thirdTabItem;
    ViewPager viewPager;
    EventsPageAdapter eventsPageAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

//        toolbar = findViewById(R.id.eventsActivityToolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);

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
}
