package com.alexbros.opidlubnyi.allfootball;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.alexbros.opidlubnyi.allfootball.helpers.FeedParserHelper;
import com.alexbros.opidlubnyi.allfootball.helpers.UrlHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class GetEventsAsyncTask extends AsyncTask<String, Void, List<ListElement>> {
    private OnCompleteListener onCompleteListener;
    private int eventsListPagePosition;
    private Handler endHandler;

    public interface OnCompleteListener {
        void onSuccess(List<ListElement> list, int position);
        void onError(int position);
    }

    GetEventsAsyncTask(Handler endHandler, OnCompleteListener onCompleteListener, int eventsListPagePosition) {
        this.endHandler = endHandler;
        this.onCompleteListener = onCompleteListener;
        this.eventsListPagePosition = eventsListPagePosition;
    }

    @Override
    protected List<ListElement> doInBackground(String... strings) {
        List<ListElement> list = new ArrayList<>();

        String result;
        String urlString = strings[0];
        UrlHelper httpDataHandler = new UrlHelper();
        result = httpDataHandler.getHttpData(urlString);
        ListElement listElement;

        if(result != null){
            try{
                JSONObject rootObject = new JSONObject(result);
                JSONObject response = rootObject.getJSONObject("response");
                JSONObject items = response.getJSONObject("items");
                JSONObject Overview = items.getJSONObject("Overview");
                JSONArray leagues = Overview.getJSONArray("leagues");

                for(int i = 0; i < leagues.length(); i++) {
                    JSONObject leaguesJSONObject = leagues.getJSONObject(i);
                    JSONArray events = leaguesJSONObject.getJSONArray("events");

                    for(int j = 0; j < events.length(); j++) {
                        JSONObject eventsJSONObject = events.getJSONObject(j);
                        JSONArray participants = eventsJSONObject.getJSONArray("participants");
                        listElement = new ListElement();

                        listElement.setStatusId(eventsJSONObject.getLong("statusId"));
                        listElement.running = listElement.isRunning();
                        listElement.finished = listElement.isFinished();
                        listElement.upcoming = listElement.isUpcoming();
                        listElement.setMinute(eventsJSONObject.getString("minute"));
                        listElement.setUtcStartTime(eventsJSONObject.getLong("utcStartTime"));

                        FeedParserHelper.EventParticipant participant;

                        participant = FeedParserHelper.parseEventParticipantJson(participants.getJSONObject(0));
                        listElement.setFirstTeamName(participant.name);
                        listElement.setTeamOneId(participant.id);
                        listElement.setTeamOneGoals(participant.goals);
                        listElement.setTeamOneGoalsString(participant.goalsString);

                        participant = FeedParserHelper.parseEventParticipantJson(participants.getJSONObject(1));
                        listElement.setSecondTeamName(participant.name);
                        listElement.setTeamTwoId(participant.id);
                        listElement.setTeamTwoGoals(participant.goals);
                        listElement.setTeamTwoGoalsString(participant.goalsString);

                        list.add(listElement);
                    }
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }

//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return list;
    }

    private int checkEventIsRunning(List<ListElement> list) {
        for (ListElement event : list) {
            if (event.isRunning()) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    protected void onPostExecute(List<ListElement> list) {
        if (onCompleteListener != null && list != null && list.size() != 0) {
            onCompleteListener.onSuccess(list, eventsListPagePosition);
            if (endHandler != null) {
                Message msg = new Message();
                msg.arg2 = checkEventIsRunning(list);
                msg.what = Constants.RESULT_OK;
                endHandler.sendMessage(msg);
            }
        } else {
            if (onCompleteListener != null) {
                onCompleteListener.onError(eventsListPagePosition);
                if (endHandler != null) {
                    Message msg = new Message();
                    msg.what = Constants.RESULT_EXCEPTION;
                    endHandler.sendMessage(msg);
                }
            }
        }
    }
}
