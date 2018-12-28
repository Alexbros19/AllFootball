package com.alexbros.opidlubnyi.allfootball.threads;

import android.os.Handler;
import android.os.Message;

import com.alexbros.opidlubnyi.allfootball.URLConstants;
import com.alexbros.opidlubnyi.allfootball.helpers.FeedParserHelper;
import com.alexbros.opidlubnyi.allfootball.helpers.URLContentHelper;
import com.alexbros.opidlubnyi.allfootball.models.CampaignOdd;
import com.alexbros.opidlubnyi.allfootball.models.ModelData;

import org.json.JSONObject;

public class GetEventOddsThread extends Thread {
    private Handler handler;
    private String eventId;

    public GetEventOddsThread(Handler handler, String eventId) {
        this.handler = handler;
        this.eventId = eventId;
    }

    @Override
    public void run() {
        try {
            ModelData model = ModelData.getInstance();

            String json = URLContentHelper.getEventDetailResponse(model, eventId);

            JSONObject root = new JSONObject(json).getJSONObject("response").getJSONObject("items");

            JSONObject eventDetailsRoot = root.getJSONObject("EventDetails");

            CampaignOdd campaign = null;

            if (eventDetailsRoot.has("campaign") && !eventDetailsRoot.isNull("campaign"))
                campaign = FeedParserHelper.parseCampaignOddJson(eventDetailsRoot.getJSONObject("campaign"), model.oddFormat);

            if (handler != null) {
                Message msg = new Message();
                msg.what = URLConstants.RESULT_OK;
                msg.obj = campaign;
                handler.sendMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (handler != null)
                handler.sendEmptyMessage(URLConstants.RESULT_EXCEPTION);
        }
    }
}
