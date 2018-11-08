package com.alexbros.opidlubnyi.allfootball;

public class Constants {
    public static final String BUTTONS_CONTEXT_ACTIVITY_PARAM = "element_number";
    public static final String TODAY_RESPONSE = "https://server.thelivescoreapp.com/api/v1/android/events/getOverview?day=0&offset=7200&language=uk&timestamp=0";
    public static final String YESTERDAY_RESPONSE = "https://server.thelivescoreapp.com/api/v1/android/events/getOverview?day=-1&offset=7200&language=uk&timestamp=0";
    public static final String TOMORROW_RESPONSE = "https://server.thelivescoreapp.com/api/v1/android/events/getOverview?day=1&offset=7200&language=uk&timestamp=0";
    public static final String MEDIA_URL = "https://media.thelivescoreapp.com/";

    public static final int RESULT_EXCEPTION = 1;
    public static final int RESULT_OK = 0;

    public static final String BROADCAST_ACTION_RELOAD_SCORES = "BROADCAST_ACTION_RELOAD_SCORES";
    public static final String BROADCAST_ACTION_REDRAW_SCORES = "BROADCAST_ACTION_REDRAW_SCORES";

    public static String BACKGROUND_IMAGEVIEW_TAG = "background";
}
