package com.alexbros.opidlubnyi.allfootball.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.alexbros.opidlubnyi.allfootball.Constants;
import com.alexbros.opidlubnyi.allfootball.PeriodicalTimer;
import com.alexbros.opidlubnyi.allfootball.Preferences;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.async_tasks.GetConfigAsyncTask;
import com.alexbros.opidlubnyi.allfootball.helpers.PreferenceHelper;
import com.alexbros.opidlubnyi.allfootball.util.ObfuscatedString;

public class ConfigManager {
    private static final long MILLISECONDS = 1000;
    private static ConfigManager instance;
    private PeriodicalTimer timer = null;
    private String configURL;
    private boolean isFirstStartSession = true;
    private boolean hasConfig = false;
    private AppConfig appConfig = null;

    private ConfigManager() {
    }

    private static String getProductionConfigUrl() {
        return (new ObfuscatedString(new long[]{
                0xE7EAF5AE2DB72587L, 0xF9C5ADBEDEC68E50L, 0xD7C321323DFC79A5L, 0x1D933A7699434A54L,
                0x19FDB4085AAD2D9DL, 0x4625DFAA54442363L, 0x2DF63D467795D9FEL, 0x1D4BAE9B3EC5CEC3L,
                0x545873AC8DF359BBL})).toString();
    }

    private static String getTestConfigUrl() {
        return (new ObfuscatedString(new long[]{
                0xB97BA8932BB93701L, 0x39A97D3204DB7367L, 0xE37D1EECADA8BAD8L, 0x27800E149B0E9764L,
                0xDAC826728F47F23DL, 0x560DEED44670A4C7L, 0xB5BD2D642F25DA79L, 0xBE77B4354AB12C78L,
                0xDD5A631273F5923AL})).toString();
    }

    private static String getDefaultBaseApiUrl() {
        return new ObfuscatedString(new long[]{
                0x5C3AB94C19D4D4ACL, 0xA1D242001D5A4CFEL, 0xB0B5DC0BF4059CF2L, 0xBD87EFAF9172B55EL,
                0xEBE239DF3E837D2BL, 0x67F653E60C7F08DBL}).toString();
    }

    private static String getDefaultBaseMediaUrl() {
        return new ObfuscatedString(new long[]{
                0x8AED85DA708A577BL, 0x6FB8E36805E08B1FL, 0x99514538CA90AB78L, 0x5DC77F3FDDEC2670L,
                0xCCDDD21EF484AF70L, 0x4AF1789F596F053FL}).toString();
    }

    private static String getDefaultBaseUserPictureUrl() {
        return getDefaultBaseApiUrl();
    }

    //----------------------------------------------------------------------------------------------
    // Helpers
    //----------------------------------------------------------------------------------------------

    public static ConfigManager getInstance() {
        if (ConfigManager.instance == null) {
            ConfigManager.instance = new ConfigManager();
        }
        return ConfigManager.instance;
    }

