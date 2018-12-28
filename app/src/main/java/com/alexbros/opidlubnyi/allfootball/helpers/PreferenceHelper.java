package com.alexbros.opidlubnyi.allfootball.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.alexbros.opidlubnyi.allfootball.Preferences;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.models.ModelData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PreferenceHelper {
    public static int getToolBarColor(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(Preferences.PREF_TOOL_BAR_COLOR_KEY, context.getResources().getColor(R.color.colorPrimary));
    }

    @SuppressLint("NewApi")
    public static boolean saveBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();

        return value;
    }

    @SuppressLint("NewApi")
    public static String saveString(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();

        return value;
    }

    public static ModelData fillAppPreferences(Context context, ModelData model) {
//        try {
//            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            model.versionName = packageInfo.versionName;
//            model.versionCode = String.valueOf(packageInfo.versionCode);
//        } catch (Exception ignored) {}
//
//        model.deviceId = getDeviceId(context);
//
//        Resources resources = context.getResources();
//        model.isTabletLayout = resources.getBoolean(R.bool.isTabletLayout);
//
//        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//        model.displayDensityDpi = displayMetrics.densityDpi;
//        model.displayDensity = displayMetrics.density;

        // GENERAL STUFF & LAYOUT
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//
//        if (ConfigManager.getInstance().isFirstStartSession()) {
//            setDefaultNotificationSound(context, preferences, R.string.pref_home_goal_key);
//            setDefaultNotificationSound(context, preferences, R.string.pref_away_goal_key);
//            setDefaultNotificationSound(context, preferences, R.string.pref_favorite_goal_key);
//            setDefaultNotificationSound(context, preferences, R.string.pref_favorite_opponent_goal_key);
//            setDefaultNotificationSound(context, preferences, R.string.pref_red_cards_sound_key);
//            setDefaultNotificationSound(context, preferences, R.string.pref_tone_kick_off);
//            setDefaultNotificationSound(context, preferences, R.string.pref_tone_half_time);
//            setDefaultNotificationSound(context, preferences, R.string.pref_tone_ht_kick_off);
//            setDefaultNotificationSound(context, preferences, R.string.pref_ringtone_finished_key);
//            setDefaultNotificationSound(context, preferences, R.string.pref_tone_lineup);
//            setDefaultNotificationSound(context, preferences, R.string.pref_tone_new_video);
//            setDefaultNotificationSound(context, preferences, R.string.pref_favorite_team_sound_key);
//        }
//
//        model.currentFilterViewId = preferences.getInt(context.getString(R.string.pref_current_filter_view_id_key), Constants.LIST_ITEM_ID_ALL);
//        model.sortScoresByTime = preferences.getBoolean(context.getString(R.string.pref_sort_scores_by_time), false);
//        model.forceUTCEnabled = preferences.getBoolean(context.getString(R.string.pref_force_utc_enabled_key), false);
//        model.verticalLayoutEnabled = preferences.getString(context.getString(R.string.pref_layout), context.getString(R.string.pref_layout_horizontal)).equals(context.getString(R.string.pref_layout_vertical));
//        model.showTeamLogosEnabled = preferences.getBoolean(context.getString(R.string.pref_show_team_logos_enabled_key), true);
//        model.navigationTypeBottom = preferences.getString(context.getString(R.string.pref_navigation_type), context.getString(R.string.pref_navigation_type_side_menu)).equals(context.getString(R.string.pref_navigation_type_bottom_bar));
        //THEME
        //model.setThemeColors(Integer.parseInt(preferences.getString("themeType", "0")));

        // WATCHLIST & NOTIFICATION
//        model.pushToken = preferences.getString(context.getString(R.string.pref_push_token_key), "");
//        model.watchlistAnonymousUserId = preferences.getString(context.getString(R.string.pref_gcm_watchlist_anonymous_user_id_key), getAnonymousUserId(model.deviceId));
//
//        model.notificationDefaults.useForAllGames = preferences.getBoolean(context.getString(R.string.pref_notification_defaults_use_for_all_key), false);
//        model.notificationDefaults.notifyOnReminder = preferences.getBoolean(context.getString(R.string.pref_notify_on_reminder_key), false);
//        model.notificationDefaults.notifyOnStart = preferences.getBoolean(context.getString(R.string.pref_notify_on_start_key), true);
//        model.notificationDefaults.notifyOnHalfTime = preferences.getBoolean(context.getString(R.string.pref_notify_on_half_time_key), true);
//        model.notificationDefaults.notifyOnFullTime = preferences.getBoolean(context.getString(R.string.pref_notify_on_full_time_key), true);
//        model.notificationDefaults.notifyOnGoal = preferences.getBoolean(context.getString(R.string.pref_notify_on_goal_key), true);
//        model.notificationDefaults.notifyOnRed = preferences.getBoolean(context.getString(R.string.pref_notify_on_red_key), false);
//        model.notificationDefaults.notifyOnLineups = preferences.getBoolean(context.getString(R.string.pref_notify_on_lineups_key), false);
//        model.notificationDefaults.notifyOnVideos = preferences.getBoolean(context.getString(R.string.pref_notify_on_videos_key), true);
//
//        model.watchList = getStringArrayListFromCSVString(preferences.getString(context.getString(R.string.pref_watchlist_items_key), ""));

//        model.notifyCardsSound = preferences.getString(context.getString(R.string.pref_red_cards_sound_key), "");
//        model.fullTimeSound = preferences.getString(context.getString(R.string.pref_ringtone_finished_key), "");
//        model.favTeamSound = preferences.getString(context.getString(R.string.pref_favorite_team_sound_key), "");
//        model.vibrate = preferences.getBoolean(context.getString(R.string.pref_vibrate_key), false);
//        model.notifySounds = preferences.getBoolean(context.getString(R.string.pref_sounds_key), true);
//        model.lineupsSound = preferences.getString(context.getString(R.string.pref_tone_lineup), "");
//        model.videoSound = preferences.getString(context.getString(R.string.pref_tone_new_video), "");
//        model.kickOffSound = preferences.getString(context.getString(R.string.pref_tone_kick_off), "");
//        model.halfTimeSound = preferences.getString(context.getString(R.string.pref_tone_half_time), "");
//        model.halfTimeKickOffSound = preferences.getString(context.getString(R.string.pref_tone_ht_kick_off), "");
//
//        model.notifySoundHomeGoal = preferences.getString(context.getString(R.string.pref_home_goal_key), "");
//        model.notifySoundAwayGoal = preferences.getString(context.getString(R.string.pref_away_goal_key), "");
//        model.notifySoundFavoriteGoal = preferences.getString(context.getString(R.string.pref_favorite_goal_key), "");
//        model.notifySoundFavoriteOpponentGoal = preferences.getString(context.getString(R.string.pref_favorite_opponent_goal_key), "");

        // FAVOURITE PLAYERS
//        String players = preferences.getString(Preferences.PREF_FAVORITE_PLAYERS, "");
//        List<PlayerProfile> playersList;
//        try {
//            Type type = new TypeToken<ArrayList<PlayerProfile>>() {
//            }.getType();
//            playersList = new Gson().fromJson(players, type);
//        } catch (Exception | AssertionError e) {
//            playersList = new ArrayList<>();
//        }
//
//        if (playersList == null)
//            playersList = new ArrayList<>();
//
//        model.userProfile.favoritePlayersList = playersList;

        // FOLLOWING USERS
//        String followingUsers = preferences.getString(Preferences.PREF_FOLLOWING_USERS, "");
//        List<UserProfile> users;
//        try {
//            Type type = new TypeToken<ArrayList<UserProfile>>() {
//            }.getType();
//            users = new Gson().fromJson(followingUsers, type);
//        } catch (Exception | AssertionError e) {
//            e.printStackTrace();
//            users = new ArrayList<>();
//        }
//
//        if (users == null)
//            users = new ArrayList<>();
//
//        model.userProfile.followingUsersList = users;


        // FOLLOWERS USER
//        String followers = preferences.getString(Preferences.PREF_FOLLOWERS, "");
//        List<UserProfile> usersFollowers;
//        try {
//            Type type = new TypeToken<ArrayList<UserProfile>>() {
//            }.getType();
//            usersFollowers = new Gson().fromJson(followers, type);
//        } catch (Exception | AssertionError e) {
//            e.printStackTrace();
//            usersFollowers = new ArrayList<>();
//        }
//
//        if (usersFollowers == null)
//            usersFollowers = new ArrayList<>();
//
//        model.userProfile.followersList = usersFollowers;

        // FAVOURITE TEAMS
//        String items = preferences.getString(context.getString(R.string.pref_favorite_team_items_key), "");
//
//        List<FavoriteTeam> teams;
//        try {
//            Type type = new TypeToken<ArrayList<FavoriteTeam>>() {
//            }.getType();
//            teams = new Gson().fromJson(items, type);
//
//            if (teams != null) {
//                for (FavoriteTeam team: teams) {
//                    team.nameLocalized = TeamNameHelper.getLocalizedName(team.name, context);
//                    team.countryNameLocalized = TeamNameHelper.getLocalizedName(team.countryName, context);
//                }
//            }
//        } catch (Exception | AssertionError e) {
//            e.printStackTrace();
//            teams = new ArrayList<>();
//        }
//
//        if (teams == null)
//            teams = new ArrayList<>();
//
//        model.userProfile.favTeamList = teams;
//        model.favoriteTeamAlarmHour = preferences.getInt(context.getString(R.string.pref_favorite_team_alarm_hour_key), -1);
//        model.favoriteTeamAlarmMinute = preferences.getInt(context.getString(R.string.pref_favorite_team_alarm_minute_key), -1);
//        model.favoriteTeamAlarmSecond = preferences.getInt(context.getString(R.string.pref_favorite_team_alarm_second_key), -1);
//        if (model.favoriteTeamAlarmHour == -1 && model.favoriteTeamAlarmMinute == -1 && model.favoriteTeamAlarmSecond == -1) {
//            int[] r = DateHelper.getDefaultAlarmTime();
//            saveFavoriteTeamAlarmTime(context, model, r[0], r[1], r[2]);
//        }
//
//        model.favoriteTeamAlarmEnabled = preferences.getBoolean(context.getString(R.string.pref_favorite_team_alarm_enabled_key), true);
//        model.userSettings.push.addTeamsToWatchList = preferences.getBoolean(context.getString(R.string.pref_add_favorite_matches_enabled_key), true);
//        model.userSettings.push.addLeaguesToWatchlist = preferences.getBoolean(context.getString(R.string.pref_add_favorite_leagues_enabled_key), false);
//        model.chatEnabled = preferences.getBoolean(context.getString(R.string.pref_user_chat), true);
//        model.oddsEnabled = preferences.getBoolean(context.getString(R.string.pref_user_odds), true);
        //model.addFavoriteMatchesCalendarEnabled = preferences.getBoolean("addFavoriteMatchesCalendarEnabled", false);

        // FILTER
//        model.userProfile.mySelectionFilter = getStringArrayListFromCSVString(preferences.getString("filterItems", ""));

        // STARRED LEAGUES / ORDERING
//        String leagues = preferences.getString(context.getString(R.string.pref_favorite_leagues_items_key), "");
//        List<League> leaguesList = null;
//        try {
//            Type type = new TypeToken<ArrayList<League>>() {
//            }.getType();
//            leaguesList = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(leagues, type);
//        } catch (Exception | AssertionError e) {
//            e.printStackTrace();
//        }
//
//        if (leaguesList == null) {
//            leaguesList = new ArrayList<>();
//        } else {
//            for (League league: leaguesList) {
//                league.countryLocalized = CountryNameHelper.get(league.country, context);
//            }
//        }
//
//        model.userProfile.favoriteLeaguesList = leaguesList;

        // CHAT, USER & USERNAME
//		model.socialEnabled = preferences.getBoolean("socialEnabled", true);
//        model.userProfile.userName = preferences.getString(context.getString(R.string.pref_user_name_key), "");


        // check necessary cause generateUserId overrides setting
//		if (preferences.contains("userId")) {
//        model.userProfile.userId = preferences.getString(context.getString(R.string.pref_user_id_key), "");
//		}

        // BETTING
        model.oddFormat = preferences.getString(context.getString(R.string.pref_odd_format_key), context.getString(R.string.pref_default_odd_format));

//        model.preferencesFilled = true;
//
//        model.hasNoAdsSubscription = preferences.getBoolean(context.getString(R.string.pref_has_no_ads_subscription), false);
//        model.userProfile.showAds = preferences.getBoolean(context.getString(R.string.pref_user_see_ads), true);
//        model.userProfile.isModerator = preferences.getBoolean(context.getString(R.string.pref_user_is_moderator), false);

        // DEVELOPMENT
//        if (BuildConfig.ENABLE_DEV_SETTINGS) {
//            model.dev_geoCountryCode = preferences.getString(context.getString(R.string.pref_dev_geo_country_code), context.getString(R.string.geo_country_code_default));
//
//            String val = preferences.getString(context.getString(R.string.pref_dev_server_response_delay), "0");
//            if (val.length() > 0)
//                model.dev_serverResponseDelay = Long.parseLong(val);
//            else
//                model.dev_serverResponseDelay = 0;
//
//            final String adsSource = preferences.getString(context.getString(R.string.pref_dev_ads_source), context.getString(R.string.string_dev_ads_source_default));
//            model.dev_adsTestMode = adsSource.equals(context.getString(R.string.string_dev_ads_source_test));
//        }

        // Collapsed Leagues
//        String collapsedLeaguesString = preferences.getString(Preferences.PREF_COLLAPSED_LEAGUES, "");
//        HashSet<String> collapsedLeagues = null;
//
//        try {
//            Type type = new TypeToken<HashSet<String>>() {}.getType();
//            collapsedLeagues = new Gson().fromJson(collapsedLeaguesString, type);
//        } catch (Exception | AssertionError e) {
//            e.printStackTrace();
//        }
//
//        if (collapsedLeagues == null)
//            collapsedLeagues = new HashSet<>();
//
//        model.collapsedLeagues = collapsedLeagues;
//
//        // leagues order
//        model.favouriteLeaguesOrder = getLeaguesOrder(context, Preferences.PREF_FAVOURITE_LEAGUES_ORDER);
//        model.allLeaguesOrder = getLeaguesOrder(context, Preferences.PREF_ALL_LEAGUES_ORDER);

        return model;
    }

}
