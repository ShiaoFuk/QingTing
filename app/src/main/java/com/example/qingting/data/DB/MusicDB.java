package com.example.qingting.data.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.qingting.Bean.Music;
import com.example.qingting.Bean.Music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicDB {
    final static String idName = "id";
    final static String nameName = "name";
    final static String pathName = "path";
    final static String genreName = "genre";
    final static String tempoName = "tempo";
    final static String tableName = "music";
    static String createSQL = "create table if not exists " + tableName + "(" +
            String.format("%s integer primary key,", idName) +
            String.format("%s text,", nameName) +
            String.format("%s text,", pathName) +
            String.format("%s text,", genreName) +
            String.format("%s text", tempoName) +
            ");";

    // crud
    
    public static void insert(Context context, Music music) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(idName, music.getId());
        contentValues.put(nameName, music.getName());
        contentValues.put(genreName, music.getGenre());
        contentValues.put(pathName, music.getPath());
        contentValues.put(tempoName, music.getTempo());
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
    public static Integer insertList(Context context, List<Music> list1) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getWritableDatabase();
        int affectedNum = 0;
        db.beginTransaction();
        for (Music music: list1) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(idName, music.getId());
            contentValues.put(nameName, music.getName());
            contentValues.put(genreName, music.getGenre());
            contentValues.put(pathName, music.getPath());
            contentValues.put(tempoName, music.getTempo());
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


    private static List<Music> selectAll(Context context) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        String[] columns = new String[]{idName, nameName, pathName, genreName, tempoName};
        Cursor cursor = db.query(tableName, columns, null, null, null, null, null);
        List<Music> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    Music music = new Music();
                    music.setId(cursor.getInt(cursor.getColumnIndexOrThrow(idName)));
                    music.setName(cursor.getString(cursor.getColumnIndexOrThrow(nameName)));
                    music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(pathName)));
                    music.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(genreName)));
                    music.setTempo(cursor.getString(cursor.getColumnIndexOrThrow(tempoName)));
                    res.add(music);
                } catch (SQLException e) {
                    Log.e(tableName, e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    private static List<Music> selectAllOrderByName(Context context) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        String[] columns = new String[]{idName, nameName, pathName, genreName, tempoName};
        Cursor cursor = db.query(tableName, columns, null, null, null, null, nameName);
        List<Music> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    Music music = new Music();
                    music.setId(cursor.getInt(cursor.getColumnIndexOrThrow(idName)));
                    music.setName(cursor.getString(cursor.getColumnIndexOrThrow(nameName)));
                    music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(pathName)));
                    music.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(genreName)));
                    music.setTempo(cursor.getString(cursor.getColumnIndexOrThrow(tempoName)));
                    res.add(music);
                } catch (SQLException e) {
                    Log.e(tableName, e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    public static List<Music> getMusicListDefault(Context context) {
        List<Music> musicList = MusicDB.selectAll(context);
        // 添加adapter
        return musicList;
    }

    public static List<Music> getMusicListOrderByName(Context context) {
        List<Music> musicList = MusicDB.selectAllOrderByName(context);
        // 添加adapter
        return musicList;
    }


    /**
     * 从数据库删除一首歌
     * @param context
     * @param music
     * @return 成功返回>0的数
     */
    public static int deleteMusic(Context context, Music music) {
        if (music == null || music.getId() == null) {
            return 0;
        }
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        String where = String.format("%s = ?", idName);
        String[] args = new String[]{music.getId().toString()};
        return db.delete(tableName, where, args);
    }


    /**
     * 从数据库删除多首歌
     * @param context
     * @param musicList
     * @return 返回成功删除的条数
     */
    public static int deleteMusicList(Context context, List<Music> musicList) {
        if (musicList == null || musicList.isEmpty()) {
            return 0;
        }

        // 创建一个包含所有 ID 的字符串数组
        List<String> ids = new ArrayList<>();
        for (Music music : musicList) {
            if (music.getId() != null) {
                ids.add(music.getId().toString());
            }
        }

        // 如果没有有效的 ID，则不执行删除
        if (ids.isEmpty()) {
            return 0;
        }

        // 使用 ? 来动态构造 IN 子句的占位符
        String where = String.format("%s IN (%s)", idName, TextUtils.join(",", Collections.nCopies(ids.size(), "?")));
        String[] args = ids.toArray(new String[0]);

        SQLiteDatabase db = MusicDBHelper.getInstance(context).getWritableDatabase();
        return db.delete(tableName, where, args);
    }


    /**
     * app会向远程数据库查询歌单包含的音乐，加入到本地数据库 但是如果远程删除了数据库，本地查询的时候并不会删除，因此每次启动app需要手动删除不在歌单里面的音乐避免占用过多
     * @param context
     * @param music
     * @return
     */
    public static void deleteUnlessMusic(Context context, Music music) {
        if (music == null || music.getId() == null) {
            return;
        }
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        String deleteSql = String.format("delete from %s where %s not in (select %s from %s)", tableName, idName, PlayListMusicDB.musicIdName, PlayListMusicDB.tableName);
        db.execSQL(deleteSql);
    }


}
