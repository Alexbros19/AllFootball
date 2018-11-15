package com.alexbros.opidlubnyi.allfootball.helpers;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.alexbros.opidlubnyi.allfootball.R;

public class ActionHelper {
    public static void view(Context context, String uriString) {
        try {
            viewNotSafe(context, uriString);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.string_error_unknown), Toast.LENGTH_SHORT).show();
        }
    }

    private static void viewNotSafe(Context context, String uriString) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uriString)));
    }

//    public static void viewPlayMarket(Context context) {
//        view(context, "market://details?id=" + context.getPackageName());
//    }
//
//    public static void viewPlayMarketServices(Context context) {
//        try {
//            viewNotSafe(context, "market://details?id=com.google.android.gms");
//        } catch (android.content.ActivityNotFoundException anfe) {
//            view(context, "http://play.google.com/store/apps/details?id=com.google.android.gms");
//        }
//    }
}
