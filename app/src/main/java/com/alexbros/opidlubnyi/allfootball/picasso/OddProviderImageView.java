package com.alexbros.opidlubnyi.allfootball.picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.alexbros.opidlubnyi.allfootball.config.ConfigManager;
import com.alexbros.opidlubnyi.allfootball.helpers.MemoryCacheCleaner;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class OddProviderImageView extends PicassoImageView {
    public interface LoadListener {
        void onSuccess();
        void onError();
    }

    @SuppressLint("NewApi")
    public static void setOddProviderImage(final Context context, final String channelImg, final ImageView view, final LoadListener loadListener) {
        final String url = ConfigManager.getInstance().config().baseApiURL + "oddproviders/logo?id=" + channelImg;

        try {
            getPicasso(context)
                    .load(url)
                    .priority(Picasso.Priority.HIGH)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(view, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (loadListener != null)
                                loadListener.onSuccess();
                        }

                        @Override
                        public void onError() {
                            if (loadListener != null)
                                loadListener.onError();

                            getPicasso(context)
                                    .load(url)
                                    .priority(Picasso.Priority.HIGH)
                                    .into(view, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            if (loadListener != null)
                                                loadListener.onSuccess();
                                        }

                                        @Override
                                        public void onError() {
                                            if (loadListener != null)
                                                loadListener.onError();
                                        }
                                    });
                        }
                    });
        } catch (OutOfMemoryError ignored) {
            MemoryCacheCleaner.clean();
        }
    }
}
