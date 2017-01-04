package com.webssa.examples.fragmframework;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;


public class AppUtils {
    @SuppressLint("NewApi")
    public static int getColorFromResourcesCompat(Resources res, int resId, @Nullable Resources.Theme theme) {
        return (Build.VERSION.SDK_INT >= 23) ? res.getColor(resId, theme) : res.getColor(resId);
    }

    public static void setBackgroundColorForViewCompat(View v, int color) {
        Drawable dr = new ColorDrawable(color);
        if (Build.VERSION.SDK_INT >= 16) {
            v.setBackground(dr);
        } else {
            v.setBackgroundDrawable(dr);
        }
    }
}
