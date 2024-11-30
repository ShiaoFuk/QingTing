package com.example.qingting.data.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qingting.Bean.PlayList;
import com.example.qingting.Bean.PlayListMusic;
import com.example.qingting.data.SP.LoginSP;

public class MusicDBHelper extends SQLiteOpenHelper {
    // 数据库名称和版本
    private static final String DATABASE_NAME = "QingTing.db";
    private static final int DATABASE_VERSION = 1;
    Context context;

    private MusicDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 需要登陆后才可以获取到数据库
     * @return 如果没有登陆返回空指针，登陆后才返回正确的实例
     */
    @Override
    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase db = super.getWritableDatabase();
        return db;
    }


    @Override
    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase db = super.getReadableDatabase();
        return db;
    }


    private static MusicDBHelper musicDBHelper;

    public static MusicDBHelper getInstance(Context context) {
        if (musicDBHelper == null) {
            musicDBHelper = new MusicDBHelper(context);
            musicDBHelper.context = context;
            SQLiteDatabase db = musicDBHelper.getWritableDatabase();  // 必要的，空调用一次来建表
            init(db);
        }
        musicDBHelper.context = context;
        return musicDBHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        init(db);
    }

    private static void init(SQLiteDatabase db) {
        db.execSQL(PlayListDB.createSQL);
        db.execSQL(MusicDB.createSQL);
        db.execSQL(PlayListMusicDB.createSQL);
    }


    /**
     * 清空所有数据库的所有行
     */
    public static void clearAllDB(Context context) {
        MusicDB.deleteAll(context);
        PlayListDB.deleteAll(context);
        PlayListMusicDB.deleteAll(context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // would not upgrade
    }
}