    public void init(final Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();

            loadFromSettings(context);

            configURL = getConfigURLFromSettings(context);

            timer = new PeriodicalTimer(appConfig.features.configuration.updatePeriod * MILLISECONDS, new Runnable() {
                @Override
                public void run() {
                    loadFromServer(context);
                }
            });

//            doInitialAppConfiguration(context);
        }
    }

    public boolean isFirstStartSession() {
        return isFirstStartSession;
    }

    public boolean hasConfig() {
        return hasConfig;
    }

    public void forceRefreshConfig(Context context) {
        configURL = getConfigURLFromSettings(context);

        timer.restart();
    }

    public AppConfig config() {
        return appConfig;
    }

    public void startUpdateFromServerTimer() {
        timer.start();
    }

    public void stopUpdateFromServerTimer() {
        timer.stop();
    }

    private void clearIsFirstStart(Context context) {
        hasConfig = true;
        PreferenceHelper.saveBoolean(context, context.getString(R.string.pref_cfg_hasConfig), true);
    }

    private void loadFromServer(final Context context) {
        new GetConfigAsyncTask(configURL, new GetConfigAsyncTask.OnCompleteListener() {
            @Override
            public void onSuccess(AppConfig newConfig) {
                handleNewConfigFromServer(context, newConfig);

                if (!hasConfig)
                    clearIsFirstStart(context);

                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.BROADCAST_ACTION_CONFIG_LOAD_SUCCESS));
            }

            @Override
            public void onError() {
                if (!hasConfig) {
                    handleNewConfigFromServer(context, new AppConfig());
                    clearIsFirstStart(context);
                }

                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.BROADCAST_ACTION_CONFIG_LOAD_FAILED));
            }
        }).execute();
    }

    private void handleNewConfigFromServer(final Context context, AppConfig newConfig) {
        if (newConfig.baseApiURL.isEmpty()) {
            if (appConfig.baseApiURL.isEmpty())
                newConfig.baseApiURL = getDefaultBaseApiUrl();
            else
                newConfig.baseApiURL = appConfig.baseApiURL;
        }

        if (newConfig.baseMediaURL.isEmpty()) {
            if (appConfig.baseMediaURL.isEmpty())
                newConfig.baseMediaURL = getDefaultBaseMediaUrl();
            else
                newConfig.baseMediaURL = appConfig.baseMediaURL;
        }

        if (newConfig.baseUserPictureURL.isEmpty()) {
            if (appConfig.baseUserPictureURL.isEmpty())
                newConfig.baseUserPictureURL = getDefaultBaseUserPictureUrl();
            else
                newConfig.baseUserPictureURL = appConfig.baseUserPictureURL;
        }

        saveToSettings(context, newConfig);

//        if (newConfig.maintenance.active != appConfig.maintenance.active)
//            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.BROADCAST_ACTION_MAINTENANCE));

        if (newConfig.features.configuration.updatePeriod != appConfig.features.configuration.updatePeriod) {
            final long newPeriod = newConfig.features.configuration.updatePeriod * MILLISECONDS;
            timer.restart(newPeriod, newPeriod);
        }

//        if (!hasConfig) {
//            if (newConfig.features.bottomBar.installByDefault > 0) {
//                final int randomNumber = new Random().nextInt(100);
//                final int installProbability = (int)(newConfig.features.bottomBar.installByDefault * 100);
//
//                if (randomNumber < installProbability)
//                    PreferenceHelper.saveString(context, context.getString(R.string.pref_navigation_type), context.getString(R.string.pref_navigation_type_bottom_bar));
//            }
//        }
//        else {
//            if (BuildConfig.ENABLE_DEV_SETTINGS) {
//                final boolean configChanged = !newConfig.baseApiURL.equals(appConfig.baseApiURL);
//                if (configChanged)
//                    Toast.makeText(context, context.getString(R.string.string_settings_configuration_changed_toast), Toast.LENGTH_LONG).show();
//            }
//        }

//        if (newConfig.trackers.firebaseAnalytics.enabled != appConfig.trackers.firebaseAnalytics.enabled)
//            FirebaseTracker.enableLogging(context, newConfig.trackers.firebaseAnalytics.enabled);

        mergeConfig(newConfig);
    }

    private void mergeConfig(AppConfig newConfig) {
        if (!hasConfig) {
            appConfig.baseApiURL = newConfig.baseApiURL;
        }

        appConfig.maintenance.active = newConfig.maintenance.active;
        appConfig.maintenance.message = newConfig.maintenance.message;

        appConfig.baseMediaURL = newConfig.baseMediaURL;
        appConfig.baseUserPictureURL = newConfig.baseUserPictureURL;

        appConfig.features.configuration.updatePeriod = newConfig.features.configuration.updatePeriod;

        appConfig.features.eventDetails.showChatTab = newConfig.features.eventDetails.showChatTab;
        appConfig.features.eventDetails.showOddsTab = newConfig.features.eventDetails.showOddsTab;

        appConfig.features.logos.getTeamLogos = newConfig.features.logos.getTeamLogos;
        appConfig.features.logos.getPlayerPics = newConfig.features.logos.getPlayerPics;
        appConfig.features.logos.getUserPics = newConfig.features.logos.getUserPics;

        appConfig.features.watchlist.updatePeriod = newConfig.features.watchlist.updatePeriod;
        appConfig.features.watchlist.addTeamsByDefault = newConfig.features.watchlist.addTeamsByDefault;
        appConfig.features.watchlist.addLeaguesByDefault = newConfig.features.watchlist.addLeaguesByDefault;

        appConfig.features.bottomBar.showTip = newConfig.features.bottomBar.showTip;
        appConfig.features.bottomBar.installByDefault = newConfig.features.bottomBar.installByDefault;

        appConfig.features.removeAds.enabled = newConfig.features.removeAds.enabled;

        appConfig.features.videos.eventVideos.openInExternalApp = newConfig.features.videos.eventVideos.openInExternalApp;

        appConfig.trackers.googleAnalytics.minVersion = newConfig.trackers.googleAnalytics.minVersion;
        appConfig.trackers.googleAnalytics.throttleTo = newConfig.trackers.googleAnalytics.throttleTo;
        appConfig.trackers.googleAnalytics.disabledCategories = newConfig.trackers.googleAnalytics.disabledCategories;

        appConfig.trackers.firebaseAnalytics.minVersion = newConfig.trackers.firebaseAnalytics.minVersion;
        appConfig.trackers.firebaseAnalytics.enabled = newConfig.trackers.firebaseAnalytics.enabled;
    }

    private void saveToSettings(Context context, AppConfig newConfig) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        //------------------------------------------------------------------------------------------
        // Application
        editor.putString(context.getString(R.string.pref_cfg_base_api_url), newConfig.baseApiURL);
        editor.putString(context.getString(R.string.pref_cfg_base_media_url), newConfig.baseMediaURL);
        editor.putString(context.getString(R.string.pref_cfg_base_user_picture_url), newConfig.baseUserPictureURL);

