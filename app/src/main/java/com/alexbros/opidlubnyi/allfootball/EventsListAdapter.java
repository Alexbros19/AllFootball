package com.alexbros.opidlubnyi.allfootball;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class EventsListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ListElement> listElements = new ArrayList();

    EventsListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ListElement> listElements) {
        this.listElements = listElements;
        notifyDataSetChanged();
    }

    public void updateFilteredData(List<ListElement> newList) {
        listElements = new ArrayList<>();
        listElements.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_list_item, parent, false);
        return new EventsViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ListElement listElement = listElements.get(position);

        ((EventsViewHolder) holder).eventView.setElement(listElement);
        ((EventsViewHolder) holder).eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventContextActivity.class);
                intent.putExtra(Constants.BUTTONS_CONTEXT_ACTIVITY_PARAM, listElement);
                context.startActivity(intent);
            }
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
