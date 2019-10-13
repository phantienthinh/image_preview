package com.mgosu.imagepreview.ui.util;

import android.content.res.Resources;

public class DimenUtils {
    public static float dpToPx(int sizeInDp) {
        return sizeInDp * Resources.getSystem().getDisplayMetrics().density;
    }
    public static float spToPx(int sizeInDp) {
        return sizeInDp * Resources.getSystem().getDisplayMetrics().scaledDensity;
    }
}
