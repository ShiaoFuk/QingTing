package com.example.qingting.utils;

public class TimeUtils {
    public static String milliSecs2MMss(int milli) {
        milli = milli / 1000;
        int minute = milli / 60;
        return String.format("%d:%02d", minute, milli % 60);
    }
}
