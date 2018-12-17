package com.alexbros.opidlubnyi.allfootball.async_tasks;

import android.os.AsyncTask;

import com.alexbros.opidlubnyi.allfootball.config.AppConfig;
import com.alexbros.opidlubnyi.allfootball.helpers.URLContentHelper;
import com.alexbros.opidlubnyi.allfootball.models.ModelData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class GetConfigAsyncTask extends AsyncTask<Void, Void, GetConfigAsyncTask.Result> {
    private ModelData model;
    private OnCompleteListener listener;
    private String configURL;

    public GetConfigAsyncTask(String configURL, OnCompleteListener listener) {
        this.model = ModelData.getInstance();
        this.listener = listener;
        this.configURL = configURL;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Result doInBackground(Void... params) {
        Result result = new Result();

        try {
            String json = URLContentHelper.getConfigResponse(configURL, model);
            result.appConfig = parse(json);
            result.success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (listener != null) {
            if (result.success)
                listener.onSuccess(result.appConfig);
            else
                listener.onError();
        }

    }

    private AppConfig parse(String json) throws JSONException {
        AppConfig appConfig = new AppConfig();

        JSONObject root = new JSONObject(json);

        parse(root, appConfig);
        parse(root, appConfig.maintenance);
        parse(root, appConfig.features);
        parse(root, appConfig.trackers);

        return appConfig;
    }

    private void parse(JSONObject root, AppConfig appConfig) {
        JSONObject node;

        try {
            node = root.getJSONObject("server").getJSONObject("hosts");
        } catch (JSONException e) {
            return;
        }

        try {
            appConfig.baseApiURL = node.getString("apiUrl");
            appConfig.baseApiURL = appConfig.baseApiURL.replaceAll("/*$", "") + "/";
        } catch (JSONException ignored) {
        }

        try {
            appConfig.baseMediaURL = node.getString("mediaUrl");
            appConfig.baseMediaURL = appConfig.baseMediaURL.replaceAll("/*$", "") + "/";
        } catch (JSONException ignored) {
        }

        try {
            appConfig.baseUserPictureURL = node.getString("userPictureUrl");
            appConfig.baseUserPictureURL = appConfig.baseUserPictureURL.replaceAll("/*$", "") + "/";
        } catch (JSONException ignored) {
        }

//        try {
//            appConfig.updateURL = root.getJSONObject("application").getString("updateURL");
//        } catch (JSONException ignored) {
//        }
    }

    private void parse(JSONObject root, AppConfig.Maintenance maintenance) {
        JSONObject node;

        try {
            node = root.getJSONObject("maintenance");
        } catch (JSONException e) {
            return;
        }

        try {
            maintenance.active = node.getBoolean("active");
        } catch (JSONException ignored) {
        }

        try {
            maintenance.message = node.getString("customMessage");
        } catch (JSONException ignored) {
        }
    }

    private void parse(JSONObject root, AppConfig.Features features) {
        JSONObject node;

        try {
            node = root.getJSONObject("features");
        } catch (JSONException e) {
            return;
        }

        parse(node, features.configuration);
        parse(node, features.eventDetails);
        parse(node, features.logos);
        parse(node, features.watchlist);
        parse(node, features.bottomBar);
        parse(node, features.removeAds);
        parse(node, features.videos);
    }

    private void parse(JSONObject root, AppConfig.Features.Configuration configuration) {
        JSONObject node;

        try {
            node = root.getJSONObject("configuration");
        } catch (JSONException e) {
            return;
        }

        try {
            configuration.updatePeriod = node.getInt("updatePeriod");

            if (configuration.updatePeriod < 5)
                configuration.updatePeriod = AppConfig.Features.Configuration.DEFAULT_UPDATE_PERIOD;
        } catch (JSONException ignored) {
        }
    }

    private void parse(JSONObject root, AppConfig.Features.EventDetails eventDetails) {
        JSONObject node;

        try {
            node = root.getJSONObject("eventDetails");
        } catch (JSONException e) {
            return;
        }

        try {
            eventDetails.showChatTab = node.getJSONObject("tabs").getBoolean("chat");
        } catch (JSONException ignored) {
        }

        try {
            eventDetails.showOddsTab = node.getJSONObject("tabs").getBoolean("odds");
        } catch (JSONException ignored) {
        }
    }

    private void parse(JSONObject root, AppConfig.Features.Logos logos) {
        JSONObject node;

        try {
            node = root.getJSONObject("logos");
        } catch (JSONException e) {
            return;
        }

        try {
            logos.getTeamLogos = node.getBoolean("getTeamLogos");
        } catch (JSONException ignored) {
        }

        try {
            logos.getPlayerPics = node.getBoolean("getPlayerPics");
        } catch (JSONException ignored) {
        }

        try {
            logos.getUserPics = node.getBoolean("getUserPics");
        } catch (JSONException ignored) {
        }
    }

    private void parse(JSONObject root, AppConfig.Features.Watchlist watchlist) {
        JSONObject node;

        try {
            node = root.getJSONObject("watchList");
        } catch (JSONException e) {
            return;
        }

        try {
            watchlist.updatePeriod = node.getInt("updatePeriod");

            if (watchlist.updatePeriod < 5)
                watchlist.updatePeriod = AppConfig.Features.Watchlist.DEFAULT_UPDATE_PERIOD;
        } catch (JSONException ignored) {
        }

        try {
            watchlist.addTeamsByDefault = node.getBoolean("addTeamsByDefault");
        } catch (JSONException ignored) {
        }

        try {
            watchlist.addLeaguesByDefault = node.getBoolean("addLeaguesByDefault");
        } catch (JSONException ignored) {
        }
    }

    private void parse(JSONObject root, AppConfig.Features.BottomBar bottomBar) {
        JSONObject node;

        try {
            node = root.getJSONObject("bottomBar");
        } catch (JSONException e) {
            return;
        }

        try {
            bottomBar.showTip = node.getBoolean("showTip");
        } catch (JSONException ignored) {
        }

        try {
            bottomBar.installByDefault = (float) node.getDouble("installByDefault");

            if ((bottomBar.installByDefault < 0) || (bottomBar.installByDefault > 1.0))
                bottomBar.installByDefault = 0;
        } catch (JSONException ignored) {
        }
    }

    private void parse(JSONObject root, AppConfig.Features.RemoveAds removeAds) {
        JSONObject node;

        try {
            node = root.getJSONObject("removeAds");
        } catch (JSONException e) {
            return;
        }

        try {
            removeAds.enabled = node.getBoolean("enabled");
        } catch (JSONException ignored) {
        }
    }

    private void parse(JSONObject root, AppConfig.Features.Videos videos) {
        JSONObject node;

        try {
            node = root.getJSONObject("videos");
        } catch (JSONException e) {
            return;
        }

        try {
            JSONObject eventVideos = node.getJSONObject("eventVideos");

            videos.eventVideos.openInExternalApp = eventVideos.getBoolean("openInExternalApp");
        } catch (JSONException ignored) {
        }
    }

    private void parse(JSONObject root, AppConfig.Trackers trackers) {
        JSONObject node;

        try {
            node = root.getJSONObject("trackers");
        } catch (JSONException e) {
            return;
        }

        parse(node, trackers.googleAnalytics);
        parse(node, trackers.firebaseAnalytics);
    }

    private void parse(JSONObject root, AppConfig.Trackers.GoogleAnalytics googleAnalytics) {
        JSONObject node;

        try {
            node = root.getJSONObject("googleAnalytics");
        } catch (JSONException e) {
            return;
        }

        try {
            googleAnalytics.minVersion = node.getString("minVersion");
        } catch (JSONException ignored) {
        }

        try {
            googleAnalytics.throttleTo = node.getInt("throttleTo");
        } catch (JSONException ignored) {
        }

        try {
            JSONArray categoriesJsonArray = node.getJSONArray("disabledCategories");

            for (int i = 0; i < categoriesJsonArray.length(); i++) {
                JSONObject pair = categoriesJsonArray.getJSONObject(i);

                Iterator<String> iterator = pair.keys();
                if (iterator.hasNext()) {
                    final String categoryName = iterator.next();

                    AppConfig.Trackers.GoogleAnalytics.DisabledCategory disabledCategory = new AppConfig.Trackers.GoogleAnalytics.DisabledCategory();

                    JSONArray actions = pair.getJSONArray(categoryName);

                    for (int y = 0; y < actions.length(); y++) {
                        final String action = actions.getString(y);
                        disabledCategory.actions.add(action.toLowerCase());
                    }

                    googleAnalytics.disabledCategories.put(categoryName.toLowerCase(), disabledCategory);
                }
            }
        } catch (JSONException ignored) {
        }
    }

    private void parse(JSONObject root, AppConfig.Trackers.FirebaseAnalytics firebaseAnalytics) {
        JSONObject node;

        try {
            node = root.getJSONObject("firebaseAnalytics");
        } catch (JSONException e) {
            return;
        }

        try {
            firebaseAnalytics.minVersion = node.getString("minVersion");
        } catch (JSONException ignored) {
        }

        firebaseAnalytics.enabled = true;

//        if (!model.versionName.isEmpty() && !firebaseAnalytics.minVersion.isEmpty()) {
//            if (VersionHelper.isVersionLess(model.versionName, firebaseAnalytics.minVersion))
//                firebaseAnalytics.enabled = false;
//        }
    }

    public interface OnCompleteListener {
        void onSuccess(AppConfig newConfig);

        void onError();
    }

    static class Result {
        boolean success = false;
        AppConfig appConfig = null;
    }
}
