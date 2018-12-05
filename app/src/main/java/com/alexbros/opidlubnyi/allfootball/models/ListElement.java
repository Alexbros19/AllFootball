package com.alexbros.opidlubnyi.allfootball.models;

import java.io.Serializable;
import java.util.Comparator;

import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_CANCELLED;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_FINISHED;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_FIRST_HALF;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_HALF_TIME;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_RUNNING;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_SECOND_HALF;
import static com.alexbros.opidlubnyi.allfootball.enums.EventStatusEnum.STATUS_UPCOMING;

public class ListElement implements Serializable {
    private String firstTeamName = "";
    private String secondTeamName = "";
    private String minute = "";
    private long statusId = 0;
    public String status = "";
    private long utcStartTime = 0;
    private String firstTeamId = "";
    private String secondTeamId = "";
    private int firstTeamGoals = 0;
    private int secondTeamGoals = 0;
    private String firstTeamGoalsString = "";
    private String secondTeamGoalsString = "";

    public boolean running;
    public boolean finished;
    public boolean upcoming;
    public boolean canceled;
    public boolean halftime;
    public boolean firstHalftime;
    public boolean secondHalftime;

    public ListElement() {
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

    public void setFirstTeamName(String firstTeamName) {
        this.firstTeamName = firstTeamName;
    }

    public String getFirstTeamName() {
        return firstTeamName;
    }

    public void setSecondTeamName(String secondTeamName) {
        this.secondTeamName = secondTeamName;
    }

    public String getSecondTeamName() {
        return secondTeamName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public long getStatusId() {
        return statusId;
    }

    public Long getUtcStartTime() {
        return utcStartTime;
    }

    public void setUtcStartTime(Long utcStartTime) {
        this.utcStartTime = utcStartTime;
    }

    public String getFirstTeamId() {
        return firstTeamId;
    }

    public void setFirstTeamId(String firstTeamId) {
        this.firstTeamId = firstTeamId;
    }

    public String getSecondTeamId() {
        return secondTeamId;
    }

    public void setSecondTeamId(String secondTeamId) {
        this.secondTeamId = secondTeamId;
    }

    public int getFirstTeamGoals() {
        return firstTeamGoals;
    }

    public void setFirstTeamGoals(int firstTeamGoals) {
        this.firstTeamGoals = firstTeamGoals;
    }

    public int getSecondTeamGoals() {
        return secondTeamGoals;
    }

    public void setSecondTeamGoals(int secondTeamGoals) {
        this.secondTeamGoals = secondTeamGoals;
    }

    public void setFirstTeamGoalsString(String firstTeamGoalsString) {
        this.firstTeamGoalsString = firstTeamGoalsString;
    }

    public void setSecondTeamGoalsString(String secondTeamGoalsString) {
        this.secondTeamGoalsString = secondTeamGoalsString;
    }

    public String getFirstTeamGoalsString() {
        return firstTeamGoalsString;
    }

    public String getSecondTeamGoalsString() {
        return secondTeamGoalsString;
    }

    public String getMinute() {
        return minute;
    }

    public static final class TimeOrderComparator implements Comparator<ListElement> {
        public TimeOrderComparator() {}

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
