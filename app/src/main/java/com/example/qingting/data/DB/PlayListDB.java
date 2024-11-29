package com.example.qingting.data.DB;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.qingting.Bean.PlayList;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PlayListDB {
    static String idName = "id";
    static String userIdName = "user_id";
    static String nameName = "name";
    static String likesName = "likes";
    static String playTimesName = "play_times";
    static String tableName = "playlist";
    static String urlName = "url";
    static String createSQL = "create table if not exists " + tableName + "(" +
            String.format("%s integer primary key,", idName) +
            String.format("%s integer,", userIdName) +
            String.format("%s text,", nameName) +
            String.format("%s integer,", likesName) +
            String.format("%s integer,", playTimesName) +
            String.format("%s text", urlName) +
            ");";

    public static void insert(Context context, PlayList list) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getWritableDatabase();
        if (db == null) {
            return;
        }
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(idName, list.getId());
        contentValues.put(userIdName, list.getUserId());
        contentValues.put(nameName, list.getName());
        contentValues.put(likesName, list.getLikes());
        contentValues.put(playTimesName, list.getPlayTimes());
        contentValues.put(urlName, list.getUrl());
        try {
            db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(tableName, e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 插入数据库
     * @param context
     * @param list1
     * @return 返回影响条数
     */
    public static Integer insertList(Context context, List<PlayList> list1) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getWritableDatabase();
        if (db == null) {
            return 0;
        }
        int affectedNum = 0;
        db.beginTransaction();
        for (PlayList list: list1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(idName, list.getId());
            contentValues.put(userIdName, list.getUserId());
            contentValues.put(nameName, list.getName());
            contentValues.put(likesName, list.getLikes());
            contentValues.put(playTimesName, list.getPlayTimes());
            contentValues.put(urlName, list.getUrl());
            try {
                if (db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE) >= 0) {
                    affectedNum++;
                }
            } catch (SQLException e) {
                Log.e(tableName, e.getMessage());
            }
        }
        if (affectedNum > 0) {
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        return affectedNum;
    }


    public static List<PlayList> selectAll(Context context) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        if (db == null) {
            return new ArrayList<>();
        }
        String[] columns = new String[]{idName, userIdName, nameName, likesName, playTimesName, urlName};
        Cursor cursor = db.query(tableName, columns, null, null, null, null, null);
        List<PlayList> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    PlayList playList = new PlayList();
                    playList.setId(cursor.getInt(cursor.getColumnIndexOrThrow(idName)));
                    playList.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(userIdName)));
                    playList.setName(cursor.getString(cursor.getColumnIndexOrThrow(nameName)));
                    playList.setLikes(cursor.getInt(cursor.getColumnIndexOrThrow(likesName)));
                    playList.setPlayTimes(cursor.getInt(cursor.getColumnIndexOrThrow(playTimesName)));
                    playList.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(urlName)));
                    res.add(playList);
                } catch (SQLException e) {
                    Log.e(tableName, e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    public static List<PlayList> selectAllOrderByName(Context context) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        if (db == null) {
            return new ArrayList<>();
        }
        String[] columns = new String[]{idName, userIdName, nameName, likesName, playTimesName, urlName};
        Cursor cursor = db.query(tableName, columns, null, null, null, null, nameName);
        List<PlayList> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    PlayList playList = new PlayList();
                    playList.setId(cursor.getInt(cursor.getColumnIndexOrThrow(idName)));
                    playList.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(userIdName)));
                    playList.setName(cursor.getString(cursor.getColumnIndexOrThrow(nameName)));
                    playList.setLikes(cursor.getInt(cursor.getColumnIndexOrThrow(likesName)));
                    playList.setPlayTimes(cursor.getInt(cursor.getColumnIndexOrThrow(playTimesName)));
                    playList.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(urlName)));
                    res.add(playList);
                } catch (SQLException e) {
                    Log.e(tableName, e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    public static List<List<PlayList>> getPlayListListList(Context context) {
        List<PlayList> playListsDefault = PlayListDB.selectAll(context);
        List<PlayList> playListsOrderByName = PlayListDB.selectAllOrderByName(context);
        // 添加adapter
        List<List<PlayList>> playListListList = new ArrayList<>();
        playListListList.add(playListsDefault);
        playListListList.add(playListsOrderByName);
        return playListListList;
    }


    /**
     * 从数据库删除一个歌单
     * @param context
     * @param playList
     * @return 成功返回>0的数
     */
    public static int deletePlayList(Context context, PlayList playList) {
        if (playList == null || playList.getId() == null) {
            return 0;
        }
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        if (db == null) {
            return 0;
        }
        String where = String.format("%s = ?", idName);
        String[] args = new String[]{playList.getId().toString()};
        return db.delete(tableName, where, args);
    }
}
