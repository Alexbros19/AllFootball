package com.alexbros.opidlubnyi.allfootball.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class League implements Serializable {
    @Expose() @SerializedName("id")
    public String id = "";

    @Expose() @SerializedName("name")
    public String name = "";

    @Expose() @SerializedName("wikiLang")
    public String wikipediaPageLanguage = "";

    @Expose() @SerializedName("wiki")
    public String wikipediaPage = "";

    @Expose() @SerializedName("hasWiki")
    public boolean hasWikipediaPage = false;

    @Expose() @SerializedName("country")
    public String country = "";

    @Expose() @SerializedName("countryLocalized")
    public String countryLocalized = "";

    @Expose() @SerializedName("playoffType")
    public int playoffType = 0;

    public String day = "";
    public ArrayList<Event> events = new ArrayList<>();
    public boolean isPopular = false;
    public int popularOrder = 0;

    public boolean isFeaturedInCountry = false;
    public boolean isFeatured = false;
    public int priorityInCountry = 0;

    public boolean noLive = false;

    private static final int DEFAULT_MAX_SORTING_ORDER = 999999;

    public League() {
    }

//    public League(HistoricEvent historicEvent) {
//        name = historicEvent.league;
//        country = historicEvent.country;
//        countryLocalized = historicEvent.countryLocalized;
//    }

    public String getTitle() {
        return country + " - " + name;
    }

    public String getLocalizedTitle() {
        return  countryLocalized + " - " + name;
    }

    public League copy() {
        League newLeague = new League();
        newLeague.merge(this);

//        for (Event event : events)
//            newLeague.events.add(event.copy());

        return newLeague;
    }

    public void merge(League c) {
        id = c.id;
        name = c.name;
        wikipediaPageLanguage = c.wikipediaPageLanguage;
        wikipediaPage = c.wikipediaPage;
        hasWikipediaPage = c.hasWikipediaPage;
        country = c.country;
        countryLocalized = c.countryLocalized;
        playoffType = c.playoffType;
        day = c.day;
        isPopular = c.isPopular;
        popularOrder = c.popularOrder;
        isFeaturedInCountry = c.isFeaturedInCountry;
        isFeatured = c.isFeatured;
        priorityInCountry = c.priorityInCountry;
        noLive = c.noLive;
    }

    public boolean hasPlayoff() {
        return (playoffType == 1) || (playoffType == 2);
    }

    private static int getSortingOrder(HashMap<String, Integer> dictionary, String id) {
        Integer order = dictionary.get(id);
        if (order == null)
            order = DEFAULT_MAX_SORTING_ORDER;

        return order;
    }

    private static int orderComparatorHelper(HashMap<String, Integer> dictionary, League o1, League o2) {
        if (dictionary.isEmpty())
            return 0;

        // move Leagues without Ids to the end of list
        if (o1.id.isEmpty() && !o2.id.isEmpty())
            return +1;
        else if (!o1.id.isEmpty() && o2.id.isEmpty())
            return -1;

        final int firstAllOrder = getSortingOrder(dictionary, o1.id);
        final int secondAllOrder = getSortingOrder(dictionary, o2.id);

        if (firstAllOrder > secondAllOrder)
            return +1;
        else if (firstAllOrder < secondAllOrder)
            return -1;
        else
            return 0;
    }

    public static final class CustomOrderComparator implements Comparator<League> {
        private HashMap<String, Integer> dictionary;

        public CustomOrderComparator(HashMap<String, Integer> dictionary) {
            this.dictionary = dictionary;
        }

        @Override
        public int compare(League o1, League o2) {
            return orderComparatorHelper(dictionary, o1, o2);
        }
    }

//    public static final class OrderComparator implements Comparator<League> {
//        private ModelData model;
//
//        public OrderComparator() {
//            model = ModelData.getInstance();
//        }
//
//        @Override
//        public int compare(League o1, League o2) {
//            return orderComparatorHelper(model.allLeaguesOrder, o1, o2);
//        }
//    }

//    public static final class FavouriteOrderComparator implements Comparator<League> {
//        private ModelData model;
//
//        public FavouriteOrderComparator() {
//            model = ModelData.getInstance();
//        }
//
//        @Override
//        public int compare(League o1, League o2) {
//            return orderComparatorHelper(model.favouriteLeaguesOrder, o1, o2);
//        }
//    }

    public static final class PopularOrderComparator implements Comparator<League> {
        @Override
        public int compare(League o1, League o2) {
            return o1.popularOrder > o2.popularOrder ? +1 : o1.popularOrder < o2.popularOrder ? -1 : 0;
            //return o2.prioritization.compareTo(o1.prioritization);
        }
    }

    public static final class DefaultOrderComparator implements Comparator<League> {
        @Override
        public int compare(League league, League otherLeague) {
            int compareValue = 0;
            if (league.isFeaturedInCountry && !otherLeague.isFeaturedInCountry) {
                compareValue = -1;
            } else if (!league.isFeaturedInCountry && otherLeague.isFeaturedInCountry) {
                compareValue = 1;
            }
            if (compareValue == 0) {
                if (league.isFeatured && !otherLeague.isFeatured) {
                    compareValue = -1;
                } else if (!league.isFeatured && otherLeague.isFeatured) {
                    compareValue = 1;
                }
            }
            if (compareValue == 0)
                compareValue = league.country.toLowerCase().compareTo(otherLeague.country.toLowerCase());
            if (compareValue == 0)
                compareValue = league.priorityInCountry > otherLeague.priorityInCountry ? +1 : league.priorityInCountry < otherLeague.priorityInCountry ? -1 : 0;
            if (compareValue == 0)
                compareValue = league.name.toLowerCase().compareTo(otherLeague.name.toLowerCase());
            return compareValue;
        }
    }
}
