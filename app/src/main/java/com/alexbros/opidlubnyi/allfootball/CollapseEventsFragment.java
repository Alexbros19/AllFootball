package com.alexbros.opidlubnyi.allfootball;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class CollapseEventsFragment extends Fragment {

    private View progressBar;
    private RecyclerView recyclerView;
    private LeagueAdapter leagueAdapter;
    private List<LeagueContext> leagueContextList = new ArrayList<>();
    private static Bundle bundleRecyclerViewState;
    private ParsingJsonHelper parsingJsonHelper = null;

    public CollapseEventsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = view.findViewById(R.id.eventsList);
        progressBar = view.findViewById(R.id.progressBar);

        getEvents();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        leagueAdapter = new LeagueAdapter(leagueContextList);
        recyclerView.setAdapter(leagueAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
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

//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (bundleRecyclerViewState != null) {
//            Parcelable listState = bundleRecyclerViewState.getParcelable(Constants.KEY_RECYCLER_STATE);
//            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
//        }
//
//        // TODO: start AsyncTask here
//        parsingJsonHelper = new ParsingJsonHelper(new ParsingJsonHelper.OnDataListener() {
//            @Override
//            public void onReceived(List<ListElement> list) {
//                setData(list);
//                leagueContextList.add(new LeagueContext("", list));
//            }
//        });
//        parsingJsonHelper.execute(Constants.URL);
//    }


    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void setData(List<ListElement> listElems) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        leagueAdapter.setData(listElems);
    }

    public void getEvents() {
        leagueContextList = new ArrayList<>(6);
        for (int i = 0; i < 6; i++) {
            List<ListElement> events = new ArrayList<>(3);
            events.add(new ListElement());
            events.add(new ListElement());
            events.add(new ListElement());
            leagueContextList.add(new LeagueContext("", events));
        }
    }
}
