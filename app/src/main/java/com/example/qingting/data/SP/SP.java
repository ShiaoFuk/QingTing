package com.example.qingting.data.SP;

import android.content.Context;
import android.content.SharedPreferences;

public class SP {
    public static SharedPreferences getShapedPreference(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context, String name) {
        return getShapedPreference(context, name).edit();
    }
}
