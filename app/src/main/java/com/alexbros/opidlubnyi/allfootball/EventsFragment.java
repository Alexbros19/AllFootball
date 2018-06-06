package com.alexbros.opidlubnyi.allfootball;


import android.os.Bundle;
import android.os.Parcelable;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class EventsFragment extends Fragment {

    private View view;
    private View progressBar;
    private RecyclerView recyclerView;
    private EventsListAdapter eventsListAdapter;
    private List<ListElement> eventsList;
    private static Bundle bundleRecyclerViewState;
    private ParsingJsonHelper parsingJsonHelper = null;

    public EventsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = view.findViewById(R.id.eventsList);
        progressBar = view.findViewById(R.id.progressBar);
        eventsListAdapter = new EventsListAdapter(getContext(), eventsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(eventsListAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        bundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundleRecyclerViewState.putParcelable(Constants.KEY_RECYCLER_STATE, listState);

        if (parsingJsonHelper != null)
            parsingJsonHelper.cancel(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bundleRecyclerViewState != null) {
            Parcelable listState = bundleRecyclerViewState.getParcelable(Constants.KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }

        // TODO: start AsyncTask here
        parsingJsonHelper = new ParsingJsonHelper(new ParsingJsonHelper.OnDataListener() {
            @Override
            public void onReceived(List<ListElement> list) {
                setData(list);
            }
        });
        parsingJsonHelper.execute(Constants.URL);
    }

    public void setData(List<ListElement> listElems) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        eventsListAdapter.setData(listElems);
    }
}