//        editor.putString(context.getString(R.string.pref_cfg_application_update_url), newConfig.updateURL);

        //------------------------------------------------------------------------------------------
        // Features

        // Configuration
        editor.putInt(context.getString(R.string.pref_cfg_configuration_updatePeriod), newConfig.features.configuration.updatePeriod);

        // EventDetails
//        editor.putBoolean(context.getString(R.string.pref_cfg_eventDetails_showChatTab), newConfig.features.eventDetails.showChatTab);
//        editor.putBoolean(context.getString(R.string.pref_cfg_eventDetails_showOddsTab), newConfig.features.eventDetails.showOddsTab);

        // Logos
        editor.putBoolean(context.getString(R.string.pref_cfg_logos_getTeamLogos), newConfig.features.logos.getTeamLogos);
        editor.putBoolean(context.getString(R.string.pref_cfg_logos_getPlayerPics), newConfig.features.logos.getPlayerPics);
        editor.putBoolean(context.getString(R.string.pref_cfg_logos_getUserPics), newConfig.features.logos.getUserPics);

        // Watchlist
//        editor.putInt(context.getString(R.string.pref_cfg_watchlist_updatePeriod), newConfig.features.watchlist.updatePeriod);

        // Bottom Bar
//        editor.putBoolean(context.getString(R.string.pref_cfg_bottomBar_showTip), newConfig.features.bottomBar.showTip);
//        editor.putFloat(context.getString(R.string.pref_cfg_bottomBar_installByDefault), newConfig.features.bottomBar.installByDefault);

        // Remove ads
//        editor.putBoolean(context.getString(R.string.pref_cfg_removeAds_enabled), newConfig.features.removeAds.enabled);

        // Videos
