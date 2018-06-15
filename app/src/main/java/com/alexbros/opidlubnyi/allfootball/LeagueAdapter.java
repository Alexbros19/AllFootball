package com.alexbros.opidlubnyi.allfootball;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class LeagueAdapter extends ExpandableRecyclerViewAdapter<LeagueViewHolder, EventViewHolder> {

    private List<ListElement> listElements = new ArrayList();

    LeagueAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    public void setData(List<ListElement> listElements) {
        this.listElements = listElements;
        notifyDataSetChanged();
    }

    @Override
    public LeagueViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collapse_events_parent, parent, false);
        return new LeagueViewHolder(view);
    }

    @Override
    public EventViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_list_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(EventViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final ListElement listElement = (ListElement) group.getItems().get(childIndex);
        holder.eventView.setElement(listElement);
    }

    @Override
    public void onBindGroupViewHolder(LeagueViewHolder holder, int flatPosition, ExpandableGroup group) {

    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }
}
