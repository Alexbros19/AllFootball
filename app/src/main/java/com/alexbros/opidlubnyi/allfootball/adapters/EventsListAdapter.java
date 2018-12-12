package com.alexbros.opidlubnyi.allfootball.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.activities.EventContextActivity;
import com.alexbros.opidlubnyi.allfootball.fragments.EventsListFragment;
import com.alexbros.opidlubnyi.allfootball.views.EventsListEventItemView;
import com.alexbros.opidlubnyi.allfootball.views.EventsListLeagueItemView;

import java.util.ArrayList;
import java.util.List;

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder> {
    private static final int LIST_ITEM_TYPE_LEAGUE = 0;
    private static final int LIST_ITEM_TYPE_EVENT = 1;

    private FragmentActivity activity;
    private LayoutInflater layoutInflater;
    private ArrayList<Object> dataSet = new ArrayList<>();

    public EventsListAdapter(FragmentActivity activity, Context context) {
        this.activity = activity;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<Object> eventsList) {
        dataSet.clear();
        dataSet.addAll(eventsList);
        notifyDataSetChanged();
    }

    public void updateFilteredData(List<Object> newList) {
        dataSet.clear();
        dataSet.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LIST_ITEM_TYPE_LEAGUE)
            return new LeagueViewHolder(layoutInflater.inflate(R.layout.event_list_league_item_layout, parent, false));
        else if (viewType == LIST_ITEM_TYPE_EVENT)
            return new EventViewHolder(new EventsListEventItemView(parent.getContext()));

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object data = dataSet.get(position);

        if (holder instanceof LeagueViewHolder) {
            LeagueViewHolder leagueViewHolder = (LeagueViewHolder) holder;
            EventsListFragment.ListItemLeague listItemLeague = (EventsListFragment.ListItemLeague) data;
            leagueViewHolder.leagueListItemView.setData(listItemLeague.league);
        } else if (holder instanceof EventViewHolder) {
            EventViewHolder eventViewHolder = (EventViewHolder) holder;
            EventsListFragment.ListItemEvent listItemEvent = (EventsListFragment.ListItemEvent) data;
            eventViewHolder.eventsListEventItemView.setData(listItemEvent.eventsListEvent);
            ((EventViewHolder) holder).eventsListEventItemView.setOnClickListener(view -> EventContextActivity.showActivity(
                    activity,
                    listItemEvent.eventsListEvent.event.getFirstTeamId(),
                    listItemEvent.eventsListEvent.event.getSecondTeamId()
            ));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = dataSet.get(position);

        if (item instanceof EventsListFragment.ListItemLeague)
            return LIST_ITEM_TYPE_LEAGUE;
        else if (item instanceof EventsListFragment.ListItemEvent)
            return LIST_ITEM_TYPE_EVENT;

        return -1;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class LeagueViewHolder extends ViewHolder {
        final EventsListLeagueItemView leagueListItemView;

        LeagueViewHolder(View view) {
            super(view);

            leagueListItemView = (EventsListLeagueItemView) view;
        }
    }

    private class EventViewHolder extends ViewHolder {
        final EventsListEventItemView eventsListEventItemView;

        EventViewHolder(View view) {
            super(view);

            eventsListEventItemView = (EventsListEventItemView) view;
        }
    }
}
