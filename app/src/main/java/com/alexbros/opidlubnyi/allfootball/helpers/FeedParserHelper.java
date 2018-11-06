package com.alexbros.opidlubnyi.allfootball.helpers;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedParserHelper {
    public static FeedParserHelper.EventParticipant parseEventParticipantJson(JSONObject participantJsonObject) throws JSONException {
        FeedParserHelper.EventParticipant participant = new FeedParserHelper.EventParticipant();

        participant.id = getStringValueOrEmpty(participantJsonObject, "id");
        participant.name = getStringValueOrEmpty(participantJsonObject, "name");

        if (participantJsonObject.has("goals") && !participantJsonObject.isNull("goals")) {
            participant.goals = participantJsonObject.optInt("goals");

            if (participantJsonObject.has("goalString") && !participantJsonObject.isNull("goalString"))
                participant.goalsString = participantJsonObject.optString("goalString", "-");
            else
                participant.goalsString = String.valueOf(participant.goals);
        } else {
            participant.goals = 0;
            if (participantJsonObject.has("goalString") && !participantJsonObject.isNull("goalString"))
                participant.goalsString = participantJsonObject.optString("goalString", "-");
            else
                participant.goalsString = "-";
        }

        return participant;
    }

    public static Integer getIntegerValueOrNull(JSONObject jsonObject, String name) throws JSONException {
        return jsonObject.isNull(name) ? null : jsonObject.getInt(name);
    }

    public static String getStringValueOrEmpty(JSONObject jsonObject, String name) throws JSONException {
        return jsonObject.isNull(name) ? "" : jsonObject.optString(name, "");
    }

    public static class EventParticipant {
        public String id = "";
        public String name = "";
        public int goals = 0;
        public String goalsString = "";
    }
}
