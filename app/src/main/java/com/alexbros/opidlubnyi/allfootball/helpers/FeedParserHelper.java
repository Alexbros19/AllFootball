package com.alexbros.opidlubnyi.allfootball.helpers;

import android.content.Context;

import com.alexbros.opidlubnyi.allfootball.ListElement;
import com.alexbros.opidlubnyi.allfootball.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_ABANDONED;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_AFTER_EXTRA_TIME;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_AFTER_PENALTIES;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_BREAK;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_DELAYED;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_EXTRA_TIME;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_FIRST_HALF;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_HALF_TIME;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_INTERRUPTED;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_PENALTIES;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_POSTPONED;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_SECOND_HALF;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_SUSPENDED;

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

    public static Long getLongValueOrNull(JSONObject jsonObject, String name) throws JSONException {
        return jsonObject.isNull(name) ? null : jsonObject.getLong(name);
    }

    public static String getStringValueOrEmpty(JSONObject jsonObject, String name) throws JSONException {
        return jsonObject.isNull(name) ? "" : jsonObject.optString(name, "");
    }

    public static String getEventStatusText(Context context, ListElement listElement) {
        final long statusId = listElement.getStatusId();
        final boolean hasTimeInfo = listElement.getMinute().equals("0");

        String status = "";
        if ((statusId & STATUS_HALF_TIME.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_half_time);
        else if (((statusId & STATUS_FIRST_HALF.getFlag()) != 0) && hasTimeInfo)
            status = context.getString(R.string.string_match_status_1st_half);
        else if (((statusId & STATUS_SECOND_HALF.getFlag()) != 0) && hasTimeInfo)
            status = context.getString(R.string.string_match_status_2nd_half);
        else if (((statusId & STATUS_EXTRA_TIME.getFlag()) != 0) && hasTimeInfo)
            status = context.getString(R.string.string_match_status_extra_time);
        else if ((statusId & STATUS_PENALTIES.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_penalty_shootout);
        else if ((statusId & STATUS_BREAK.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_break);
        else if ((statusId & STATUS_INTERRUPTED.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_interrupted);
        else if ((statusId & STATUS_SUSPENDED.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_suspended);
        else if ((statusId & STATUS_POSTPONED.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_postponed);
        else if ((statusId & STATUS_DELAYED.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_delayed);
        else if ((statusId & STATUS_AFTER_EXTRA_TIME.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_result_after_extra_time);
        else if ((statusId & STATUS_AFTER_PENALTIES.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_result_after_penalty_shootout);
        else if ((statusId & STATUS_ABANDONED.getFlag()) != 0)
            status = context.getString(R.string.string_match_status_abandoned);
        else {
            if (listElement.isRunning())
                status = listElement.getFormattedMinute();
            else if (listElement.isUpcoming())
                status = DateHelper.getFormattedTime(context, listElement.getUtcStartTime());
            else if (listElement.isFinished())
                status = context.getString(R.string.string_match_status_full_time);
            else if (listElement.isCancelled())
                status = context.getString(R.string.string_match_status_cancelled);
        }

        return status;
    }

    public static class EventParticipant {
        public String id = "";
        public String name = "";
        public int goals = 0;
        public String goalsString = "";
    }
}
