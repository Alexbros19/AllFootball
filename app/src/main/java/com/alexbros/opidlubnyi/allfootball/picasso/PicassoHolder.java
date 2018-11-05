package com.alexbros.opidlubnyi.allfootball.picasso;

import android.content.Context;

import com.alexbros.opidlubnyi.allfootball.helpers.URLContentHelper;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class PicassoHolder {
    private static final long cacheMaxSize = 30000000;
    private static PicassoHolder instance;
    private Picasso picasso = null;
    private LruCache memoryCache = null;


    private PicassoHolder() {
    }

    //----------------------------------------------------------------------------------------------
    // Singleton
    //----------------------------------------------------------------------------------------------
    public static PicassoHolder getInstance() {
        if (PicassoHolder.instance == null) {
            PicassoHolder.instance = new PicassoHolder();
        }
        return PicassoHolder.instance;
    }

    public Picasso getPicasso(Context context) {
        PicassoHolder picassoHolder = getInstance();
        if (picassoHolder.picasso == null) {
            if (memoryCache == null)
                memoryCache = new LruCache(context);

            File cachePath = new File(context.getCacheDir(), "media");

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient
                    .setCache(new Cache(cachePath, cacheMaxSize))
                    .interceptors().add(new PicassoCustomInterceptor());
            Picasso.Builder builder = new Picasso.Builder(context);
            picassoHolder.picasso = builder.downloader(new OkHttpDownloader(okHttpClient)).memoryCache(memoryCache).build();

//            if (BuildConfig.ENABLE_DEV_SETTINGS)
//                picassoHolder.picasso.setIndicatorsEnabled(true);
        }

        return picassoHolder.picasso;
    }

    public void clearMemoryCache() {
        if (memoryCache == null)
            return;

        try {
            memoryCache.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class PicassoCustomInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request original = chain.request();
            final Request.Builder requestBuilder = original.newBuilder()
                    .header(URLContentHelper.getAllGoalsClientHeader(), URLContentHelper.getAllGoalsClientHeaderValue())
                    .method(original.method(), original.body());
            return chain.proceed(requestBuilder.build());
        }
    }

}
