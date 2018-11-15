package com.alexbros.opidlubnyi.allfootball;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class EventContextActivity extends AppCompatActivity {
    public static void showActivity(Activity activity, String firstTeamId, String secondTeamId) {
        Intent intent = new Intent(activity, EventContextActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_FIRST_TEAM_ID, firstTeamId);
        intent.putExtra(Constants.INTENT_EXTRA_SECOND_TEAM_ID, secondTeamId);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_holder);

        Intent intent = getIntent();
        String firstTeamId = intent.getStringExtra(Constants.INTENT_EXTRA_FIRST_TEAM_ID);
        String secondTeamId = intent.getStringExtra(Constants.INTENT_EXTRA_SECOND_TEAM_ID);

        EventContextFragment.showFragment(getSupportFragmentManager(), R.id.fragmentFrameLayout, firstTeamId, secondTeamId);
    }
}
