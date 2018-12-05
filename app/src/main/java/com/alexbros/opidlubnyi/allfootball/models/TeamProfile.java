package com.alexbros.opidlubnyi.allfootball.models;

import java.util.LinkedHashMap;

public class TeamProfile {
    public String name = "";

    public LinkedHashMap<String, String> channels = new LinkedHashMap<>();

    public boolean hasChannels() {
        for (String key : channels.keySet()) {
            if (!channels.get(key).equals(""))
                return true;
        }
        return false;
    }
}
