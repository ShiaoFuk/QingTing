package com.example.qingting.data.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qingting.Bean.PlayList;

public class MusicDBHelper extends SQLiteOpenHelper {
    // 数据库名称和版本
    private static final String DATABASE_NAME = "QingTing.db";
    private static final int DATABASE_VERSION = 1;


    private MusicDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static MusicDBHelper musicDBHelper;

    public static MusicDBHelper getInstance(Context context) {
        if (musicDBHelper == null) {
            musicDBHelper = new MusicDBHelper(context);
            musicDBHelper.getWritableDatabase();
        }
        return musicDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PlayListDB.createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // would not upgrade
    }
}
