package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.Colors;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.models.EventsListItem;
import com.alexbros.opidlubnyi.allfootball.picasso.TeamLogoImageView;

public class EventsListEventItemView extends LinearLayout {
    private TextView homeTeamTextView;
    private TextView awayTeamTextView;
    private TextView scoreTextView;
    private ImageView homeTeamImageView;
    private ImageView awayTeamImageView;
    private EventStatusView statusTextView;
    private Typeface scoreTextViewDefaultTypeface;
    private EventLayout eventLayout;

    public EventsListEventItemView(Context context) {
        super(context);
        init(context);
    }

    public EventsListEventItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EventsListEventItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.event_list_item_impl, this);
        eventLayout = findViewById(R.id.eventLayout);
        homeTeamTextView = eventLayout.findViewById(R.id.homeTeamTextView);
        awayTeamTextView = eventLayout.findViewById(R.id.awayTeamTextView);
        scoreTextView = eventLayout.findViewById(R.id.scoreTextView);
        homeTeamImageView = eventLayout.findViewById(R.id.homeTeamImageView);
        awayTeamImageView = eventLayout.findViewById(R.id.awayTeamImageView);
        statusTextView = eventLayout.findViewById(R.id.statusTextView);

        if (scoreTextView != null)
            scoreTextViewDefaultTypeface = scoreTextView.getTypeface();

        statusTextView.setIsScores();
    }

    public void setData(EventsListItem.EventsListEvent event) {
        homeTeamTextView.setText(event.event.getFirstTeamName());
        awayTeamTextView.setText(event.event.getSecondTeamName());
        TeamLogoImageView.setTeamImage(getContext(), homeTeamImageView, event.event.getFirstTeamId(), true);
        TeamLogoImageView.setTeamImage(getContext(), awayTeamImageView, event.event.getSecondTeamId(), true);

        statusTextView.setStatusText(event.event.status);

        if (event.event.running || event.event.finished) {
            scoreTextView.setTypeface(scoreTextViewDefaultTypeface, Typeface.BOLD);
            final SpannableStringBuilder goals = new SpannableStringBuilder();
            int startIndex = goals.length();
            goals.append(event.event.getFirstTeamGoalsString().trim());
            goals.setSpan(new ForegroundColorSpan(Colors.textColorThird), startIndex, goals.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            startIndex = goals.length();
            goals.append(" : ");
            goals.setSpan(new ForegroundColorSpan(Colors.textColorThird), startIndex, goals.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            startIndex = goals.length();
            goals.append(event.event.getSecondTeamGoalsString().trim());
            goals.setSpan(new ForegroundColorSpan(Colors.textColorThird), startIndex, goals.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            scoreTextView.setText(goals);
        } else {
            scoreTextView.setTypeface(scoreTextViewDefaultTypeface, Typeface.NORMAL);
            scoreTextView.setText("- : -");
        }

        if (event.event.running) {
            eventLayout.setBackgroundColors(Colors.eventListStatusBackgroundColorRunning, Color.WHITE, statusTextView.getLayoutParams().width);
            statusTextView.setTextColor(Colors.textColorThird);
        } else if (event.event.halftime || event.event.firstHalftime || event.event.secondHalftime) {
            eventLayout.setBackgroundColors(Colors.eventListStatusBackgroundColorHalftime, Color.WHITE, statusTextView.getLayoutParams().width);
            statusTextView.setTextColor(Colors.textColorThird);
        } else {
            eventLayout.setBackgroundColors(Color.WHITE, Color.WHITE, statusTextView.getLayoutParams().width);
        }
    }
}