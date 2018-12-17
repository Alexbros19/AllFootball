package com.alexbros.opidlubnyi.allfootball.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppConfig {
    public Maintenance maintenance = new Maintenance();

    public String baseApiURL = "";
    public String baseMediaURL = "";
    public String baseUserPictureURL = "";

//    public String updateURL = "";

    public Features features = new Features();
    public Trackers trackers = new Trackers();

    //----------------------------------------------------------------------------------------------

    public AppConfig() {
    }

    //----------------------------------------------------------------------------------------------

    public static class Trackers {
        public GoogleAnalytics googleAnalytics = new GoogleAnalytics();
        public FirebaseAnalytics firebaseAnalytics = new FirebaseAnalytics();

        public static class GoogleAnalytics {
            public String minVersion = "";
            public int throttleTo = 19;

            public HashMap<String, DisabledCategory> disabledCategories = new HashMap<>();

            public static class DisabledCategory {
                public List<String> actions = new ArrayList<>();
            }
        }

        public static class FirebaseAnalytics {
            public boolean enabled = true;
            public String minVersion = "";
        }
    }

    //----------------------------------------------------------------------------------------------

    public class Maintenance {
        public boolean active = false;
        public String message = "";
    }

    //----------------------------------------------------------------------------------------------

    public class Features {
        public Configuration configuration = new Configuration();
        public EventDetails eventDetails = new EventDetails();
        public Logos logos = new Logos();
        public Watchlist watchlist = new Watchlist();
        public BottomBar bottomBar = new BottomBar();
        public RemoveAds removeAds = new RemoveAds();
        public Videos videos = new Videos();

        public class Configuration {
            public static final int DEFAULT_UPDATE_PERIOD = 5 * 60;
            public int updatePeriod = DEFAULT_UPDATE_PERIOD;
        }

        public class Logos {
            public boolean getTeamLogos = true;
            public boolean getPlayerPics = true;
            public boolean getUserPics = true;
        }

        public class EventDetails {
            public boolean showChatTab = true;
            public boolean showOddsTab = true;
        }

        public class Watchlist {
            public static final int DEFAULT_UPDATE_PERIOD = 5 * 60;
            public int updatePeriod = DEFAULT_UPDATE_PERIOD;
            public boolean addTeamsByDefault = true;
            public boolean addLeaguesByDefault = false;
        }

        public class BottomBar {
            public boolean showTip = false;
            public float installByDefault = 0.0f;
        }

        public class RemoveAds {
            public boolean enabled = true;
        }

        public class Videos {
            public EventVideos eventVideos = new EventVideos();

            public class EventVideos {
                public boolean openInExternalApp = true;
            }
        }
    }
}
