package com.example.musicapp.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.musicapp.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {
    private SQLiteOpenHelper dbHelper;
    public AlbumDAO(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }
    public List<Album> getaddAlbum() {
        List<Album> musicList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM ALBUMS";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Album musicItem = new Album(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TITLE")),
                        cursor.getString(cursor.getColumnIndexOrThrow("RELEASE_DATE"))
                );
                musicList.add(musicItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return musicList;
    }
    public boolean addAlbum(Album album) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TITLE", album.getTitle());
        values.put("RELEASE_DATE", album.getReleaseDate());

        long result = db.insert("ALBUMS", null, values);
        db.close();
        return result != -1; // Returns true if insert was successful, false otherwise
    }
    public boolean editAlbum(Album album) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TITLE", album.getTitle());
        values.put("RELEASE_DATE", album.getReleaseDate());
        int rowsAffected = db.update("ALBUMS", values, "ID = ?", new String[]{String.valueOf(album.getId())});
        db.close();
        return rowsAffected > 0; // Returns true if update was successful, false otherwise
    }
    public boolean deleteAlbum(int albumId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete("ALBUMS", "ID = ?", new String[]{String.valueOf(albumId)});
        db.close();
        return rowsAffected > 0; // Returns true if delete was successful, false otherwise
    }

    public boolean AddMusic_Album(int idM, int idA){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID_SONG", idM);
        values.put("ID_ALBUM", idA);
        long result = db.insert("ALBUM_SONG", null, values);
        db.close();
        return result != -1; // Returns true if insert was successful, false otherwise
    }
    public boolean deleteAlbum_Song(int albumId,int Musicid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = db.delete("ALBUM_SONG", "ID_ALBUM = ? AND ID_SONG = ?", new String[]{String.valueOf(albumId), String.valueOf(Musicid)});
        db.close();
        return rowsAffected > 0; // Trả về true nếu xóa thành công, false nếu không
    }
}