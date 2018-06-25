package com.alexbros.opidlubnyi.allfootball.viewholders;

import android.view.View;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class EventViewHolder extends ChildViewHolder {

    private TextView timeTextView;
    private TextView firstTeamTitle;
    private TextView secondTeamTitle;

    public EventViewHolder(View itemView) {
        super(itemView);
        timeTextView = itemView.findViewById(R.id.timeText);
        firstTeamTitle = itemView.findViewById(R.id.firstTeamName);
        secondTeamTitle = itemView.findViewById(R.id.secondTeamName);
    }

    public void setTeamTitle(String time, String firstName, String secondName) {
        timeTextView.setText(time);
        firstTeamTitle.setText(firstName);
        secondTeamTitle.setText(secondName);
    }
}
