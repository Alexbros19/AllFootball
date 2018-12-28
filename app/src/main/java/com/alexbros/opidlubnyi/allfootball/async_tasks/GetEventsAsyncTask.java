package com.alexbros.opidlubnyi.allfootball.async_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alexbros.opidlubnyi.allfootball.Constants;
import com.alexbros.opidlubnyi.allfootball.helpers.CountryNameHelper;
import com.alexbros.opidlubnyi.allfootball.helpers.FeedParserHelper;
import com.alexbros.opidlubnyi.allfootball.helpers.URLContentHelper;
import com.alexbros.opidlubnyi.allfootball.models.Event;
import com.alexbros.opidlubnyi.allfootball.models.EventsListItem;
import com.alexbros.opidlubnyi.allfootball.models.League;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetEventsAsyncTask extends AsyncTask<Void, Void, EventsListItem> {
    private OnCompleteListener onCompleteListener;
    private int eventsListPagePosition;
    private Handler endHandler;
    private Context context;


    public interface OnCompleteListener {
        void onSuccess(EventsListItem list, int position);
        void onError(int position);
    }

    public GetEventsAsyncTask(Context context, Handler endHandler, OnCompleteListener onCompleteListener, int eventsListPagePosition) {
        this.context = context;
        this.endHandler = endHandler;
        this.onCompleteListener = onCompleteListener;
        this.eventsListPagePosition = eventsListPagePosition - 1; // set response of events list using days page position
    }

    private EventsListItem parse(JSONObject root) throws JSONException {
        JSONObject eventsListItemJsonObject = root.getJSONObject("Overview");

        EventsListItem eventsListEvent = new EventsListItem();

        EventsListItem.EventsListLeagues eventsListLeagues = new EventsListItem.EventsListLeagues();

        eventsListLeagues.leagues = parseLeagues(eventsListItemJsonObject);

        eventsListEvent.leagues.add(eventsListLeagues);

        return eventsListEvent;
    }

    private List<EventsListItem.EventsListLeague> parseLeagues(JSONObject rootObject) throws JSONException {
        List<EventsListItem.EventsListLeague> eventsListLeagues = new ArrayList<>();

        if (rootObject.has("leagues") && !rootObject.isNull("leagues")) {
            JSONArray leaguesJsonArray = rootObject.getJSONArray("leagues");

            for (int i = 0; i < leaguesJsonArray.length(); i++) {
                JSONObject leaguesJsonObject = leaguesJsonArray.getJSONObject(i);

                EventsListItem.EventsListLeague eventsListLeague = new EventsListItem.EventsListLeague();
                    eventsListLeague.league = new League();
                    eventsListLeague.league.id = leaguesJsonObject.getString("id");
                    eventsListLeague.league.name = FeedParserHelper.getStringValueOrEmpty(leaguesJsonObject, "name");
                    eventsListLeague.league.country = FeedParserHelper.getStringValueOrEmpty(leaguesJsonObject, "country");
                    eventsListLeague.league.countryLocalized = CountryNameHelper.get(eventsListLeague.league.country, context);
                    eventsListLeague.league.playoffType = leaguesJsonObject.optInt("playoffId");
                    eventsListLeague.league.noLive = leaguesJsonObject.optBoolean("noLive");

                    eventsListLeague.events = parseEvents(leaguesJsonObject);

                    eventsListLeagues.add(eventsListLeague);
            }
        }

        return eventsListLeagues;
    }

    private List<EventsListItem.EventsListEvent> parseEvents(JSONObject root) throws JSONException {
        List<EventsListItem.EventsListEvent> eventsListEvents = new ArrayList<>();

        if (root.has("events") && !root.isNull("events")) {
            JSONArray eventsJsonArray = root.getJSONArray("events");

            for (int i = 0; i < eventsJsonArray.length(); i++) {
                EventsListItem.EventsListEvent eventsListEvent = new EventsListItem.EventsListEvent();
                JSONObject eventsJsonObject = eventsJsonArray.getJSONObject(i);
                JSONArray participants = eventsJsonObject.getJSONArray("participants");

                eventsListEvent.event = new Event();
                eventsListEvent.event.setStatusId(FeedParserHelper.getLongValueOrNull(eventsJsonObject, "statusId"));
                eventsListEvent.event.running = eventsListEvent.event.isRunning();
                eventsListEvent.event.finished = eventsListEvent.event.isFinished();
                eventsListEvent.event.upcoming = eventsListEvent.event.isUpcoming();
                eventsListEvent.event.halftime = eventsListEvent.event.isHalftime();
                eventsListEvent.event.firstHalftime = eventsListEvent.event.isFirstHalftime();
                eventsListEvent.event.secondHalftime = eventsListEvent.event.isSecondHalftime();
                eventsListEvent.event.setMinute(FeedParserHelper.getStringValueOrEmpty(eventsJsonObject, "minute"));
                eventsListEvent.event.setUtcStartTime(FeedParserHelper.getLongValueOrNull(eventsJsonObject, "utcStartTime"));
                eventsListEvent.event.setStatus(FeedParserHelper.getEventStatusText(context, eventsListEvent.event));
                eventsListEvent.event.setEventId(FeedParserHelper.getStringValueOrEmpty(eventsJsonObject, "id"));

                FeedParserHelper.EventParticipant participant;

                participant = FeedParserHelper.parseEventParticipantJson(participants.getJSONObject(0));
                eventsListEvent.event.setFirstTeamName(participant.name);
                eventsListEvent.event.setFirstTeamId(participant.id);
                eventsListEvent.event.setFirstTeamGoals(participant.goals);
                eventsListEvent.event.setFirstTeamGoalsString(participant.goalsString);

                participant = FeedParserHelper.parseEventParticipantJson(participants.getJSONObject(1));
                eventsListEvent.event.setSecondTeamName(participant.name);
                eventsListEvent.event.setSecondTeamId(participant.id);
                eventsListEvent.event.setSecondTeamGoals(participant.goals);
                eventsListEvent.event.setSecondTeamGoalsString(participant.goalsString);

                eventsListEvents.add(eventsListEvent);
            }
        }
        return eventsListEvents;
    }

    @Override
    protected EventsListItem doInBackground(Void... voids) {
        EventsListItem eventsListItem;

        try {
            final String json = URLContentHelper.getOverviewResponse(eventsListPagePosition);

            if (!json.isEmpty()) {
                JSONObject root = new JSONObject(json).getJSONObject("response").getJSONObject("items");

                eventsListItem = parse(root);

                return eventsListItem;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private int checkEventIsRunning(EventsListItem list) {
        for (EventsListItem.EventsListLeagues leagues : list.leagues) {
            for (EventsListItem.EventsListLeague league : leagues.leagues) {
                for (EventsListItem.EventsListEvent event : league.events) {
                    if (event.event.isRunning()) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    protected void onPostExecute(EventsListItem list) {
        if (onCompleteListener != null && list != null) {
            onCompleteListener.onSuccess(list, eventsListPagePosition + 1);
            if (endHandler != null) {
                Message msg = new Message();
                msg.arg2 = checkEventIsRunning(list);
                msg.what = Constants.RESULT_OK;
                endHandler.sendMessage(msg);
            }
        } else {
            if (onCompleteListener != null) {
                onCompleteListener.onError(eventsListPagePosition + 1);
                if (endHandler != null) {
                    Message msg = new Message();
                    msg.what = Constants.RESULT_EXCEPTION;
                    endHandler.sendMessage(msg);
                }
            }
        }
    }
}
