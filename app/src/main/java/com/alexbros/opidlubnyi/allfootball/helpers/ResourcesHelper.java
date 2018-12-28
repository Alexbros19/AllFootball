package com.alexbros.opidlubnyi.allfootball.helpers;

import android.content.Context;
import android.content.res.TypedArray;

import com.alexbros.opidlubnyi.allfootball.R;

public class ResourcesHelper {
    public static int getStyledDrawableResourceId(Context context, int attrResourceId) {
        int resourceId;

//        final boolean isLightTheme = PreferenceHelper.isAppThemeWhite(context);

        TypedArray tempTypedArray = null;
        try {
            tempTypedArray = context.getTheme().obtainStyledAttributes(R.style.TheLivescoreAppStyle_White_white, new int[] {attrResourceId});
            resourceId = tempTypedArray.getResourceId(0, 0);
        } finally {
            if (tempTypedArray != null)
                tempTypedArray.recycle();
        }

        return resourceId;
    }
}