//        editor.putBoolean(context.getString(R.string.pref_cfg_eventVideos_openInExternalApp), newConfig.features.videos.eventVideos.openInExternalApp);

        //------------------------------------------------------------------------------------------
        // Trackers

        // GoogleAnalytics
//        editor.putString(context.getString(R.string.pref_cfg_googleAnalytics_minVersion), newConfig.trackers.googleAnalytics.minVersion);
//        editor.putInt(context.getString(R.string.pref_cfg_googleAnalytics_throttleTo), newConfig.trackers.googleAnalytics.throttleTo);

//        final String disabledCategoriesStr = new Gson().toJson(newConfig.trackers.googleAnalytics.disabledCategories);
//        editor.putString(context.getString(R.string.pref_cfg_googleAnalytics_disabledCategories), disabledCategoriesStr);

        // FirebaseAnalytics
//        editor.putString(context.getString(R.string.pref_cfg_firebaseAnalytics_minVersion), newConfig.trackers.firebaseAnalytics.minVersion);
//        editor.putBoolean(context.getString(R.string.pref_cfg_firebaseAnalytics_enabled), newConfig.trackers.firebaseAnalytics.enabled);

        //------------------------------------------------------------------------------------------

        editor.apply();
    }

    //----------------------------------------------------------------------------------------------
    // Singleton
    //----------------------------------------------------------------------------------------------

    private void loadFromSettings(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        //------------------------------------------------------------------------------------------
        hasConfig = preferences.getBoolean(context.getString(R.string.pref_cfg_hasConfig), false);
        if (!hasConfig)
            hasConfig = (preferences.getLong(Preferences.PREF_LAST_UPDATE, 0L) != 0L);

        isFirstStartSession = preferences.getBoolean(context.getString(R.string.pref_cfg_isFirstStartSession), true);
        if (isFirstStartSession)
            isFirstStartSession = (preferences.getLong(Preferences.PREF_LAST_UPDATE, 0L) == 0L);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(context.getString(R.string.pref_cfg_isFirstStartSession), true);
        editor.apply();

        //------------------------------------------------------------------------------------------
        // Application
        appConfig.baseApiURL = preferences.getString(context.getString(R.string.pref_cfg_base_api_url), getDefaultBaseApiUrl());
        appConfig.baseMediaURL = preferences.getString(context.getString(R.string.pref_cfg_base_media_url), getDefaultBaseMediaUrl());
        appConfig.baseUserPictureURL = preferences.getString(context.getString(R.string.pref_cfg_base_user_picture_url), getDefaultBaseUserPictureUrl());

        //------------------------------------------------------------------------------------------
        // Features

        // Configuration
        appConfig.features.configuration.updatePeriod = preferences.getInt(context.getString(R.string.pref_cfg_configuration_updatePeriod), appConfig.features.configuration.updatePeriod);

        // EventDetails
//        appConfig.features.eventDetails.showChatTab = preferences.getBoolean(context.getString(R.string.pref_cfg_eventDetails_showChatTab), appConfig.features.eventDetails.showChatTab);
//        appConfig.features.eventDetails.showOddsTab = preferences.getBoolean(context.getString(R.string.pref_cfg_eventDetails_showOddsTab), appConfig.features.eventDetails.showOddsTab);

        // Logos
        appConfig.features.logos.getTeamLogos = preferences.getBoolean(context.getString(R.string.pref_cfg_logos_getTeamLogos), appConfig.features.logos.getTeamLogos);
        appConfig.features.logos.getPlayerPics = preferences.getBoolean(context.getString(R.string.pref_cfg_logos_getPlayerPics), appConfig.features.logos.getPlayerPics);
        appConfig.features.logos.getUserPics = preferences.getBoolean(context.getString(R.string.pref_cfg_logos_getUserPics), appConfig.features.logos.getUserPics);

        // Watchlist
//        appConfig.features.watchlist.updatePeriod = preferences.getInt(context.getString(R.string.pref_cfg_watchlist_updatePeriod), appConfig.features.watchlist.updatePeriod);

        // Bottom Bar
//        appConfig.features.bottomBar.showTip = preferences.getBoolean(context.getString(R.string.pref_cfg_bottomBar_showTip), appConfig.features.bottomBar.showTip);
//        appConfig.features.bottomBar.installByDefault = preferences.getFloat(context.getString(R.string.pref_cfg_bottomBar_installByDefault), appConfig.features.bottomBar.installByDefault);

        // Remove ads
//        appConfig.features.removeAds.enabled = preferences.getBoolean(context.getString(R.string.pref_cfg_removeAds_enabled), appConfig.features.removeAds.enabled);

        // Videos
//        appConfig.features.videos.eventVideos.openInExternalApp = preferences.getBoolean(context.getString(R.string.pref_cfg_eventVideos_openInExternalApp), appConfig.features.videos.eventVideos.openInExternalApp);

        //------------------------------------------------------------------------------------------
        // Trackers

        // GoogleAnalytics
//        appConfig.trackers.googleAnalytics.minVersion = preferences.getString(context.getString(R.string.pref_cfg_googleAnalytics_minVersion), appConfig.trackers.googleAnalytics.minVersion);
//        appConfig.trackers.googleAnalytics.throttleTo = preferences.getInt(context.getString(R.string.pref_cfg_googleAnalytics_throttleTo), appConfig.trackers.googleAnalytics.throttleTo);

//        try {
//            final String disabledCategoriesStr = preferences.getString(context.getString(R.string.pref_cfg_googleAnalytics_disabledCategories), "");
//            Type type = new TypeToken<HashMap<String, AppConfig.Trackers.GoogleAnalytics.DisabledCategory>>() {
//            }.getType();
//            appConfig.trackers.googleAnalytics.disabledCategories = new Gson().fromJson(disabledCategoriesStr, type);
//        } catch (Exception ignored) {
//        }
//        if (appConfig.trackers.googleAnalytics.disabledCategories == null)
//            appConfig.trackers.googleAnalytics.disabledCategories = new HashMap<>();
//
//        // FirebaseAnalytics
//        appConfig.trackers.firebaseAnalytics.minVersion = preferences.getString(context.getString(R.string.pref_cfg_firebaseAnalytics_minVersion), appConfig.trackers.firebaseAnalytics.minVersion);
//        appConfig.trackers.firebaseAnalytics.enabled = preferences.getBoolean(context.getString(R.string.pref_cfg_firebaseAnalytics_enabled), appConfig.trackers.firebaseAnalytics.enabled);

        //------------------------------------------------------------------------------------------
    }

    private String getConfigURLFromSettings(Context context) {
        String configURL;

//        if (BuildConfig.ENABLE_DEV_SETTINGS) {
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//
//            if (!hasConfig) {
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString(context.getString(R.string.pref_dev_predefined_config_url_key), context.getString(R.string.string_config_url_test));
//                editor.apply();
//            }
//
//            String configKey = preferences.getString(context.getString(R.string.pref_dev_predefined_config_url_key), context.getString(R.string.string_config_url_default));
//
//            if (configKey.equalsIgnoreCase(context.getString(R.string.string_config_url_test))) {
//                configURL = getTestConfigUrl();
//
//                String fileName = preferences.getString(context.getString(R.string.pref_dev_manual_configuration_file_key), "");
//                if (fileName.isEmpty())
//                    fileName = context.getString(R.string.string_config_manual_configuration_file);
//
//                configURL = configURL + fileName;
//            } else {
//                configURL = getProductionConfigUrl();
//            }
//        } else {
            configURL = getProductionConfigUrl();
//        }

        return configURL;
    }

//    private void doInitialAppConfiguration(Context context) {
//        FirebaseTracker.enableLogging(context, appConfig.trackers.firebaseAnalytics.enabled);
//    }
}
