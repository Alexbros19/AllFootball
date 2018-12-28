package com.alexbros.opidlubnyi.allfootball.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alexbros.opidlubnyi.allfootball.Constants;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.fragments.EventContextFragment;

public class EventContextActivity extends AppCompatActivity {
    public static void showActivity(Activity activity, String firstTeamId, String secondTeamId, boolean isUpcoming, long utcStartTime, String eventId) {
        Intent intent = new Intent(activity, EventContextActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_FIRST_TEAM_ID, firstTeamId);
        intent.putExtra(Constants.INTENT_EXTRA_SECOND_TEAM_ID, secondTeamId);
        intent.putExtra(Constants.INTENT_EXTRA_IS_UPCOMING, isUpcoming);
        intent.putExtra(Constants.INTENT_EXTRA_UTC_START_TIME, utcStartTime);
        intent.putExtra(Constants.INTENT_EXTRA_EVENT_ID, eventId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_holder);

        Intent intent = getIntent();
        String firstTeamId = intent.getStringExtra(Constants.INTENT_EXTRA_FIRST_TEAM_ID);
        String secondTeamId = intent.getStringExtra(Constants.INTENT_EXTRA_SECOND_TEAM_ID);
        boolean isUpcoming = intent.getBooleanExtra(Constants.INTENT_EXTRA_IS_UPCOMING, true);
        long utcStartTime = intent.getLongExtra(Constants.INTENT_EXTRA_UTC_START_TIME, 0);
        String eventId = intent.getStringExtra(Constants.INTENT_EXTRA_EVENT_ID);

        EventContextFragment.showFragment(getSupportFragmentManager(), R.id.fragmentFrameLayout, firstTeamId, secondTeamId, isUpcoming, utcStartTime, eventId);
    }
}
