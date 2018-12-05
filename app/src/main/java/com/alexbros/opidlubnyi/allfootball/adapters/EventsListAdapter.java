package com.alexbros.opidlubnyi.allfootball.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexbros.opidlubnyi.allfootball.activities.EventContextActivity;
import com.alexbros.opidlubnyi.allfootball.views.EventView;
import com.alexbros.opidlubnyi.allfootball.models.ListElement;
import com.alexbros.opidlubnyi.allfootball.R;

import java.util.ArrayList;
import java.util.List;

public class EventsListAdapter extends RecyclerView.Adapter {

    private FragmentActivity activity;
    private List<ListElement> listElements = new ArrayList();

    public EventsListAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    public void setData(List<ListElement> listElements) {
        this.listElements = listElements;
        notifyDataSetChanged();
    }

    public void updateFilteredData(List<ListElement> newList) {
        this.listElements = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new EventsViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ListElement listElement = listElements.get(position);

        ((EventsViewHolder) holder).eventView.setElement(listElement);
        ((EventsViewHolder) holder).eventView.setOnClickListener(view -> {
            EventContextActivity.showActivity(
                    activity,
                    listElement.getFirstTeamId(),
                    listElement.getSecondTeamId()
            );
        });
    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }

    class EventsViewHolder extends RecyclerView.ViewHolder {
        EventView eventView;

        private EventsViewHolder(View itemView) {
            super(itemView);
            eventView = (EventView) itemView;
        }
    }
}
