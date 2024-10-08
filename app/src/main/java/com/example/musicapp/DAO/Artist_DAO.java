package com.example.musicapp.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.models.Artists;
import com.example.musicapp.models.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Artist_DAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public Artist_DAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    public void close() {
        dbHelper.close();
    }
    // get Artist by Email
    public List<Artists> getArtistByEmail(String e) {
        List<Artists> artistList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM ARTISTS WHERE EMAIL = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{e});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String artistName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("PASS"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                String biography = cursor.getString(cursor.getColumnIndexOrThrow("BIOGRAPHY"));
                String stageName = cursor.getString(cursor.getColumnIndexOrThrow("STAGE_NAME"));
                String nationality = cursor.getString(cursor.getColumnIndexOrThrow("NATIONALITY"));
                String debugYear = cursor.getString(cursor.getColumnIndexOrThrow("DEBUG_YEAR"));
                int albumsCount = cursor.getInt(cursor.getColumnIndexOrThrow("ALBUMS_COUNT"));
                String website = cursor.getString(cursor.getColumnIndexOrThrow("WEBSITE"));
                int genreId = cursor.getInt(cursor.getColumnIndexOrThrow("GENRE_ID"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("AVATAR"));
                Artists artist = new Artists(id, artistName, email, password, biography, stageName, nationality, debugYear, albumsCount, website, genreId, imageUrl);
                artistList.add(artist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return artistList;
    }
    public Artists findArtistById(int id) {
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM ARTISTS WHERE ID = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(id)});

        Artists artist = null; // Khởi tạo artist là null
        if (cursor.moveToFirst()) {
            // Lấy các giá trị từ cursor
            int artistId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
            String artistName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("PASS"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
            String biography = cursor.getString(cursor.getColumnIndexOrThrow("BIOGRAPHY"));
            String stageName = cursor.getString(cursor.getColumnIndexOrThrow("STAGE_NAME"));
            String nationality = cursor.getString(cursor.getColumnIndexOrThrow("NATIONALITY"));
            String debugYear = cursor.getString(cursor.getColumnIndexOrThrow("DEBUG_YEAR"));
            int albumsCount = cursor.getInt(cursor.getColumnIndexOrThrow("ALBUMS_COUNT"));
            String website = cursor.getString(cursor.getColumnIndexOrThrow("WEBSITE"));
            int genreId = cursor.getInt(cursor.getColumnIndexOrThrow("GENRE_ID"));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("AVATAR"));

            // Tạo đối tượng Artists
            artist = new Artists(artistId, artistName, email, password, biography, stageName, nationality, debugYear, albumsCount, website, genreId, imageUrl);
        }

        cursor.close(); // Đóng cursor
        db.close(); // Đóng database

        return artist; // Trả về đối tượng Artists (có thể là null nếu không tìm thấy)
    }


    // get Artist by Name
    public List<Artists> getArtistByName(String name) {
        List<Artists> artistList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM ARTISTS WHERE NAME = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String artistName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("PASS"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                String biography = cursor.getString(cursor.getColumnIndexOrThrow("BIOGRAPHY"));
                String stageName = cursor.getString(cursor.getColumnIndexOrThrow("STAGE_NAME"));
                String nationality = cursor.getString(cursor.getColumnIndexOrThrow("NATIONALITY"));
                String debugYear = cursor.getString(cursor.getColumnIndexOrThrow("DEBUG_YEAR"));
                int albumsCount = cursor.getInt(cursor.getColumnIndexOrThrow("ALBUMS_COUNT"));
                String website = cursor.getString(cursor.getColumnIndexOrThrow("WEBSITE"));
                int genreId = cursor.getInt(cursor.getColumnIndexOrThrow("GENRE_ID"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("AVATAR"));
                Artists artist = new Artists(id, artistName, email, password, biography, stageName, nationality, debugYear, albumsCount, website, genreId, imageUrl);
                artistList.add(artist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return artistList;
    }

    // add Artist
    public  void addArtist(String name, String email, String password, String imageUrl) {
        db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO ARTISTS (NAME, EMAIL, PASS, BIOGRAPHY, STAGE_NAME, NATIONALITY, DEBUG_YEAR, ALBUMS_COUNT, WEBSITE, GENRE_ID, AVATAR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{name, email, password, null, null, null, null, 0, null, 0, imageUrl});
        db.close();
    }

    // add Role Artist
    public void addRoleArtist(int artistId, int roleId) {
        db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO ARTIST_ROLE (ID_ARTIST, ID_ROLE) VALUES (?, ?)";
        db.execSQL(sql, new Object[]{artistId, roleId});
        db.close();
    }
    // update avatar artist
    public void updateAvatarArtist(int artistId, String imageUrl) {
        db = dbHelper.getWritableDatabase();
        String sql = "UPDATE ARTISTS SET AVATAR = ? WHERE ID = ?";
        db.execSQL(sql, new Object[]{imageUrl, artistId});
    }
}