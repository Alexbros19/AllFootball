package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.models.EventPrediction;

public class EventVotingShowResultsView extends LinearLayout {
    private Context context;
    private PredictionMessagesView predictionMessagesView;

    private TextView predictionVotingTeamTextView1;
    private TextView predictionVotingTeamTextView2;

    private VotingView predictionVotingView1;
    private VotingView predictionVotingView2;
    private VotingView predictionVotingViewX;

    private TextView predictionTextView1;
    private TextView predictionTextView2;
    private TextView predictionTextViewX;

    public interface OnEventListener {
        void onUserProfileClick(String userId, String userName);
    }

    public EventVotingShowResultsView(Context context) {
        super(context);
        init(context);
    }

    public EventVotingShowResultsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EventVotingShowResultsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.event_voting_result_layout, this);

        this.context = context;

        predictionMessagesView = findViewById(R.id.resultPredictionMessagesViewFlipper);

        predictionVotingTeamTextView1 = findViewById(R.id.predictionVotingTeamTextView1);
        predictionVotingTeamTextView2 = findViewById(R.id.predictionVotingTeamTextView2);

        predictionVotingView1 = findViewById(R.id.predictionVotingView1);
        predictionVotingView2 = findViewById(R.id.predictionVotingView2);
        predictionVotingViewX = findViewById(R.id.predictionVotingViewX);

        predictionTextView1 = findViewById(R.id.predictionVotingPercentageTextView1);
        predictionTextView2 = findViewById(R.id.predictionVotingPercentageTextView2);
        predictionTextViewX = findViewById(R.id.predictionVotingPercentageTextViewX);
    }

//    public void setEventListener(final OnEventListener onEventListener) {
//        if (onEventListener != null) {
//            predictionMessagesView.setEventListener(new PredictionMessagesView.OnEventListener() {
//                @Override
//                public void onUserProfileClick(String userId, String userName) {
//                    onEventListener.onUserProfileClick(userId, userName);
//                }
//            });
//        }
//    }

    private boolean withAnimation = true;
    public void setData(EventPrediction eventPrediction) {
        predictionMessagesView.setData(eventPrediction.messages);

        predictionVotingTeamTextView1.setText(eventPrediction.teamOneLocalized);
        predictionVotingTeamTextView2.setText(eventPrediction.teamTwoLocalized);

        predictionVotingView1.setVotes(eventPrediction.votes1Percentage, withAnimation);
        predictionVotingView2.setVotes(eventPrediction.votes2Percentage, withAnimation);
        predictionVotingViewX.setVotes(eventPrediction.votesXPercentage, withAnimation);

        withAnimation = false;

        predictionTextView1.setText(eventPrediction.votes1PercentageString + "% (" + eventPrediction.votes1 + ")");
        predictionTextView2.setText(eventPrediction.votes2PercentageString + "% (" + eventPrediction.votes2 + ")");
        predictionTextViewX.setText(eventPrediction.votesXPercentageString + "% (" + eventPrediction.votesX + ")");
    }
}
