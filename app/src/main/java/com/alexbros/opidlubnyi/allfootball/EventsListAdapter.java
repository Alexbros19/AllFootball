package com.alexbros.opidlubnyi.allfootball;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.EventsViewHolder> {

    private Context context;
    private List<ListElement> listElements;

    public EventsListAdapter(Context context, List<ListElement> listElements) {
        this.context = context;
        this.listElements = listElements;
    }

    public void setData(List<ListElement> listElements) {
        this.listElements = listElements;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_event_list_item, parent, false);
        return new EventsViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsListAdapter.EventsViewHolder holder, int position) {
        final ListElement listElement = listElements.get(position);

        holder.eventView.setElement(listElement);
        holder.eventView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(EventsListActivity.this, ButtonsContextActivity.class);
//                intent.putExtra(SyncStateContract.Constants.BUTTONS_CONTEXT_ACTIVITY_PARAM, listElement);
//                startActivity(intent);
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
