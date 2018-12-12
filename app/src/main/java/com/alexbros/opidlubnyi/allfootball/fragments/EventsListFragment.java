package com.alexbros.opidlubnyi.allfootball.fragments;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.alexbros.opidlubnyi.allfootball.Constants;
import com.alexbros.opidlubnyi.allfootball.PeriodicalTimer;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.adapters.EventsListAdapter;
import com.alexbros.opidlubnyi.allfootball.adapters.EventsListHorizontalPageAdapter;
import com.alexbros.opidlubnyi.allfootball.async_tasks.GetEventsAsyncTask;
import com.alexbros.opidlubnyi.allfootball.async_tasks.GetEventsAsyncTask.OnCompleteListener;
import com.alexbros.opidlubnyi.allfootball.dialogs.FilterDialog;
import com.alexbros.opidlubnyi.allfootball.helpers.URLContentHelper;
import com.alexbros.opidlubnyi.allfootball.models.EventsListItem;
import com.alexbros.opidlubnyi.allfootball.models.League;
import com.alexbros.opidlubnyi.allfootball.models.ModelData;
import com.alexbros.opidlubnyi.allfootball.views.PagerSlidingTabStrip;
import com.alexbros.opidlubnyi.allfootball.views.ZoomOutPageTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EventsListFragment extends Fragment implements SearchView.OnQueryTextListener {
    private static final long AUTO_REFRESH_DELAY = 120000;
    private static final long AUTO_REFRESH_OFFLINE_DELAY = 5000;
    private static final long AUTO_REFRESH_LIVE_DELAY = 30000;
    private static int currentEventsList = 1;

    private FragmentActivity activity;
    private View rootView;
    private Toolbar toolbar;
    private ArrayList<View> views;
    PagerSlidingTabStrip tabPageIndicator;
    private SearchView searchView;
    private MenuItem refreshMenuItem;
    private MenuItem filterMenuItem;
    private boolean isRunningEvent = false;
    private ModelData model;
    private List<Object> eventsListDataSet = null;
    private EventsListItem.EventsListLeague lastEventsListLeague = null;

    private PeriodicalTimer autoUpdateTimer = null;
    private Runnable autoUpdateTimerTask = this::reloadCurrentViewFromServer;
    private final ParseScoreCompleteHandler parseScoreCompleteHandler = new ParseScoreCompleteHandler(this);

    public EventsListFragment() {
        model = ModelData.getInstance();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isAdded())
                return;

            String action = intent.getAction();

            if (action != null && action.equals(Constants.BROADCAST_ACTION_RELOAD_SCORES)) {
                reloadScores();
            }

            if (action != null && action.equals(Constants.BROADCAST_ACTION_REDRAW_SCORES)) {
                reloadCurrentViewFromServer();
                setOptionsMenuVisibilityAndStyle();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        views = new ArrayList<>();
    }

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.fragment_events_list, container, false);

        initToolbar();

        tabPageIndicator = rootView.findViewById(R.id.tabPageIndicatorEventsList);

        View currentView = inflater.inflate(R.layout.fragment_events_list_container, container, false);
        currentView.setId(R.layout.fragment_events_list_container);
        swipeRefreshLayout(currentView);

        RecyclerView recyclerViewEventsList;
        recyclerViewEventsList = currentView.findViewById(R.id.recyclerViewEventsList);
        setDivider(recyclerViewEventsList);
        setListAdapter(recyclerViewEventsList);

        views.add(currentView);

        currentView = inflater.inflate(R.layout.fragment_events_list_container, container, false);
        currentView.setId(R.layout.fragment_events_list_container);
        swipeRefreshLayout(currentView);

        recyclerViewEventsList = currentView.findViewById(R.id.recyclerViewEventsList);
        setDivider(recyclerViewEventsList);
        setListAdapter(recyclerViewEventsList);

        views.add(currentView);

        currentView = inflater.inflate(R.layout.fragment_events_list_container, container, false);
        currentView.setId(R.layout.fragment_events_list_container);
        swipeRefreshLayout(currentView);

        recyclerViewEventsList = currentView.findViewById(R.id.recyclerViewEventsList);
        setDivider(recyclerViewEventsList);
        setListAdapter(recyclerViewEventsList);

        views.add(currentView);

        ViewPager viewPager = rootView.findViewById(R.id.viewPagerEventsList);
        viewPager.setAdapter(new EventsListHorizontalPageAdapter(views, activity));
        int currentMainPagerViewId = 1;
        viewPager.setCurrentItem(currentMainPagerViewId);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setOnPageChangeListener(viewPagerOnPageListener);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(getString(R.string.app_name));
        (new GetEventsAsyncTask(getContext(), null, getEventsListListener, 1)).execute();

        if (autoUpdateTimer == null)
            autoUpdateTimer = new PeriodicalTimer(AUTO_REFRESH_DELAY, autoUpdateTimerTask);
        reloadScores();
        autoUpdateTimer.start();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_ACTION_RELOAD_SCORES);
        intentFilter.addAction(Constants.BROADCAST_ACTION_REDRAW_SCORES);
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && searchView != null)
            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

        LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcastReceiver);

        autoUpdateTimer.stop();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<Object> newList = new ArrayList<>();

