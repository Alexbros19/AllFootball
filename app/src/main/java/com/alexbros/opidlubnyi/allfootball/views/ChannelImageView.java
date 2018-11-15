package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.helpers.ActionHelper;

import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.FACEBOOK;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.INSTAGRAM;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.TWITTER;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.VIBER;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.WEB_URL;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.WIKIPEDIA;
import static com.alexbros.opidlubnyi.allfootball.enums.SocialChannelsEnum.YOUTUBE;

public class ChannelImageView extends android.support.v7.widget.AppCompatImageView {
    private Context context;

    public ChannelImageView(final Context context, String channel, final String url) {
        super(context);
        this.context = context;
        setChannelBackground(channel);
        OnClickListener socialChannelClickListener = v -> ActionHelper.view(context, url);
        setOnClickListener(socialChannelClickListener);
    }

    public ChannelImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setChannelBackground(String channel) {

        if (channel.equals(FACEBOOK.getChannelString())) {
            setImageResource(R.drawable.facebook_icon);
        } else if (channel.equals(INSTAGRAM.getChannelString())) {
            setImageResource(R.drawable.instagram_icon);
        } else if (channel.equals(TWITTER.getChannelString())) {
            setImageResource(R.drawable.twitter_icon);
        } else if (channel.equals(VIBER.getChannelString())) {
            setImageResource(R.drawable.viber_icon);
        } else if (channel.equals(WEB_URL.getChannelString())) {
            setImageResource(R.drawable.web_icon);
        } else if (channel.equals(WIKIPEDIA.getChannelString())) {
            setImageResource(R.drawable.wikipedia_icon);
        } else if (channel.equals((YOUTUBE.getChannelString()))) {
            setImageResource(R.drawable.youtube_icon);
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.getLayoutParams();
        params.width = (int)context.getResources().getDimension(R.dimen.channel_image_view_width);
        params.height = (int)context.getResources().getDimension(R.dimen.channel_image_view_height);
        params.setMargins(0, 0, (int) context.getResources().getDimension(R.dimen.channel_image_view_margin_right), 0);
        this.setLayoutParams(params);
    }
}
