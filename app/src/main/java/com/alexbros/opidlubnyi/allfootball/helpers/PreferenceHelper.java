package com.alexbros.opidlubnyi.allfootball.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.alexbros.opidlubnyi.allfootball.Preferences;
import com.alexbros.opidlubnyi.allfootball.R;

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
}
