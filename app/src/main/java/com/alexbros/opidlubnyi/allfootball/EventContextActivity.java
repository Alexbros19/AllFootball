package com.alexbros.opidlubnyi.allfootball;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class EventContextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_context);

        Toolbar toolbar = findViewById(R.id.eventContentToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView buttonNumber = findViewById(R.id.eventContentInfo);
        ListElement listElement = (ListElement) getIntent().getSerializableExtra(Constants.BUTTONS_CONTEXT_ACTIVITY_PARAM);
        buttonNumber.setText(listElement.getFirstTeamName() + " vs " + listElement.getSecondTeamName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
