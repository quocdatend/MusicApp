package com.example.musicapp.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.musicapp.models.Song;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Song_DAO {
    private SQLiteOpenHelper dbHelper;

    public Song_DAO(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void addMusicRecord(String name, String title, String duration, String thumbnailUrl,
                               String lyrics, String language, int idAlbum, int idSlink,
                               int idStyle, String linkMusic) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", name);
        values.put("TITLE", title);
        values.put("DURATION", duration);
        values.put("THUMBNAILURL", thumbnailUrl);
        values.put("LYRICS", lyrics);
        values.put("LANGUAGE", language);
        values.put("Link_Music", linkMusic);

        // Inserting Row
        db.insert("SONGS", null, values);
        db.close(); // Closing database connection
    }

    public List<Song> getAllMusicRecords() {
        List<Song> musicList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM SONGS";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Song musicItem = new Song(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TITLE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DURATION")),
                        cursor.getString(cursor.getColumnIndexOrThrow("THUMBNAILURL")),
                        cursor.getString(cursor.getColumnIndexOrThrow("LYRICS")),
                        cursor.getString(cursor.getColumnIndexOrThrow("LANGUAGE")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID_ALBUM")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID_SLINK")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID_STYLE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Link_Music"))
                );
                musicList.add(musicItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return musicList;
    }

    public List<Song> getAllMusicByAlbumId(int albumId) {
        List<Song> musicList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT s.* FROM SONGS s " +
                "JOIN ALBUM_SONG a ON s.ID = a.ID_SONG " +
                "WHERE a.ID_ALBUM = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(albumId)});
        if (cursor.moveToFirst()) {
            do {
                Song musicItem = new Song(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TITLE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DURATION")),
                        cursor.getString(cursor.getColumnIndexOrThrow("THUMBNAILURL")),
                        cursor.getString(cursor.getColumnIndexOrThrow("LYRICS")),
                        cursor.getString(cursor.getColumnIndexOrThrow("LANGUAGE")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID_ALBUM")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID_SLINK")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID_STYLE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Link_Music"))
                );
                musicList.add(musicItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return musicList;
    }

    public int deleteMusicRecord(int songId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "ID = ?";
        String[] whereArgs = new String[]{String.valueOf(songId)};

        // Deleting Row
        int rowsDeleted = db.delete("SONGS", whereClause, whereArgs);
        db.close(); // Closing database connection

        return rowsDeleted;
    }

    public void addMusicRecord(Song song) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", song.getName());
        values.put("TITLE", song.getTitle());
        values.put("DURATION", song.getDuration());
        values.put("THUMBNAILURL", song.getThumbnailUrl());
        values.put("LYRICS", song.getLyrics());
        values.put("LANGUAGE", song.getLanguage());
        values.put("ID_ALBUM", song.getIdAlbum());
        values.put("ID_SLINK", song.getIdSlink());
        values.put("ID_STYLE", song.getIdStyle());
        values.put("Link_Music", song.getLinkMusic());
        // Inserting Row
        db.insert("SONGS", null, values);
        db.close(); // Closing database connection
    }

    public int updateMusicRecord(Song song) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", song.getName());
        values.put("TITLE", song.getTitle());
        values.put("DURATION", song.getDuration());
        values.put("THUMBNAILURL", song.getThumbnailUrl());
        values.put("LYRICS", song.getLyrics());
        values.put("LANGUAGE", song.getLanguage());
        values.put("ID_ALBUM", song.getIdAlbum());
        values.put("ID_SLINK", song.getIdSlink());
        values.put("ID_STYLE", song.getIdStyle());
        values.put("Link_Music", song.getLinkMusic());

        String whereClause = "ID = ?";
        String[] whereArgs = new String[]{String.valueOf(song.getId())};

        // Updating Row
        int rowsUpdated = db.update("SONGS", values, whereClause, whereArgs);
        db.close(); // Closing database connection

        return rowsUpdated;
    }

    public boolean addfavMusic(int idUser, int idMusic) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_SONG", idMusic);
        values.put("ID_USER", idUser);
        // Thêm giá trị cho cột MARKED_DATETIME với thời gian hiện tại
        long currentTimeMillis = System.currentTimeMillis();
        String markedDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(currentTimeMillis));
        values.put("MARKED_DATETIMEE", markedDateTime);
        long result = db.insert("FAVORITES", null, values);
        db.close();
        System.out.println("llasdasjdlasd;alsdkl");
        return result != -1;
    }

    // Kiểm tra xem bài hát có trong danh sách yêu thích hay không
    public boolean checkfavMusic(int idUser, int idMusic) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "ID_USER = ? AND ID_SONG = ?";
        String[] selectionArgs = { String.valueOf(idUser), String.valueOf(idMusic) };

        Cursor cursor = db.query(
                "FAVORITES",         // Tên bảng
                new String[] {"ID_USER", "ID_SONG"}, // Các cột cần trả về
                selection,           // Tiêu chí lựa chọn
                selectionArgs,       // Tham số lựa chọn
                null,                // Group by
                null,                // Having
                null                 // Order by
        );
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }
    public boolean deleteFavorate_Song(int idUser, int idMusic) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete("FAVORITES", "ID_USER = ? AND ID_SONG = ? ",  new String[]{String.valueOf(idUser), String.valueOf(idMusic)});
        db.close();
        return rowsAffected > 0;
    }
}