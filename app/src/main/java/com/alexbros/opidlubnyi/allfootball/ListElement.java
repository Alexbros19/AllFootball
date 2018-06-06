package com.alexbros.opidlubnyi.allfootball;

import java.io.Serializable;

class ListElement implements Serializable {
    private String firstTeamName;
    private String secondTeamName;

    void setFirstTeamName(String firstTeamName){ this.firstTeamName = firstTeamName; }
    String getFirstTeamName(){ return firstTeamName; }

    void setSecondTeamName(String secondTeamName){ this.secondTeamName = secondTeamName; }
    String getSecondTeamName(){ return secondTeamName; }
}
