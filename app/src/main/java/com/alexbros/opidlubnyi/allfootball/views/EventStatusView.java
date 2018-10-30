package com.alexbros.opidlubnyi.allfootball.views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.R;

public class EventStatusView extends RelativeLayout {
    private Context context;
    private final static int BLINK_DURATION = 1500;
    private ObjectAnimator objectAnimator = null;
    private boolean blinking = false;
    private TextView statusTextView;
    private TextView cursorTextView;

    public EventStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public EventStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EventStatusView(Context context) {
        super(context);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EventStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    //    @SuppressLint("SetTextI18n")
    private void init(Context context) {
        inflate(context, R.layout.event_status_view_layout, this);

        this.context = context;

        statusTextView = findViewById(R.id.status_text_view);
        cursorTextView = findViewById(R.id.cursor_text_view);

//        if (isInEditMode()) {
//            statusTextView.setText("12:25");
//            cursorTextView.setVisibility(VISIBLE);
//        }
    }

    public void setIsEventDetails() {
        final float textSize = android.text.format.DateFormat.is24HourFormat(context) ? 16 : 12;

        statusTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        cursorTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setIsScores() {
        if (!android.text.format.DateFormat.is24HourFormat(context)) {
            final float textSize = 11;
            statusTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            cursorTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    public void setTextColor(int color) {
        statusTextView.setTextColor(color);
        cursorTextView.setTextColor(color);
    }

    public void setStatusText(String text) {
        String tempText = text;

        if (text.endsWith("'")) {
            cursorTextView.setVisibility(VISIBLE);
            setBlinking(true);
            tempText = text.substring(0, text.length() - 1);
        } else {
            cursorTextView.setVisibility(GONE);
            setBlinking(false);
        }

        statusTextView.setText(tempText);
    }

    private void setBlinking(boolean b) {
        //effect running
        if (this.blinking && !b) {
            if (objectAnimator != null)
                objectAnimator.end();
        } else if (!this.blinking && b) {
            if (objectAnimator == null) {
                objectAnimator = ObjectAnimator.ofFloat(cursorTextView, "alpha", 0.0f);
                objectAnimator.setDuration(BLINK_DURATION);
                objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
                objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            }

            objectAnimator.start();
        }

        blinking = b;
    }
}
