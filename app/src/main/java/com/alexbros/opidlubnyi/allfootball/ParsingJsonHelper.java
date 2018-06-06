package com.alexbros.opidlubnyi.allfootball;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ParsingJsonHelper extends AsyncTask<String, Void, List<ListElement>> {

    private OnDataListener onDataListener;

    interface OnDataListener {
        void onReceived(List<ListElement> list);
    }

    public ParsingJsonHelper(OnDataListener onDataListener) {
        this.onDataListener = onDataListener;
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
                        for(int k = 0; k < participants.length(); k++) {
                            JSONObject participantsJSONObject = participants.getJSONObject(k);
                            if(k == 0)
                                listElement.setFirstTeamName(participantsJSONObject.getString("name"));
                            else
                                listElement.setSecondTeamName(participantsJSONObject.getString("name"));
                        }
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

    @Override
    protected void onPostExecute(List<ListElement> list) {
        onDataListener.onReceived(list);
    }
}
