package com.alexbros.opidlubnyi.allfootball;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.picasso.TeamLogoImageView;
import com.alexbros.opidlubnyi.allfootball.views.EventLayout;
import com.alexbros.opidlubnyi.allfootball.views.EventStatusView;


public class EventView extends LinearLayout {
    private TextView homeTeamTextView;
    private TextView awayTeamTextView;
    private ImageView homeTeamImageView;
    private ImageView awayTeamImageView;
    private EventStatusView statusTextView;

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
        inflate(context, R.layout.fragment_event_list_item_impl, this);
        EventLayout eventLayout = findViewById(R.id.eventLayout);
        homeTeamTextView = eventLayout.findViewById(R.id.homeTeamTextView);
        awayTeamTextView = eventLayout.findViewById(R.id.awayTeamTextView);
        homeTeamImageView = eventLayout.findViewById(R.id.homeTeamImageView);
        awayTeamImageView = eventLayout.findViewById(R.id.awayTeamImageView);
        statusTextView = eventLayout.findViewById(R.id.statusTextView);

        statusTextView.setIsScores();
    }

    public void setElement(ListElement listElement) {
        homeTeamTextView.setText(listElement.getFirstTeamName());
        awayTeamTextView.setText(listElement.getSecondTeamName());
        TeamLogoImageView.setTeamImage(getContext(), homeTeamImageView, listElement.getTeamOneId(), true);
        TeamLogoImageView.setTeamImage(getContext(), awayTeamImageView, listElement.getTeamTwoId(), true);
        statusTextView.setStatusText(DateHelper.getFormattedTime(getContext(), listElement.getUtcStartTime()));
    }
}