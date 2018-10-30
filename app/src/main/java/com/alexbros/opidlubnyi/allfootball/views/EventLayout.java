package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class EventLayout extends RelativeLayout {
    private Paint backgroundPaint;
    private int backgroundColorStatus = 0;
    private int backgroundColor = 0;
    private float statusWidth = 0;

    public EventLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // backgroundPaint.setAntiAlias(true);

        setWillNotDraw(false);
    }

    public void setBackgroundColors(int backgroundColorStatus, int backgroundColor, float statusWidth) {
        this.backgroundColorStatus = backgroundColorStatus;
        this.backgroundColor = backgroundColor;
        this.statusWidth = statusWidth;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        backgroundPaint.setColor(backgroundColor);
        canvas.drawRect(statusWidth, 0, getWidth(), getHeight(), backgroundPaint);

        backgroundPaint.setColor(backgroundColorStatus);
        canvas.drawRect(0, 0, statusWidth, getHeight(), backgroundPaint);
    }
}
