package com.alexbros.opidlubnyi.allfootball.threads;

import android.os.Handler;
import android.os.Message;

import com.alexbros.opidlubnyi.allfootball.URLConstants;
import com.alexbros.opidlubnyi.allfootball.helpers.FeedParserHelper;
import com.alexbros.opidlubnyi.allfootball.helpers.URLContentHelper;
import com.alexbros.opidlubnyi.allfootball.models.CampaignOdd;
import com.alexbros.opidlubnyi.allfootball.models.ModelData;

import java.util.ArrayList;

public class GetLiveOddsThread extends Thread {
    private Handler handler;
    private String eventId;
    private String providerId;

    public GetLiveOddsThread(Handler handler, String eventId, String providerId) {
        this.handler = handler;
        this.eventId = eventId;
        this.providerId = providerId;
    }

    public void run() {
        try {
            ModelData modelData = ModelData.getInstance();

            ArrayList<String> lines = URLContentHelper.getLiveOddsResponse(modelData, eventId, providerId);
            int size = lines.size();

            CampaignOdd campaign = null;

            for (String currentLine : lines) {
                if (currentLine.startsWith("O")) {
                    campaign = FeedParserHelper.parseOdds(currentLine, modelData.oddFormat);
                    campaign.isLive = true;
                }
            }

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
