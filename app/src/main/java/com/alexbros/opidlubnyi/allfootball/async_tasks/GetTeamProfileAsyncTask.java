package com.alexbros.opidlubnyi.allfootball.async_tasks;

import android.os.AsyncTask;

import com.alexbros.opidlubnyi.allfootball.models.TeamProfile;
import com.alexbros.opidlubnyi.allfootball.helpers.FeedParserHelper;
import com.alexbros.opidlubnyi.allfootball.helpers.URLContentHelper;

import org.json.JSONObject;

import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.FACEBOOK;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.INSTAGRAM;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.TWITTER;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.VIBER;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.WEB_URL;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.WIKIPEDIA;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.YOUTUBE;

public class GetTeamProfileAsyncTask extends AsyncTask<Void, Void, TeamProfile> {
    private OnCompleteListener onCompleteListener;
    private final String id;

    public interface OnCompleteListener {
        void onSuccess(TeamProfile teamProfile);

        void onError();
    }

    public GetTeamProfileAsyncTask(OnCompleteListener onCompleteListener, String id) {
        this.onCompleteListener = onCompleteListener;
        this.id = id;
    }

    @Override
    protected TeamProfile doInBackground(Void... voids) {
        try {
            String json = URLContentHelper.getTeamProfileResponse(id);
            JSONObject root = new JSONObject(json).getJSONObject("response").getJSONObject("items").getJSONObject("TeamProfile");

            TeamProfile teamProfile = new TeamProfile();

            if (root.has("GeneralInfo") && !root.isNull("GeneralInfo")) {
                JSONObject generalInfo = root.getJSONObject("GeneralInfo");

                teamProfile.name = FeedParserHelper.getStringValueOrEmpty(generalInfo, "teamName");
            }

            if (root.has("Social") && !root.isNull("Social")) {
                JSONObject social = root.getJSONObject("Social");

                teamProfile.channels.put(WEB_URL.getChannelString(), FeedParserHelper.getStringValueOrEmpty(social, "web"));
                teamProfile.channels.put(WIKIPEDIA.getChannelString(), FeedParserHelper.getStringValueOrEmpty(social, "wikipediaId"));
                teamProfile.channels.put(FACEBOOK.getChannelString(), FeedParserHelper.getStringValueOrEmpty(social, "facebookId"));
                teamProfile.channels.put(TWITTER.getChannelString(), FeedParserHelper.getStringValueOrEmpty(social, "twitterId"));
                teamProfile.channels.put(YOUTUBE.getChannelString(), FeedParserHelper.getStringValueOrEmpty(social, "youTubeChannelId"));
                teamProfile.channels.put(VIBER.getChannelString(), FeedParserHelper.getStringValueOrEmpty(social, "viberId"));
                teamProfile.channels.put(INSTAGRAM.getChannelString(), FeedParserHelper.getStringValueOrEmpty(social, "instagramId"));
            }

            return teamProfile;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(TeamProfile teamProfile) {
        if (onCompleteListener == null)
            return;

        if (teamProfile == null)
            onCompleteListener.onError();
        else
            onCompleteListener.onSuccess(teamProfile);
    }
}
