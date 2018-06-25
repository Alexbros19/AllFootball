package com.alexbros.opidlubnyi.allfootball;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexbros.opidlubnyi.allfootball.adapters.LeagueAdapter;
import com.alexbros.opidlubnyi.allfootball.models.League;

import java.util.ArrayList;
import java.util.List;

public class CollapseEventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeagueAdapter leagueAdapter;
    private List<League> leagueList;
    private View progressBar;
    private static Bundle bundleRecyclerViewState;
    private ParsingJsonHelper parsingJsonHelper = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        recyclerView = view.findViewById(R.id.eventsList);
        progressBar = view.findViewById(R.id.progressBar);

//        setLeagueList();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        leagueAdapter = new LeagueAdapter(leagueList);
        recyclerView.setAdapter(leagueAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

//    public void setLeagueList() {
//        progressBar.setVisibility(View.GONE);
//        recyclerView.setVisibility(View.VISIBLE);
//
//        leagueList = new ArrayList<>(1);
//        for (int i = 0; i < 1; i++) {
//            List<ListElement> listElements = new ArrayList<>(5);
//            listElements.add(new ListElement("team " + i + 1, "team " + i + 2));
//            listElements.add(new ListElement("team " + i + 1, "team " + i + 2));
//            listElements.add(new ListElement("team " + i + 1, "team " + i + 2));
//            listElements.add(new ListElement("team " + i + 1, "team " + i + 2));
//            listElements.add(new ListElement("team " + i + 1, "team " + i + 2));
//            leagueList.add(new League("All Leagues", listElements));
//        }
//    }

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

                leagueAdapter = new LeagueAdapter(leagueList);
                recyclerView.setAdapter(leagueAdapter);
            }
        });
        parsingJsonHelper.execute(Constants.URL);
    }

    public void setData(List<ListElement> listElems) {

        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        leagueList = new ArrayList<>(1);
        for (int i = 0; i < 1; i++) {
            List<ListElement> listElements = new ArrayList<>(listElems.size());

            for (int j = 0; j < listElems.size() - 1; j++) {
                listElements.add(new ListElement());
            }

            leagueList.add(new League("All Leagues", listElements));
        }
//        leagueAdapter.setData(listElems);
    }
}
