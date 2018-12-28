package com.alexbros.opidlubnyi.allfootball.helpers;

import java.text.DecimalFormat;

public class DecimalHelper {
    private static DecimalFormat df = new DecimalFormat("####0.00");
//    private static DecimalFormat followersFormat = new DecimalFormat("###.#");
//    private static final String[] KMB = new String[]{"", "K", "M", "B"};

    public static String getFormattedOdd(float odd, String oddFormat) {
        if (oddFormat.equals("EU"))
            return df.format(odd);
        else if (oddFormat.equals("UK")) {
            int hundretValue = Math.round(odd * 100) - 100;
            int ggt = ggt(hundretValue, 100);
            return (hundretValue / ggt) + "/" + (100 / ggt);
        } else if (oddFormat.equals("US")) {
            if (odd >= 2)
                return "+" + Math.round(100 * (odd - 1));
            else if (odd == 1)
                return "-0";
            else
                return String.valueOf(Math.round(100 / (1 - odd)));
        } else if (oddFormat.equals("HK")) {
            return df.format(getHK(odd));
        } else if (oddFormat.equals("Indo")) {
            float oddHK = getHK(odd);
            if (oddHK >= 1)
                return df.format(oddHK);
            else
                return df.format((1f/getHK(odd))*(-1));
        } else if (oddFormat.equals("Malay")) {
            float oddHK = getHK(odd);
            if (oddHK <= 1)
                return df.format(oddHK);
            else
                return df.format((1f/oddHK)*(-1));
        } else { //Implied probability
            return df.format((1f/odd)*100) + "%";
        }
    }

//    public static String formatFollowers(float d) {
//        int i = 0;
//        while (d >= 1000) { i++; d /= 1000; }
//
//        return followersFormat.format(d) + KMB[i];
//    }

    private static int ggt(int u, int v) {
        return ((u > 0) ? ggt(v % u, u) : v);
    }

//    public static String getFormattedWinning(float odd) {
//        return df.format(odd);
//    }
//
//    public static String getAccountBalance(float balance) {
//        // String stringOdd = nf.format(odd);
//        return df.format(balance);
//    }

//    public static String getTimeWithZero(int i) {
//        if (i < 10)
//            return "0" + String.valueOf(i);
//        else
//            return String.valueOf(i);
//    }

    private static float getHK(float decimalOdd) {
        return decimalOdd - 1;
    }
}
