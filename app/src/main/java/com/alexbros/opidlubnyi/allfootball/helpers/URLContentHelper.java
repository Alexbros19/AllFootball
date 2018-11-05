package com.alexbros.opidlubnyi.allfootball.helpers;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.alexbros.opidlubnyi.allfootball.util.ObfuscatedString;

public class URLContentHelper {
    public static String getAllGoalsClientHeader() {
        return new ObfuscatedString(new long[]{0x161277BED41F67AEL, 0xA1204B68013F367EL, 0xA261FE01B58811D8L}).toString();
    }

    public static String getAllGoalsClientHeaderValue() {
        return "a " + "5.4" + " " + "701";
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = null;
        if (manager != null) {
            ni = manager.getActiveNetworkInfo();
        }
        return (ni != null && ni.isConnected());
    }
}
