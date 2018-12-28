package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.Colors;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.helpers.ResourcesHelper;
import com.alexbros.opidlubnyi.allfootball.listeners.OpenURLClickListener;
import com.alexbros.opidlubnyi.allfootball.models.ModelData;

public class EventDetailOddsButtonView extends LinearLayout {
    private View oddButton;
    private ImageView arrowImageView;
    private TextView valueTextView;

    private int backgroundTransparentResourceId = 0;

    public float oldOddValue = -1;

    public EventDetailOddsButtonView(Context context) {
        super(context);
        init(context, null);
    }

    public EventDetailOddsButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EventDetailOddsButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(context, R.layout.event_detail_odds_button_layout, this);

        oddButton = findViewById(R.id.oddButtonRoot);
        arrowImageView = findViewById(R.id.oddButtonArrowImageView);
        TextView labelTextView = findViewById(R.id.oddButtonLabelTextView);
        valueTextView = findViewById(R.id.oddButtonValueTextView);

        if (!isInEditMode()) {
//            isLightTheme = PreferenceHelper.isAppThemeWhite(context);
            backgroundTransparentResourceId = ResourcesHelper.getStyledDrawableResourceId(context, R.attr.btn_round_edge_background_transparent);

//            if (!isLightTheme)
            labelTextView.setTextColor(Colors.textColorThird);
        }

        String label = null;
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.EventDetailOddsButtonView,
                    0, 0);

            try {
                label = a.getString(R.styleable.EventDetailOddsButtonView_label);
            } finally {
                a.recycle();
            }
        }

        oddButton.setBackgroundResource(backgroundTransparentResourceId);

        if (label != null && !label.isEmpty())
            labelTextView.setText(label);

        this.setOnClickListener(new OpenURLClickListener(context));
    }

    public void setData(String url, String oddTextValue, float oddValue, boolean isLive) {
        if (url.isEmpty()) {
            final int textColor = Colors.textColorThird;
            valueTextView.setBackgroundResource(backgroundTransparentResourceId);

            valueTextView.setTextColor(textColor);
            setClickable(false);
            setTag(OpenURLClickListener.TAG_URL, null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                oddButton.setElevation(0);
        } else {
            valueTextView.setTextColor(Color.WHITE);
            valueTextView.setBackgroundResource(R.drawable.btn_round_edge_background);
            setClickable(true);
            this.setTag(OpenURLClickListener.TAG_URL, url);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                oddButton.setElevation((int) ((3 * ModelData.getInstance().displayDensity) + 0.5f));
        }

        valueTextView.setText(oddTextValue);

        if (isLive) {
            arrowImageView.setVisibility(View.INVISIBLE);
            setButtonOddArrow(arrowImageView, oddValue);
        } else {
            arrowImageView.setVisibility(View.GONE);
        }
    }

    private void setButtonOddArrow(final ImageView arrow, float newValue) {
        boolean createAnimation = false;

        if (oldOddValue < 0) {
            arrow.setVisibility(View.INVISIBLE);
        } else if (newValue < oldOddValue) {
            createAnimation = true;
            arrow.setImageResource(R.drawable.icon_form_down);
            arrow.setVisibility(View.VISIBLE);
        } else if (newValue > oldOddValue) {
            createAnimation = true;
            arrow.setImageResource(R.drawable.icon_form_up);
            arrow.setVisibility(View.VISIBLE);
        } else
            arrow.setVisibility(View.INVISIBLE);

        if (createAnimation) {
            final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatCount(10); // Repeat animation 10 times
            animation.setRepeatMode(Animation.REVERSE);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    arrow.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });

            arrow.setAnimation(animation);
        }

        oldOddValue = newValue;
    }
}
