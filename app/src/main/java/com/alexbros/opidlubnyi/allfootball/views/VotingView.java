package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.alexbros.opidlubnyi.allfootball.Colors;
import com.alexbros.opidlubnyi.allfootball.models.ModelData;

public class VotingView extends View {
    private final static long GAP_IN_DP = 3;

    private double votes = 0;
    private double drawnVotes = 0;

    private int gap;
    private Paint barPaint, bgPaint;

    public VotingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        barPaint = new Paint();
        barPaint.setColor(Colors.accentColor);

        bgPaint = new Paint();

//        if (!isInEditMode()) {
//            if (PreferenceHelper.isAppThemeWhite(context))
//                bgPaint.setColor(Colors.separatorWhiteTheme);
//            else
                bgPaint.setColor(Colors.textColorThird);
//        }

        gap = (int) ((float) GAP_IN_DP * ModelData.getInstance().displayDensity + 0.5f);
    }

    public void setVotes(double votes, boolean animate) {
        this.votes = votes;

        if (animate) {
            drawnVotes = 0;
            this.post(barAnimation);
        } else {
            drawnVotes = votes;
            invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        float width = (this.getWidth() / 100.0f);
        float heightThird = (this.getHeight() / 3.0f);

        canvas.drawRect(0, 0, width * (float)drawnVotes - gap, this.getHeight(), barPaint);
        canvas.drawRect(width * (float)drawnVotes, heightThird, this.getWidth(), this.getHeight() - heightThird, bgPaint);

        super.onDraw(canvas);
    }

    private Runnable barAnimation = new Runnable() {

        @Override
        public void run() {
            drawnVotes++;

            if (drawnVotes > votes)
                drawnVotes = votes;
            else
                VotingView.this.post(barAnimation);

            VotingView.this.invalidate();
        }
    };
}
