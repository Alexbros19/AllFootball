package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.models.EventPredictionMessage;

import java.util.ArrayList;

public class PredictionMessagesView extends LinearLayout {
    private Context context;
    private LayoutInflater inflater;
    private ViewFlipper viewFlipper;
//    private OnEventListener onEventListener = null;

//    public interface OnEventListener {
//        void onUserProfileClick(String userId, String userName);
//    }

    public PredictionMessagesView(Context context) {
        super(context);
        init(context);
    }

    public PredictionMessagesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PredictionMessagesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.event_voting_prediction_message, this);

        this.context = context;
        this.inflater = LayoutInflater.from(context);

        if (!isInEditMode()) {
            viewFlipper = findViewById(R.id.predictionMessagesAdapterViewFlipper);
            viewFlipper.setInAnimation(context, android.R.anim.fade_in);
            viewFlipper.setOutAnimation(context, android.R.anim.fade_out);
        }
    }

//    public void setEventListener(OnEventListener onEventListener) {
//        this.onEventListener = onEventListener;
//    }

    public void setData(ArrayList<EventPredictionMessage> messages) {
        if (messages.isEmpty()) {
            viewFlipper.stopFlipping();
            viewFlipper.setVisibility(GONE);
        } else {
            viewFlipper.startFlipping();
            viewFlipper.setVisibility(VISIBLE);
        }

        updateList(messages);
    }

    static private final int TAG_MESSAGE = R.id.userNameSlidingLayer;
    private class MessageViewHolder {
        TextView userNameTextView;
        TextView userMessageTextView;
        ImageView userHeadImageView;
    }

//    private OnClickListener messageOnClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            EventPredictionMessage message = (EventPredictionMessage) v.getTag(TAG_MESSAGE);
//
//            if (message != null) {
//                if (onEventListener != null)
//                    onEventListener.onUserProfileClick(message.userIdHash, message.userName);
//            }
//        }
//    };

    private void updateList(ArrayList<EventPredictionMessage> messages) {

        int index = 0;
        for (EventPredictionMessage message : messages) {
            MessageViewHolder holder;

            View view = viewFlipper.getChildAt(index);

            if (view == null) {
                view = inflater.inflate(R.layout.fragment_event_detail_details_prediction_user, viewFlipper, false);

                holder = new MessageViewHolder();

//                view.setOnClickListener(messageOnClickListener);

                holder.userNameTextView = view.findViewById(R.id.userNameSlidingLayer);
                holder.userMessageTextView = view.findViewById(R.id.userMessageTextView);
                holder.userHeadImageView = view.findViewById(R.id.userHeadImageView);

                view.setTag(holder);

                viewFlipper.addView(view);
            } else
                holder = (MessageViewHolder) view.getTag();

            view.setTag(TAG_MESSAGE, message);

            holder.userNameTextView.setText(message.userName);
//            if (message.isMe)
//                holder.userNameTextView.setTextColor(Colors.orangeColor);

            String comment;
            switch (message.tip) {
                case "1":
                    comment = message.teamOneLocalized;
                    break;
                case "X":
                    comment = context.getString(R.string.string_draw);
                    break;
                default:
                    comment = message.teamTwoLocalized;
                    break;
            }

            if (!message.message.equals(""))
                comment = comment + ": " + message.message;

            holder.userMessageTextView.setText(comment);

            UserImageView.setImage(context, "", message.status, R.drawable.head_user_small, holder.userHeadImageView);

            index++;
        }

        if (viewFlipper.getChildCount() > messages.size())
            viewFlipper.removeViewAt(viewFlipper.getChildCount() - 1);
    }
}
