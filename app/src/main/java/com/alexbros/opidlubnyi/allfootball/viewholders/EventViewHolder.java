package com.alexbros.opidlubnyi.allfootball.viewholders;

import android.view.View;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.EventView;
import com.alexbros.opidlubnyi.allfootball.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class EventViewHolder extends ChildViewHolder {

    private TextView firstTeamTitle;
    private TextView secondTeamTitle;
    public EventView eventView;

    public EventViewHolder(View itemView) {
        super(itemView);
        firstTeamTitle = itemView.findViewById(R.id.firstTeamName);
        secondTeamTitle = itemView.findViewById(R.id.secondTeamName);
        eventView = (EventView) itemView;
    }

    public void setTeamTitle(String firstName, String secondName) {
        firstTeamTitle.setText(firstName);
        secondTeamTitle.setText(secondName);
    }
}
