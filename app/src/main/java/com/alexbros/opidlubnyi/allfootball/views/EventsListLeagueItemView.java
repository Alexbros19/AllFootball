package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.Colors;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.models.League;

public class EventsListLeagueItemView extends LinearLayout {
    private Context context;
    //    private LeagueClickListener leagueClickListener;
    private ImageView leagueFlagImageView;
    private TextView leagueNameTextView;
//    private OnClickListener leagueOnViewClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            League league = (League) view.getTag();
//
//            if (league != null)
//                leagueClickListener.onLeagueClick(league);
//        }
//    };

    public EventsListLeagueItemView(Context context) {
        super(context);
        init(context);
    }

    public EventsListLeagueItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EventsListLeagueItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

//    public void setEventListener(LeagueClickListener leagueClickListener) {
//        this.leagueClickListener = leagueClickListener;
//    }

    private void init(Context context) {
        inflate(context, R.layout.event_list_league_item_view_layout, this);

        this.context = context;

        View userPredictionLeagueContent = findViewById(R.id.userPredictionLeagueContent);
        leagueFlagImageView = findViewById(R.id.userPredictionLeagueFlagImageView);
        leagueNameTextView = findViewById(R.id.userPredictionLeagueNameTextView);

//        if (!isInEditMode())
//            isLightTheme = PreferenceHelper.isAppThemeWhite(context);
//
//        if (isLightTheme) {
            setBackgroundColor(Colors.eventListBarColorWhiteTheme);
            userPredictionLeagueContent.setBackgroundColor(Colors.eventListTitleColorWhiteTheme);
            final int textColor = ContextCompat.getColor(context, R.color.color_activity_background_black);

            leagueNameTextView.setTextColor(textColor);
//        } else {
//            setBackgroundColor(Colors.eventListBarColorBlackTheme);
//            userPredictionLeagueContent.setBackgroundColor(Colors.eventListTitleColorBlackTheme);
//        }

//        setOnClickListener(leagueOnViewClickListener);
    }

    public void setData(League league) {
        leagueNameTextView.setText(league.getLocalizedTitle());
        FlagImageView.setCountry(context, league.country, leagueFlagImageView);

        setTag(league);

        setEnabled(!league.id.isEmpty());
    }
}
