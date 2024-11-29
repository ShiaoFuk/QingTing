package com.example.qingting.data.SP;

import static com.example.qingting.data.SP.Token.DEFAULT;
import static com.example.qingting.data.SP.Token.SPName;
import static com.example.qingting.data.SP.Token.TOKEN;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginSP {

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = SP.getShapedPreference(context, SPName);
        String token = sharedPreferences.getString(TOKEN, DEFAULT);
        return token;
    }

    public static void setToken(Context context, String token) {
        SharedPreferences.Editor editor = SP.getEditor(context, SPName);
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public static void clearToken(Context context) {
        SharedPreferences.Editor editor = SP.getEditor(context, SPName);
        editor.remove(TOKEN);
        editor.commit();
    }
}


class Token {
    final static String SPName = "Login";

    final static String TOKEN = "token";

    final static String DEFAULT = null;
}
