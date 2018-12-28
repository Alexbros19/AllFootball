package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.models.CampaignOdd;
import com.alexbros.opidlubnyi.allfootball.picasso.OddProviderImageView;

public class EventDetailOddsView extends LinearLayout {
    private TextView titleTextView;
    private EventDetailOddsButtonView oddButtonTeamOne;
    private EventDetailOddsButtonView oddButtonTeamTwo;
    private EventDetailOddsButtonView oddButtonDraw;

    private FrameLayout oddProviderLogoFrameLayout;
    private ImageView providerLogoImageView;

    public int currentType = -1;

    public EventDetailOddsView(Context context) {
        super(context);
        init(context);
    }

    public EventDetailOddsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EventDetailOddsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.event_detail_odds_layout, this);

        titleTextView = findViewById(R.id.eventDetailOddsTitle);
        oddButtonTeamOne = findViewById(R.id.oddButtonOneTextView);
        oddButtonDraw = findViewById(R.id.oddButtonDrawTextView);
        oddButtonTeamTwo = findViewById(R.id.oddButtonTwoTextView);
        oddProviderLogoFrameLayout = findViewById(R.id.oddProviderLogoFrameLayout);
        providerLogoImageView = findViewById(R.id.oddProviderLogoImageView);

//        if (!isInEditMode())
//            isLightTheme = PreferenceHelper.isAppThemeWhite(context);
//
//        if (isLightTheme)
//            findViewById(R.id.eventDetailOddsSeparator).setBackgroundColor(Colors.separatorWhiteTheme);
    }

    public void setData(boolean isUpcoming, long utcStartTime, CampaignOdd campaignOdd, boolean canBeHidden) {
        final boolean show = !(isUpcoming && (System.currentTimeMillis() > utcStartTime));

        if (campaignOdd != null && show) {
            final boolean typeChanged = setType(campaignOdd.oddType);

            final boolean isLive = !typeChanged && campaignOdd.isLive;

            setVisibility(VISIBLE);
            oddButtonTeamOne.setData(campaignOdd.onClickUrl1, campaignOdd.odd1String, campaignOdd.odd1, isLive);
            oddButtonDraw.setData(campaignOdd.onClickUrlX, campaignOdd.oddXString, campaignOdd.oddX, isLive);
            oddButtonTeamTwo.setData(campaignOdd.onClickUrl2, campaignOdd.odd2String, campaignOdd.odd2, isLive);

            OddProviderImageView.setOddProviderImage(getContext(), campaignOdd.publisherId, providerLogoImageView, new OddProviderImageView.LoadListener() {
                @Override
                public void onSuccess() {
                    providerLogoImageView.setVisibility(VISIBLE);
                    Bitmap bitmap = ((BitmapDrawable)providerLogoImageView.getDrawable()).getBitmap();
                    int cornerPixelColor = bitmap.getPixel(0,0);
                    oddProviderLogoFrameLayout.setBackgroundColor(cornerPixelColor);
                }

                @Override
                public void onError() {
                    providerLogoImageView.setVisibility(GONE);
                    oddProviderLogoFrameLayout.setBackgroundColor(Color.TRANSPARENT);
                }
            });
        } else {
            if (canBeHidden)
                setVisibility(GONE);
        }
    }

    public boolean isVisible() {
        return getVisibility() == VISIBLE;
    }

    private boolean setType(int type) {
        if (type == CampaignOdd.TYPE_PRE_MATCH) {
            titleTextView.setText("");
            titleTextView.setVisibility(GONE);
        } else if (type == CampaignOdd.TYPE_1_X_2) {
            titleTextView.setText(R.string.string_which_team_will_win_game);
            titleTextView.setVisibility(VISIBLE);
        } else if (type == CampaignOdd.TYPE_NEXT_TO_SCORE) {
            titleTextView.setText(R.string.string_who_will_score_next_goal);
            titleTextView.setVisibility(VISIBLE);
        }

        final boolean typeChanged = (currentType != -1) && (currentType != type);
        currentType = type;

        return typeChanged;
    }
}
