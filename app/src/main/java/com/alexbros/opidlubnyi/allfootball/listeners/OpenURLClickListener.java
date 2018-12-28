package com.alexbros.opidlubnyi.allfootball.listeners;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.helpers.ActionHelper;

public class OpenURLClickListener implements OnClickListener {
    public static final int TAG_URL = R.id.eventDetailOddsTitle; // default tag url that rewrites value from Server
    private Context context;
//    private String analyticsCategory, analyticsAction, analyticsLabel;

    public OpenURLClickListener(Context context) {
        super();

        this.context = context;
//        this.analyticsCategory = analyticsCategory;
//        this.analyticsAction = analyticsAction;
//        this.analyticsLabel = analyticsLabel;
    }

    public void onClick(final View v) {
        String url = (String) v.getTag(TAG_URL);
        if (url == null)
            return;

        ActionHelper.view(context, url);

//        if (analyticsCategory != null && analyticsAction != null) {
//            if (analyticsLabel != null)
//                GoogleAnalytics.sendEvent(context, analyticsCategory, analyticsAction, analyticsLabel);
//            else
//                GoogleAnalytics.sendEvent(context, analyticsCategory, analyticsAction);
//        }
    }
}
