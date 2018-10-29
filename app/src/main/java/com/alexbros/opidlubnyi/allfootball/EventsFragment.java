package com.alexbros.opidlubnyi.allfootball;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final long AUTO_REFRESH_DELAY = 120000;
    private static final long AUTO_REFRESH_OFFLINE_DELAY = 5000;
    private static final long AUTO_REFRESH_LIVE_DELAY = 30000;

    private View progressBar;
    private RecyclerView recyclerView;
    private EventsListAdapter eventsListAdapter;
    private static Bundle bundleRecyclerViewState;
    private ParsingJsonHelper parsingJsonHelper = null;
    private SearchView mSearchView;
    private MenuItem refreshMenuItem;
    private FragmentActivity activity;
    private List<ListElement> listElements = new ArrayList();
    private boolean isRunningEvent = false;

    private PeriodicalTimer autoUpdateTimer = null;
    private Runnable autoUpdateTimerTask = this::reloadCurrentViewFromServer;
    private final ParseScoreCompleteHandler parseScoreCompleteHandler = new ParseScoreCompleteHandler(this);

    public EventsFragment() {
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isAdded())
                return;

            String action = intent.getAction();

            if(action != null && action.equals(Constants.BROADCAST_ACTION_RELOAD_SCORES)) {
                reloadScores();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        activity = getActivity();

        recyclerView = view.findViewById(R.id.eventsList);
        progressBar = view.findViewById(R.id.progressBar);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        eventsListAdapter = new EventsListAdapter(getContext());
        recyclerView.setAdapter(eventsListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.eventsListSwipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            Intent reloadScoresIntent = new Intent(Constants.BROADCAST_ACTION_RELOAD_SCORES);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(reloadScoresIntent);
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Objects.requireNonNull(getActivity()), linearLayoutManager.getOrientation());
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

        LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcastReceiver);

        autoUpdateTimer.stop();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (bundleRecyclerViewState != null) {
            Parcelable listState = bundleRecyclerViewState.getParcelable(Constants.KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }

        // TODO: start AsyncTask here
        parsingJsonHelper = new ParsingJsonHelper(null, list -> {
            setData(list);
            listElements = list;
        });
        parsingJsonHelper.execute(Constants.URL);

        if (autoUpdateTimer == null)
            autoUpdateTimer = new PeriodicalTimer(AUTO_REFRESH_DELAY, autoUpdateTimerTask);
        reloadScores();
        autoUpdateTimer.start();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_ACTION_RELOAD_SCORES);
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, intentFilter);
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
//        refreshMenuItem.setActionView(R.layout.actionbar_progress_refresh);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

//        if (itemId == filterMenuItem.getItemId()) {
//            FilterDialog.createDialog(activity, activity.getLayoutInflater(), model).show();
//            return true;
//        } else if (itemId == refreshMenuItem.getItemId()) {
        if (itemId == refreshMenuItem.getItemId()) {
            reloadScores();
            return true;
        }
        return false;
    }

    private void reloadScores() {
        long delay = AUTO_REFRESH_DELAY;
        if (isRunningEvent) {
            isRunningEvent = false;
            delay = AUTO_REFRESH_LIVE_DELAY;
        }
        if (autoUpdateTimer == null) {
            autoUpdateTimer = new PeriodicalTimer(delay, autoUpdateTimerTask);
            autoUpdateTimer.start();
        } else {
            autoUpdateTimer.restart(delay, 1);
        }
    }

    private void reloadCurrentViewFromServer() {
        setRefreshMenuItemLoading(true);

        try {
            parsingJsonHelper = new ParsingJsonHelper(parseScoreCompleteHandler, list -> {
                setData(list);
                listElements = list;
            });
            parsingJsonHelper.execute(Constants.URL);
        } catch (OutOfMemoryError e) {
            setRefreshMenuItemLoading(false);
        }
    }

    private void setRefreshMenuItemLoading(boolean visible) {
        if (refreshMenuItem == null)
            return;

        if (visible)
            refreshMenuItem.setActionView(R.layout.actionbar_progress_refresh);
        else
            refreshMenuItem.setActionView(null);
    }

    private void enableSearchMode(boolean enable) {
        boolean visibility = !enable;
        refreshMenuItem.setVisible(visibility);
    }

    @SuppressWarnings("unchecked")
    private static class ParseScoreCompleteHandler extends Handler {
        private final WeakReference<EventsFragment> parentWeakRef;

        ParseScoreCompleteHandler(EventsFragment parent) {
            this.parentWeakRef = new WeakReference<>(parent);
        }

        @Override
        public void handleMessage(Message msg) {
            final EventsFragment parent = parentWeakRef.get();
            if (parent == null)
                return;

            if (!parent.isAdded())
                return;

            parent.isRunningEvent = (msg.arg2 == 1);

            if (msg.what == Constants.RESULT_OK) {

                if (parent.isRunningEvent) {
                    parent.isRunningEvent = false;
                    parent.autoUpdateTimer.restart(AUTO_REFRESH_LIVE_DELAY, AUTO_REFRESH_LIVE_DELAY);
                } else {
                    parent.autoUpdateTimer.restart(AUTO_REFRESH_DELAY, AUTO_REFRESH_DELAY);
                }
            } else {
                parent.autoUpdateTimer.restart(AUTO_REFRESH_OFFLINE_DELAY, AUTO_REFRESH_OFFLINE_DELAY);
            }
            parent.setRefreshMenuItemLoading(false);
        }
    }
}
