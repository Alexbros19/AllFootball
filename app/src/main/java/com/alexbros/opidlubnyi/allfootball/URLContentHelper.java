package com.alexbros.opidlubnyi.allfootball;


import com.alexbros.opidlubnyi.allfootball.util.ObfuscatedString;

public class URLContentHelper {
    public static String getAllGoalsClientHeader() {
        return new ObfuscatedString(new long[]{0x161277BED41F67AEL, 0xA1204B68013F367EL, 0xA261FE01B58811D8L}).toString();
    }

    public static String getAllGoalsClientHeaderValue() {
        return "a " + "5.4" + " " + "701";
    }
}
