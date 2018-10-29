package com.alexbros.opidlubnyi.allfootball;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;


public class EventView extends LinearLayout {
    private TextView startTimeTextView;
    private TextView firstTeamNameTextView;
    private TextView secondTeamNameTextView;

    public EventView(Context context) {
        super(context);
        init(context);
    }

    public EventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.fragment_event_list, this);
        startTimeTextView = findViewById(R.id.timeText);
        firstTeamNameTextView = findViewById(R.id.firstTeamName);
        secondTeamNameTextView = findViewById(R.id.secondTeamName);
    }

    public void setElement(ListElement listElement) {
        startTimeTextView.setText(DateHelper.getFormattedTime(getContext(), listElement.getUtcStartTime()));
        firstTeamNameTextView.setText(listElement.getFirstTeamName());
        secondTeamNameTextView.setText(listElement.getSecondTeamName());
    }
}