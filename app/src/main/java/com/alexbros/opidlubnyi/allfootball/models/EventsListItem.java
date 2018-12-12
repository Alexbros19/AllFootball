package com.alexbros.opidlubnyi.allfootball.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EventsListItem implements Serializable {
    public List<EventsListLeagues> leagues = new ArrayList<>();

    public static class EventsListLeagues {
        public List<EventsListLeague> leagues = new ArrayList<>();
    }

    public static class EventsListLeague {
        public League league = null;
        public List<EventsListEvent> events = new ArrayList<>();
    }

    public static class EventsListEvent {
        public Event event = null;
    }

    public static final class TimeOrderComparator implements Comparator<EventsListEvent> {
        public TimeOrderComparator() {}

        @Override
        public int compare(EventsListEvent o1, EventsListEvent o2) {
            return Long.compare(o1.event.getUtcStartTime(), o2.event.getUtcStartTime());
        }
    }
}
