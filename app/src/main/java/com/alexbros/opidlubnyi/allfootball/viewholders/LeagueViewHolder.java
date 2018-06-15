package com.alexbros.opidlubnyi.allfootball.viewholders;

import android.view.View;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.R;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class LeagueViewHolder extends GroupViewHolder {

    private TextView leagueTitle;

    public LeagueViewHolder(View itemView) {
        super(itemView);
        leagueTitle = itemView.findViewById(R.id.all_events_title);
    }

    public void setLeagueTitle(String name) {
        leagueTitle.setText(name);
    }
}