//        for (Event event : events) {
//            if (event.getFirstTeamName().toLowerCase().contains(userInput) || event.getSecondTeamName().toLowerCase().contains(userInput)) {
//                newList.add(event);
//            }
//        }

        View currentView = views.get(currentEventsList);
        RecyclerView recyclerViewEventsList = currentView.findViewById(R.id.recyclerViewEventsList);
        EventsListAdapter eventsListAdapter = (EventsListAdapter) recyclerViewEventsList.getAdapter();
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
        searchView = (SearchView) searchMenuItem.getActionView();
        searchMenuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(activity.getResources().getString(R.string.string_search));
        searchView.setOnSearchClickListener(v -> enableSearchMode(true));
        searchView.setOnCloseListener(() -> {
            enableSearchMode(false);
            return false;
        });

        refreshMenuItem = menu.findItem(R.id.action_refresh);
        filterMenuItem = menu.findItem(R.id.action_filter);

        setOptionsMenuVisibilityAndStyle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        if (itemId == filterMenuItem.getItemId()) {
            FilterDialog.createDialog(activity, activity.getLayoutInflater(), model).show();
            return true;
        } else if (itemId == refreshMenuItem.getItemId()) {
            reloadScores();
            return true;
        }
        return false;
    }

    private void swipeRefreshLayout(View currentView) {
        SwipeRefreshLayout swipeRefreshLayout = currentView.findViewById(R.id.swipeRefreshLayoutEventsList);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            Intent reloadScoresIntent = new Intent(Constants.BROADCAST_ACTION_RELOAD_SCORES);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(reloadScoresIntent);
        });
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
            (new GetEventsAsyncTask(getContext(), parseScoreCompleteHandler, getEventsListListener, currentEventsList)).execute();
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

    private void setDivider(RecyclerView recyclerView) {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void enableSearchMode(boolean enable) {
        boolean visibility = !enable;
        refreshMenuItem.setVisible(visibility);
        filterMenuItem.setVisible(visibility);
    }

    private void setOptionsMenuVisibilityAndStyle() {
        if (filterMenuItem == null) //this should be the last added menu item that is going to be edit below!!!
            return;

        if (model.onlyLiveGamesFilterEnabled)
            filterMenuItem.setIcon(R.drawable.icon_actionbar_funnel_on);
        else
            filterMenuItem.setIcon(R.drawable.icon_actionbar_funnel_off);
    }

    private void setListAdapter(RecyclerView recyclerView) {
        EventsListAdapter eventsListAdapter = new EventsListAdapter(activity, getContext());
        recyclerView.setAdapter(eventsListAdapter);
    }

    private void initToolbar() {
        toolbar = rootView.findViewById(R.id.toolbarEventsList);

        ((AppCompatActivity) activity).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> activity.onBackPressed());

        toolbar.setTitle("");
        toolbar.setSubtitle("");
    }

    private ViewPager.SimpleOnPageChangeListener viewPagerOnPageListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            if (!isAdded())
                return;

            (new GetEventsAsyncTask(getContext(), null, getEventsListListener, position)).execute();
        }
    };

    private List<Object> buildEventsList(EventsListItem event) {
        if (eventsListDataSet == null)
            eventsListDataSet = new ArrayList<>();
        else
            eventsListDataSet.clear();

        for (EventsListItem.EventsListLeagues eventsListLeagues : event.leagues) {
            lastEventsListLeague = null;

            for (EventsListItem.EventsListLeague eventsListLeague : eventsListLeagues.leagues) {
                if (shouldLeagueBeAdded(eventsListLeague) && !model.onlyLiveGamesFilterEnabled)
                    eventsListDataSet.add(new ListItemLeague(eventsListLeague));

                lastEventsListLeague = eventsListLeague;
                Collections.sort(eventsListLeague.events, new EventsListItem.TimeOrderComparator());

                if (model.onlyLiveGamesFilterEnabled) {
                    for (EventsListItem.EventsListEvent eventsListEvent : eventsListLeague.events) {
                        if (eventsListEvent.event.running)
                            eventsListDataSet.add(new ListItemEvent(eventsListEvent));
                    }
                } else {
                    for (EventsListItem.EventsListEvent eventsListEvent : eventsListLeague.events) {
                        eventsListDataSet.add(new ListItemEvent(eventsListEvent));
                    }
                }
            }
        }

        return eventsListDataSet;
    }

    private boolean shouldLeagueBeAdded(EventsListItem.EventsListLeague eventsListLeague) {
        return lastEventsListLeague == null || !(lastEventsListLeague.league.id.equals(eventsListLeague.league.id)
                && lastEventsListLeague.league.name.equals(eventsListLeague.league.name)
                && lastEventsListLeague.league.country.equals(eventsListLeague.league.country));
    }

    public static class ListItemLeague {
        public final League league;

        ListItemLeague(EventsListItem.EventsListLeague eventsListLeague) {
            league = eventsListLeague.league;
        }
    }

    public static class ListItemEvent {
        public final EventsListItem.EventsListEvent eventsListEvent;

        ListItemEvent(EventsListItem.EventsListEvent predictionEvent) {
            this.eventsListEvent = predictionEvent;
        }
    }

    private OnCompleteListener getEventsListListener = new OnCompleteListener() {
        @Override
        public void onSuccess(EventsListItem event, int position) {
            if (!isAdded())
                return;

            tabPageIndicator.notifyDataSetChanged();
            lastEventsListLeague = null;

            View currentView = views.get(position);
            currentView.findViewById(R.id.progressBarLayoutEventsList).setVisibility(View.GONE);

            RecyclerView recyclerViewEventsList = currentView.findViewById(R.id.recyclerViewEventsList);
            View notificationNoDataEventsList = currentView.findViewById(R.id.notificationNoDataEventsList);
            View notificationNoMatchesFilter = currentView.findViewById(R.id.notificationNoMatchesFilter);

            if (event == null || event.leagues.get(0).leagues.isEmpty()) {
                recyclerViewEventsList.setVisibility(View.GONE);
                notificationNoDataEventsList.setVisibility(View.VISIBLE);
            } else {
                EventsListAdapter eventsListAdapter = (EventsListAdapter) recyclerViewEventsList.getAdapter();

                if (model.onlyLiveGamesFilterEnabled) {
                    Objects.requireNonNull(eventsListAdapter).setData(buildEventsList(event));
                    if (buildEventsList(event).isEmpty()) {
                        recyclerViewEventsList.setVisibility(View.GONE);
                        notificationNoDataEventsList.setVisibility(View.GONE);
                        notificationNoMatchesFilter.setVisibility(View.VISIBLE);
                    } else {
                        recyclerViewEventsList.setVisibility(View.VISIBLE);
                        notificationNoDataEventsList.setVisibility(View.GONE);
                        notificationNoMatchesFilter.setVisibility(View.GONE);
                    }
                } else {
                    Objects.requireNonNull(eventsListAdapter).setData(buildEventsList(event));
                    recyclerViewEventsList.setVisibility(View.VISIBLE);
                    notificationNoDataEventsList.setVisibility(View.GONE);
                    notificationNoMatchesFilter.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onError(int position) {
            if (!isAdded())
                return;

            View currentView = views.get(position);
            currentView.findViewById(R.id.progressBarLayoutEventsList).setVisibility(View.GONE);
            currentView.findViewById(R.id.recyclerViewEventsList).setVisibility(View.GONE);
            currentView.findViewById(R.id.notificationNoDataEventsList).setVisibility(View.VISIBLE);
        }
    };

    @SuppressWarnings("unchecked")
    private static class ParseScoreCompleteHandler extends Handler {
        private final WeakReference<EventsListFragment> parentWeakRef;

        ParseScoreCompleteHandler(EventsListFragment parent) {
            this.parentWeakRef = new WeakReference<>(parent);
        }

        @Override
        public void handleMessage(Message msg) {
            final EventsListFragment parent = parentWeakRef.get();
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
            if (!URLContentHelper.isConnectedToNetwork(parent.activity)) {
                Handler handler = new Handler();
                handler.postDelayed(() -> parent.setRefreshMenuItemLoading(false), 500);
            } else {
                parent.setRefreshMenuItemLoading(false);
                parent.setOptionsMenuVisibilityAndStyle();
            }
        }
    }
}
