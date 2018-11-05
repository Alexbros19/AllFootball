package com.alexbros.opidlubnyi.allfootball.helpers;

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
}
