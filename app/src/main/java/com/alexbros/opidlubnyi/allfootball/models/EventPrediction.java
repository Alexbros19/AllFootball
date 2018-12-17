package com.alexbros.opidlubnyi.allfootball.models;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EventPrediction {
    public boolean hasAlreadyPredicted = false;
    public ArrayList<EventPredictionMessage> messages = new ArrayList<EventPredictionMessage>();

    public int votes1 = 0;
    public double votes1Percentage = 0;
    public String votes1PercentageString = "";

    public int votes2 = 0;
    public double votes2Percentage = 0;
    public String votes2PercentageString = "";

    public int votesX = 0;
    public double votesXPercentage = 0;
    public String votesXPercentageString = "";

    public String teamOneLocalized = "";
    public String teamTwoLocalized = "";

    public void setVotes(int votes1, int votes2, int votesX) {
        this.votes1 = votes1;
        this.votes2 = votes2;
        this.votesX = votesX;

        float total = votes1 + votes2 + votesX;

        if (total > 0) {
            DecimalFormat df = new DecimalFormat("#.##");

            this.votes1Percentage = (votes1 * 100.0d) / total;
            this.votes2Percentage = (votes2 * 100.0d) / total;
            this.votesXPercentage = (votesX * 100.0d) / total;

            this.votes1PercentageString = df.format(votes1Percentage);
            this.votes2PercentageString = df.format(votes2Percentage);
            this.votesXPercentageString = df.format(votesXPercentage);
        }
    }

    public void increaseVotes1() {
        votes1++;
        setVotes(votes1, votes2, votesX);
    }

    public void increaseVotes2() {
        votes2++;
        setVotes(votes1, votes2, votesX);
    }

    public void increaseVotesX() {
        votesX++;
        setVotes(votes1, votes2, votesX);
    }
}
