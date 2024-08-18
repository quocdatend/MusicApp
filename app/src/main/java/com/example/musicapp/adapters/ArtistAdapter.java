package com.example.musicapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.models.Artists;
import com.example.musicapp.models.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ArtistAdapter {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public ArtistAdapter(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    public void close() {
        dbHelper.close();
    }
    // get all Artists
    public List<Artists> getAllArtists() {
        List<Artists> artistList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ARTISTS", null);
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
            cursor.close();
            db.close();
        }
        return artistList;
    }
}
