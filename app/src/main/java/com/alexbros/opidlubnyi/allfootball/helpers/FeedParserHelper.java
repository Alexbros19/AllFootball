package com.alexbros.opidlubnyi.allfootball.helpers;

import android.content.Context;

import com.alexbros.opidlubnyi.allfootball.models.CampaignOdd;
import com.alexbros.opidlubnyi.allfootball.models.Event;
import com.alexbros.opidlubnyi.allfootball.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_ABANDONED;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_AFTER_EXTRA_TIME;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_AFTER_PENALTIES;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_BREAK;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_DELAYED;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_EXTRA_TIME;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_FIRST_HALF;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_HALF_TIME;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_INTERRUPTED;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_PENALTIES;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_POSTPONED;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_SECOND_HALF;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_SUSPENDED;

public class FeedParserHelper {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");

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

    public static String getEventStatusText(Context context, Event event) {
        final long statusId = event.getStatusId();
        final boolean hasTimeInfo = event.getMinute().equals("0");

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
            if (event.isRunning())
                status = event.getFormattedMinute();
            else if (event.isUpcoming())
                status = DateHelper.getFormattedTime(context, event.getUtcStartTime());
            else if (event.isFinished())
                status = context.getString(R.string.string_match_status_full_time);
            else if (event.isCancelled())
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

    public static CampaignOdd parseOdds(String currentLine, String oddFormat) {
        String[] data = SPLIT_PATTERN.split(currentLine);

        CampaignOdd campaignOdd = new CampaignOdd();
        campaignOdd.publisherId = data[1];
        campaignOdd.publisher = data[2];
        campaignOdd.publisherImageName = data[3];
        campaignOdd.timestamp = Long.parseLong(data[4]);
        campaignOdd.prioritization = Integer.parseInt(data[5]);
        campaignOdd.oddType = Integer.parseInt(data[6]);
        campaignOdd.odd1 = Float.parseFloat(data[7]);
        campaignOdd.onClickUrl1 = data[8];
        campaignOdd.odd1String = DecimalHelper.getFormattedOdd(campaignOdd.odd1, oddFormat);
        campaignOdd.odd2 = Float.parseFloat(data[9]);
        campaignOdd.onClickUrl2 = data[10];
        campaignOdd.odd2String = DecimalHelper.getFormattedOdd(campaignOdd.odd2, oddFormat);
        campaignOdd.oddX = Float.parseFloat(data[11]);
        campaignOdd.onClickUrlX = data[12];
        campaignOdd.oddXString = DecimalHelper.getFormattedOdd(campaignOdd.oddX, oddFormat);

//        campaignOdd.odd1XFloat = Float.parseFloat(data[13]);
//        campaignOdd.odd1XString = DecimalHelper.getFormattedOdd(campaignOdd.odd1XFloat, oddFormat);
//        campaignOdd.odd1XURL = data[14];
//
//        campaignOdd.odd2XFloat = Float.parseFloat(data[15]);
//        campaignOdd.odd2XString = DecimalHelper.getFormattedOdd(campaignOdd.odd2XFloat, oddFormat);
//        campaignOdd.odd2XURL = data[16];
//
//        campaignOdd.oddOver15Float = Float.parseFloat(data[17]);
//        campaignOdd.oddOver15String = DecimalHelper.getFormattedOdd(campaignOdd.oddOver15Float, oddFormat);
//        campaignOdd.oddOver15URL = data[18];
//
//        campaignOdd.oddUnder15Float = Float.parseFloat(data[19]);
//        campaignOdd.oddUnder15String = DecimalHelper.getFormattedOdd(campaignOdd.oddUnder15Float, oddFormat);
//        campaignOdd.oddUnder15URL = data[20];
//
//        campaignOdd.oddOver25Float = Float.parseFloat(data[21]);
//        campaignOdd.oddOver25String = DecimalHelper.getFormattedOdd(campaignOdd.oddOver25Float, oddFormat);
//        campaignOdd.oddOver25URL = data[22];
//
//        campaignOdd.oddUnder25Float = Float.parseFloat(data[23]);
//        campaignOdd.oddUnder25String = DecimalHelper.getFormattedOdd(campaignOdd.oddUnder25Float, oddFormat);
//        campaignOdd.oddUnder25URL = data[24];
//
//        campaignOdd.oddOver35Float = Float.parseFloat(data[25]);
//        campaignOdd.oddOver35String = DecimalHelper.getFormattedOdd(campaignOdd.oddOver35Float, oddFormat);
//        campaignOdd.oddOver35URL = data[26];
//
//        campaignOdd.oddUnder35Float = Float.parseFloat(data[27]);
//        campaignOdd.oddUnder35String = DecimalHelper.getFormattedOdd(campaignOdd.oddUnder35Float, oddFormat);
//        campaignOdd.oddUnder35URL = data[28];
//
//        campaignOdd.oddYesFloat = Float.parseFloat(data[29]);
//        campaignOdd.oddYesString = DecimalHelper.getFormattedOdd(campaignOdd.oddYesFloat, oddFormat);
//        campaignOdd.oddYesURL = data[30];
//
//        campaignOdd.oddNoFloat = Float.parseFloat(data[31]);
//        campaignOdd.oddNoString = DecimalHelper.getFormattedOdd(campaignOdd.oddNoFloat, oddFormat);
//        if (data.length > 32)
//            campaignOdd.oddNoURL = data[32];

        return campaignOdd;
    }

    public static CampaignOdd parseCampaignOddJson(JSONObject campaignOddJsonObject, String oddFormat) throws JSONException {
        if (campaignOddJsonObject == null)
            return null;

        CampaignOdd campaignOdd = new CampaignOdd();

        if (campaignOddJsonObject.has("odds") && !campaignOddJsonObject.isNull("odds")) {
            JSONObject oddsJsonObject = campaignOddJsonObject.getJSONObject("odds");

            campaignOdd.oddType = Integer.parseInt(oddsJsonObject.optString("type", "0"));

            campaignOdd.odd1 = (float) oddsJsonObject.optDouble("one");
            campaignOdd.odd1String = DecimalHelper.getFormattedOdd(campaignOdd.odd1, oddFormat);
            campaignOdd.odd2 = (float) oddsJsonObject.optDouble("two");
            campaignOdd.odd2String = DecimalHelper.getFormattedOdd(campaignOdd.odd2, oddFormat);
            campaignOdd.oddX = (float) oddsJsonObject.optDouble("x");
            campaignOdd.oddXString = DecimalHelper.getFormattedOdd(campaignOdd.oddX, oddFormat);
        }

        campaignOdd.type = getStringValueOrEmpty(campaignOddJsonObject, "type");
        campaignOdd.isLive = campaignOdd.type.equals("LIVEODD");
        campaignOdd.publisherId = getStringValueOrEmpty(campaignOddJsonObject, "publisherId");
        campaignOdd.onClickUrl1 = getStringValueOrEmpty(campaignOddJsonObject, "linkOne");
        campaignOdd.onClickUrl2 = getStringValueOrEmpty(campaignOddJsonObject, "linkTwo");
        campaignOdd.onClickUrlX = getStringValueOrEmpty(campaignOddJsonObject, "linkX");

        return campaignOdd;
    }
}
