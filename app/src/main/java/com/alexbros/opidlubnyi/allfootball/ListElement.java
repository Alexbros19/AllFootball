package com.alexbros.opidlubnyi.allfootball;

import java.io.Serializable;

import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_RUNNING;

public class ListElement implements Serializable {
    private String firstTeamName;
    private String secondTeamName;
    private String minute;
    private long statusId;
    private long utcStartTime;
    private String teamOneId;
    private String teamTwoId;

    public ListElement() {
    }

    public void setFirstTeamName(String firstTeamName){ this.firstTeamName = firstTeamName; }
    public String getFirstTeamName(){ return firstTeamName; }

    public void setSecondTeamName(String secondTeamName){ this.secondTeamName = secondTeamName; }
    public String getSecondTeamName(){ return secondTeamName; }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }
    // Check if event is running
    public boolean isRunning() {
        return ((this.statusId & STATUS_RUNNING.getFlag()) != 0);
    }

    public long getUtcStartTime() {
        return utcStartTime;
    }

    public void setUtcStartTime(long utcStartTime) {
        this.utcStartTime = utcStartTime;
    }

    public String getTeamOneId() {
        return teamOneId;
    }

    public void setTeamOneId(String teamOneId) {
        this.teamOneId = teamOneId;
    }

    public String getTeamTwoId() {
        return teamTwoId;
    }

    public void setTeamTwoId(String teamTwoId) {
        this.teamTwoId = teamTwoId;
    }
}
