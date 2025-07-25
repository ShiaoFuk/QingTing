package com.example.qingting.utils;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.widget.ImageView;

public class TintUtils {
    public static void setImageViewTint(ImageView view, int color) {
        view.setImageTintList(ColorStateList.valueOf(color));
        view.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
    }

}
