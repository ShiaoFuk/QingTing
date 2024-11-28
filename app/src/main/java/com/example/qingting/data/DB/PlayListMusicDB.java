package com.example.qingting.data.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.qingting.Bean.Music;
import com.example.qingting.Bean.PlayList;
import com.example.qingting.Bean.PlayListMusic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 歌单和歌单音乐关系的数据库
 * 对这个数据库操作的时候，要同时对music数据库做同步操作
 */
public class PlayListMusicDB {
    static final String idName = "id";
    static final String playListIdName = "play_list_id";
    static final String musicIdName = "music_id";
    static final String tableName = "playlist_music";
    static String createSQL = "create table if not exists " + tableName + "(" +
            String.format("%s integer primary key autoincrement,", idName) +
            String.format("%s integer,", playListIdName) +
            String.format("%s integer,", musicIdName) +
            String.format("UNIQUE (%s, %s)", playListIdName, musicIdName) +
            ");";


    /**
     * 将音乐加入歌单的数据库中
     * @param context
     * @param playListId 歌单id
     * @param music 音乐对象
     */
    public static void insert(Context context, Integer playListId, Music music) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getWritableDatabase();
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(playListIdName, playListId);
        contentValues.put(musicIdName, music.getId());
        try {
            db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(tableName, e.getMessage());
        } finally {
            db.endTransaction();
        }
        MusicDB.insert(context, music);
    }

    /**
     * 将多个音乐加入歌单数据库
     * @param context
     * @param playListId 歌单id
     * @param musicList 音乐列表
     * @return 返回影响条数
     */
    public static Integer insertList(Context context, Integer playListId, List<Music> musicList) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getWritableDatabase();
        int affectedNum = 0;
        db.beginTransaction();
        for (Music music: musicList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(playListIdName, playListId);
            contentValues.put(musicIdName, music.getId());
            try {
                if (db.insertWithOnConflict(tableName, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE) >= 0) {
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
        MusicDB.insertList(context, musicList);
        return affectedNum;
    }


    /**
     * 获取某个歌单的所有音乐,多表查询
     * @param context
     * @return 音乐列表
     */
    private static List<Music> selectAll(Context context, Integer playListId) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        String query = String.format("select %s.* from %s, %s where %s.%s = %s.%s and %s.%s = %s",
                MusicDB.tableName, tableName, MusicDB.tableName, tableName, musicIdName, MusicDB.tableName, MusicDB.idName, tableName, playListIdName, playListId);
        Cursor cursor = db.rawQuery(query, null);
        List<Music> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    Music music = new Music();
                    music.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MusicDB.idName)));
                    music.setName(cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.nameName)));
                    music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.pathName)));
                    music.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.genreName)));
                    music.setTempo(cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.tempoName)));
                    res.add(music);
                } catch (SQLException e) {
                    Log.e(tableName, e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }

    private static List<Music> selectAllWithOrder(Context context, Integer playListId, String orderColumn) {
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        String selection = String.format("%s.%s = ?", tableName, playListIdName);
        String query = String.format("select %s.* from %s join %s on %s.%s = %s.%s where %s order by %s ",
                MusicDB.tableName, tableName, MusicDB.tableName, tableName, playListIdName, MusicDB.tableName, MusicDB.idName, selection, orderColumn);
        Cursor cursor = db.rawQuery(query, new String[] { playListId.toString() });
        List<Music> res = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                try {
                    Music music = new Music();
                    music.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MusicDB.idName)));
                    music.setName(cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.nameName)));
                    music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.pathName)));
                    music.setGenre(cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.genreName)));
                    music.setTempo(cursor.getString(cursor.getColumnIndexOrThrow(MusicDB.tempoName)));
                    res.add(music);
                } catch (SQLException e) {
                    Log.e(tableName, e.getMessage());
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;
    }


    /**
     * 获取歌单的所有音乐，按id排序
     * @param context
     * @param playListId 歌单id
     * @return 返回歌单的音乐列表
     */
    public static List<Music> getMusicListDefault(Context context, Integer playListId) {
        List<Music> musicList = PlayListMusicDB.selectAll(context, playListId);
        // 添加adapter
        return musicList;
    }


    /**
     * 获取歌单的所有音乐，按指定列排序
     * @param context
     * @param playListId 歌单id
     * @param orderColumn 要排序的列
     * @return 返回歌单的音乐列表
     */
    public static List<Music> getMusicListWithOrder(Context context, Integer playListId, String orderColumn) {
        List<Music> musicList = PlayListMusicDB.selectAllWithOrder(context, playListId, orderColumn);;
        // 添加adapter
        return musicList;
    }


    /**
     * 从歌单数据库删除一首歌
     * @param context
     * @param playListId 歌单的id
     * @param music 要删除的音乐
     * @return 成功返回>0的数
     */
    public static int deleteMusicFromPlayList(Context context, Integer playListId, Music music) {
        if (music == null || music.getId() == null) {
            return 0;
        }
        SQLiteDatabase db = MusicDBHelper.getInstance(context).getReadableDatabase();
        String where = String.format("%s = ?", idName);
        String[] args = new String[]{music.getId().toString()};
        MusicDB.deleteMusic(context, music);
        return db.delete(tableName, where, args);
    }


    /**
     * 从歌单数据库删除多首歌
     * @param context
     * @param playListId 歌单的id
     * @param musicList 要删除的音乐列表
     * @return 返回成功删除的条数
     */
    public static int deleteMusicsFromPlayList(Context context, Integer playListId, List<Music> musicList) {
        if (playListId == null || musicList == null || musicList.isEmpty()) {
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
        String where = String.format("%s = %s and %s IN (%s)", playListIdName, playListId.toString(), musicIdName, TextUtils.join(",", Collections.nCopies(ids.size(), "?")));
        String[] args = ids.toArray(new String[0]);

        SQLiteDatabase db = MusicDBHelper.getInstance(context).getWritableDatabase();
        db.delete(tableName, where, args);
        MusicDB.deleteMusicList(context, musicList);
        return db.delete(tableName, where, args);
    }
}
