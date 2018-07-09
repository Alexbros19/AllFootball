package com.alexbros.opidlubnyi.allfootball;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private View progressBar;
    private RecyclerView recyclerView;
    private EventsListAdapter eventsListAdapter;
    private static Bundle bundleRecyclerViewState;
    private ParsingJsonHelper parsingJsonHelper = null;
    private String searchText;
    private SearchView mSearchView;
    private MenuItem refreshMenuItem;
    private FragmentActivity activity;
    private List<ListElement> listElements = new ArrayList();

    public EventsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        searchText = "";
        activity = getActivity();

        recyclerView = view.findViewById(R.id.eventsList);
        progressBar = view.findViewById(R.id.progressBar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        eventsListAdapter = new EventsListAdapter(getContext());
        recyclerView.setAdapter(eventsListAdapter);
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

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && mSearchView != null)
            imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
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
                listElements = list;
            }
        });
        parsingJsonHelper.execute(Constants.URL);
    }

    public void setData(List<ListElement> listElems) {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        eventsListAdapter.setData(listElems);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<ListElement> newList = new ArrayList<>();

        for (ListElement listElement : listElements)
        {
            if(listElement.getFirstTeamName().toLowerCase().contains(userInput) || listElement.getSecondTeamName().toLowerCase().contains(userInput)) {
                newList.add(listElement);
            }
        }

        eventsListAdapter.updateFilteredData(newList);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!isAdded())
            return;

        menu.clear();

        inflater.inflate(R.menu.menu_events_list, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchMenuItem.getActionView();
        searchMenuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(activity.getResources().getString(R.string.string_search));
        mSearchView.setOnSearchClickListener(v -> enableSearchMode(true));
        mSearchView.setOnCloseListener(() -> {
            enableSearchMode(false);
            return false;
        });

        refreshMenuItem = menu.findItem(R.id.action_refresh);
        refreshMenuItem.setActionView(R.layout.actionbar_progress_refresh);
    }

    private void enableSearchMode(boolean enable) {
        boolean visibility = !enable;
        refreshMenuItem.setVisible(visibility);
    }
}
