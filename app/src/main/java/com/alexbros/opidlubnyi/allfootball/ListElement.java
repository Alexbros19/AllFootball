package com.alexbros.opidlubnyi.allfootball;

import java.io.Serializable;
import java.util.Comparator;

import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_CANCELLED;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_FINISHED;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_FIRST_HALF;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_HALF_TIME;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_RUNNING;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_SECOND_HALF;
import static com.alexbros.opidlubnyi.allfootball.EventStatusEnum.STATUS_UPCOMING;

public class ListElement implements Serializable {
    private String firstTeamName = "";
    private String secondTeamName = "";
    private String minute = "";
    private long statusId = 0;
    public String status = "";
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
    public boolean canceled;
    public boolean halftime;
    public boolean firstHalftime;
    public boolean secondHalftime;

    ListElement() {
    }

    // Check if event is running
    public boolean isRunning() {
        return ((this.statusId & STATUS_RUNNING.getFlag()) != 0);
    }

    public boolean isFinished() {
        return ((this.statusId & STATUS_FINISHED.getFlag()) != 0);
    }

    public boolean isUpcoming() {
        return ((this.statusId & STATUS_UPCOMING.getFlag()) != 0);
    }

    public boolean isCancelled() {
        return ((this.statusId & STATUS_CANCELLED.getFlag()) != 0);
    }

    public boolean isHalftime() {
        return ((this.statusId & STATUS_HALF_TIME.getFlag()) != 0);
    }

    public boolean isFirstHalftime() {
        return ((this.statusId & STATUS_FIRST_HALF.getFlag()) != 0);
    }

    public boolean isSecondHalftime() {
        return ((this.statusId & STATUS_SECOND_HALF.getFlag()) != 0);
    }

    public String getFormattedMinute() {
        if (((statusId & STATUS_FIRST_HALF.getFlag()) != 0) && (45 - Integer.parseInt(minute) < 0)) {
            return 45 + "+" + (Integer.parseInt(minute) - 45) + "'";
        } else if (((statusId & STATUS_SECOND_HALF.getFlag()) != 0) && (90 - Integer.parseInt(minute) < 0)) {
            return 90 + "+" + (Integer.parseInt(minute) - 90) + "'";
        }
        return minute + "'";
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

    void setStatus(String status) {
        this.status = status;
    }

    String getStatus() {
        return status;
    }

    void setMinute(String minute) {
        this.minute = minute;
    }

    void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public long getStatusId() {
        return statusId;
    }

    public Long getUtcStartTime() {
        return utcStartTime;
    }

    void setUtcStartTime(Long utcStartTime) {
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

    public String getMinute() {
        return minute;
    }

    public static final class TimeOrderComparator implements Comparator<ListElement> {
        TimeOrderComparator() {}

        @Override
        public int compare(ListElement o1, ListElement o2) {
            if (o1.utcStartTime > o2.utcStartTime)
                return +1;
            else if (o1.utcStartTime < o2.utcStartTime)
                return -1;
            else
                return 0;
        }
    }
}
