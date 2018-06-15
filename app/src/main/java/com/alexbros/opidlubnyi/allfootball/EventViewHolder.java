package com.alexbros.opidlubnyi.allfootball;

import android.view.View;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class EventViewHolder extends ChildViewHolder {
    EventView eventView;

    public EventViewHolder(View itemView) {
        super(itemView);
        eventView = (EventView) itemView;
    }
}
