package com.alexbros.opidlubnyi.allfootball.enums;

public enum SocialChannelsEnum {
    WEB_URL("webURL")
    , YOUTUBE("youtube")
    , TWITTER("twitter")
    , INSTAGRAM("instagram")
    , FACEBOOK("facebook")
    , VIBER("viber")
    , WIKIPEDIA("wikipedia")
    ;

    private String channel;

    SocialChannelsEnum(String channel) {
        this.channel = channel;
    }

    public String getChannelString() {
        return channel;
    }
}
