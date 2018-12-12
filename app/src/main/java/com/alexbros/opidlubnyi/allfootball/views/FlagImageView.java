package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.alexbros.opidlubnyi.allfootball.Colors;
import com.alexbros.opidlubnyi.allfootball.Constants;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.helpers.MemoryCacheCleaner;
import com.alexbros.opidlubnyi.allfootball.picasso.PicassoImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class FlagImageView extends PicassoImageView {
    public static void setCountry(final Context context, String country, final ImageView view) {
        String fileName = "";
        try {
            fileName = URLEncoder.encode(country.toLowerCase(Locale.US), "UTF-8").replace('%', '+');
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String url = Constants.MEDIA_URL + "assets/flags/128/" + fileName + ".png";

        try {
            if (view.getTag() != null && view.getTag().equals(Constants.BACKGROUND_IMAGEVIEW_TAG)) {
                getPicasso(context)
                        .load(url)
                        .fit()
                        .centerCrop()
                        .priority(Picasso.Priority.LOW)
                        .placeholder(new ColorDrawable(Colors.activityBackground))
                        .transform(new BlurTransform(context, view))
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(view, new Callback() {
                            @Override
                            public void onSuccess() {}

                            @Override
                            public void onError() {
                                getPicasso(context)
                                        .load(url)
                                        .fit()
                                        .centerCrop()
                                        .priority(Picasso.Priority.LOW)
                                        .placeholder(new ColorDrawable(Colors.activityBackground))
                                        .transform(new BlurTransform(context, view))
                                        .into(view);
                            }
                        });
            } else {
                getPicasso(context)
                        .load(url)
                        .priority(Picasso.Priority.HIGH)
                        .placeholder(R.drawable.no_flag)
                        .error(R.drawable.no_flag)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(view, new Callback() {
                            @Override
                            public void onSuccess() {}

                            @Override
                            public void onError() {
                                getPicasso(context)
                                        .load(url)
                                        .priority(Picasso.Priority.HIGH)
                                        .placeholder(R.drawable.no_flag)
                                        .error(R.drawable.no_flag)
                                        .into(view);
                            }
                        });
            }
        } catch (OutOfMemoryError e) {
            MemoryCacheCleaner.clean();
        }
    }
}
