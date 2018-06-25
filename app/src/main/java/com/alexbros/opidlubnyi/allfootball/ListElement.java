package com.alexbros.opidlubnyi.allfootball;

import java.io.Serializable;

public class ListElement implements Serializable {
    private String timeTextView;
    private String firstTeamName;
    private String secondTeamName;

    public ListElement() {
    }

    public ListElement(String time, String firstTeamName, String secondTeamName) {
        this.timeTextView = time;
        this.firstTeamName = firstTeamName;
        this.secondTeamName = secondTeamName;
    }

    public void setFirstTeamName(String firstTeamName){ this.firstTeamName = firstTeamName; }
    public String getFirstTeamName(){ return firstTeamName; }

    public void setSecondTeamName(String secondTeamName){ this.secondTeamName = secondTeamName; }
    public String getSecondTeamName(){ return secondTeamName; }

    public void setTimeTextView(String timeTextView){ this.timeTextView = timeTextView; }
    public String getTimeTextView(){ return timeTextView; }
}
