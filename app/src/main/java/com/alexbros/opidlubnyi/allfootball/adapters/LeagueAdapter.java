package com.alexbros.opidlubnyi.allfootball.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexbros.opidlubnyi.allfootball.EventView;
import com.alexbros.opidlubnyi.allfootball.EventsListAdapter;
import com.alexbros.opidlubnyi.allfootball.ListElement;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.viewholders.EventViewHolder;
import com.alexbros.opidlubnyi.allfootball.viewholders.LeagueViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class LeagueAdapter extends ExpandableRecyclerViewAdapter<LeagueViewHolder, EventViewHolder> {

    public LeagueAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public LeagueViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collapse_league_item, parent, false);
        return new LeagueViewHolder(view);
    }

    @Override
    public EventViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_list_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(EventViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        ListElement listElement = (ListElement) group.getItems().get(childIndex);
        holder.setTeamTitle(listElement.getTimeTextView(), listElement.getFirstTeamName(), listElement.getSecondTeamName());
    }

    @Override
    public void onBindGroupViewHolder(LeagueViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setLeagueTitle(group.getTitle());
    }
}
