package com.example.musicapp.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.musicapp.models.Song;
import com.example.musicapp.models.Style;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StyleDAO {
    private SQLiteOpenHelper dbHelper;

    public StyleDAO(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Style> getAllStyles() throws SQLException {
        List<Style> styles = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM STYLE_SONG";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Style musicItem = new Style(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DETAIL"))
                );
                styles.add(musicItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return styles;
    }

    public boolean addStyle(Style style) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", style.getName());
        values.put("DETAIL", style.getDetail());

        long result = db.insert("STYLE_SONG", null, values);
        db.close();
        return result != -1; // Returns true if insert was successful, false otherwise
    }

    public boolean updateStyle(Style style) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NAME", style.getName());
        values.put("DETAIL", style.getDetail());

        int rowsAffected = db.update("STYLE_SONG", values, "ID = ?", new String[]{String.valueOf(style.getId())});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteStyle(int styleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete("STYLE_SONG", "ID = ?", new String[]{String.valueOf(styleId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean addSongStyle(int songId, int styleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_SONG", songId);
        values.put("ID_STYLE", styleId);
        long result = db.insert("SONGS_STYLES", null, values);
        db.close();
        return result != -1;
    }

    public List<Style> getStylesBySongId(int songId) {
        List<Style> musicList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT s.* FROM STYLE_SONG s " +
                "JOIN SONGS_STYLES a ON s.ID = a.ID_STYLE " +
                "WHERE a.ID_SONG = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(songId)});
        if (cursor.moveToFirst()) {
            do {
                Style musicItem = new Style(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("DETAIL"))
                );
                musicList.add(musicItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return musicList;
    }

    public boolean deleteStyle_Song(int styleId, int songId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete("SONGS_STYLES", "ID_STYLE = ? AND ID_SONG = ? ",  new String[]{String.valueOf(styleId), String.valueOf(songId)});
        db.close();
        return rowsAffected > 0;
    }
    public List<Song> getAllSongsByStyleId(int styleId) {
        List<Song> songs = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            String query = "SELECT s.* FROM SONGS s " +
                    "INNER JOIN SONGS_STYLES ss ON s.ID = ss.ID_SONG " +
                    "WHERE ss.ID_STYLE = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(styleId)});
            if (cursor.moveToFirst()) {
                do {
                    Song song = new Song();
                    song.setId(cursor.getInt(cursor.getColumnIndexOrThrow("ID")));
                    song.setName(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
                    song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("TITLE")));
                    song.setDuration(cursor.getString(cursor.getColumnIndexOrThrow("DURATION")));
                    song.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow("THUMBNAILURL")));
                    song.setLyrics(cursor.getString(cursor.getColumnIndexOrThrow("LYRICS")));
                    song.setLanguage(cursor.getString(cursor.getColumnIndexOrThrow("LANGUAGE")));
                    song.setIdAlbum(cursor.getInt(cursor.getColumnIndexOrThrow("ID_ALBUM")));
                    song.setIdSlink(cursor.getInt(cursor.getColumnIndexOrThrow("ID_SLINK")));
                    song.setIdStyle(cursor.getInt(cursor.getColumnIndexOrThrow("ID_STYLE")));
                    song.setLinkMusic(cursor.getString(cursor.getColumnIndexOrThrow("Link_Music")));
                    songs.add(song);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return songs;
    }
}