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
        values.put("ID", style.getId()); // Assuming ID is not auto-increment and needs to be set
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
}