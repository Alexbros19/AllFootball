package com.alexbros.opidlubnyi.allfootball;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {
    private static int utcOffset = 0;
    private static String yearlessPattern = "";
    public static boolean utcOffsetSet = false;

    public static synchronized int getUTCOffset() {
        if (!utcOffsetSet) {
            utcOffsetSet = true;

            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();

            if (tz.inDaylightTime(cal.getTime())) {
                utcOffset = (tz.getRawOffset() / 1000) + (tz.getDSTSavings() / 1000);
            } else {
                utcOffset = (tz.getRawOffset() / 1000);
            }
        }

        return utcOffset;
    }

    public static String getYearlessPattern() {
        if (yearlessPattern.equals("")) {
            SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance(java.text.DateFormat.SHORT, Locale.getDefault());
            yearlessPattern = sdf.toLocalizedPattern().replaceAll("\\W?[Yy]+\\W?", "");
        }

        return yearlessPattern;
    }

    public static String getFormattedTime(Context context, Long utcStartTime) {
        SimpleDateFormat sdf;
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            sdf = new SimpleDateFormat("HH:mm", context.getResources().getConfiguration().locale);
            return sdf.format(utcStartTime);
        } else {
            sdf = new SimpleDateFormat("h:mma", context.getResources().getConfiguration().locale);
            String time = sdf.format(utcStartTime);
            return time.toLowerCase().substring(0, time.length() - 1);
        }
    }
}
