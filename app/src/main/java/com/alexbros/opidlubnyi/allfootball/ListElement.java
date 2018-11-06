package com.alexbros.opidlubnyi.allfootball;

import java.io.Serializable;

import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_FINISHED;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_RUNNING;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_UPCOMING;

public class ListElement implements Serializable {
    private String firstTeamName = "";
    private String secondTeamName = "";
    private String minute = "";
    private long statusId = 0;
    private long utcStartTime = 0;
    private String teamOneId = "";
    private String teamTwoId = "";
    private int teamOneGoals = 0;
    private int teamTwoGoals = 0;
    private String teamOneGoalsString = "";
    private String teamTwoGoalsString = "";

    public boolean running;
    public boolean finished;
    public boolean upcoming;

    ListElement() {
    }

    // Check if event is running
    boolean isRunning() {
        return ((this.statusId & STATUS_RUNNING.getFlag()) != 0);
    }

    public boolean isFinished() {
        return ((this.statusId & STATUS_FINISHED.getFlag()) != 0);
    }

    public boolean isUpcoming() {
        return ((this.statusId & STATUS_UPCOMING.getFlag()) != 0);
    }

    void setFirstTeamName(String firstTeamName) {
        this.firstTeamName = firstTeamName;
    }

    String getFirstTeamName() {
        return firstTeamName;
    }

    void setSecondTeamName(String secondTeamName) {
        this.secondTeamName = secondTeamName;
    }

    String getSecondTeamName() {
        return secondTeamName;
    }

    void setMinute(String minute) {
        this.minute = minute;
    }

    void setStatusId(long statusId) {
        this.statusId = statusId;
    }


    long getUtcStartTime() {
        return utcStartTime;
    }

    void setUtcStartTime(long utcStartTime) {
        this.utcStartTime = utcStartTime;
    }

    String getTeamOneId() {
        return teamOneId;
    }

    void setTeamOneId(String teamOneId) {
        this.teamOneId = teamOneId;
    }

    String getTeamTwoId() {
        return teamTwoId;
    }

    void setTeamTwoId(String teamTwoId) {
        this.teamTwoId = teamTwoId;
    }

    public int getTeamOneGoals() {
        return teamOneGoals;
    }

    void setTeamOneGoals(int teamOneGoals) {
        this.teamOneGoals = teamOneGoals;
    }

    public int getTeamTwoGoals() {
        return teamTwoGoals;
    }

    void setTeamTwoGoals(int teamTwoGoals) {
        this.teamTwoGoals = teamTwoGoals;
    }

    void setTeamOneGoalsString(String teamOneGoalsString) {
        this.teamOneGoalsString = teamOneGoalsString;
    }

    public void setTeamTwoGoalsString(String teamTwoGoalsString) {
        this.teamTwoGoalsString = teamTwoGoalsString;
    }

    public String getTeamOneGoalsString() {
        return teamOneGoalsString;
    }

    public String getTeamTwoGoalsString() {
        return teamTwoGoalsString;
    }
}
