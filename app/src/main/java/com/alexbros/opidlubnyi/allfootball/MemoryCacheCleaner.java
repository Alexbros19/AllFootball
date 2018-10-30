package com.alexbros.opidlubnyi.allfootball;

import com.alexbros.opidlubnyi.allfootball.picasso.PicassoHolder;

public class MemoryCacheCleaner {
    public static void clean() {
        try {
            PicassoHolder.getInstance().clearMemoryCache();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError ignored) {
        }

        System.gc();
    }
}
