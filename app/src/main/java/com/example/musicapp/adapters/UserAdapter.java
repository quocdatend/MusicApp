package com.example.musicapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Users;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public UserAdapter(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public UserAdapter(Context context, List<Users> users) {
    }

    public void close() {
        dbHelper.close();
    }

    // get all users from model users
    public List<Users> getAllUsers() {
        List<Users> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS", null);
        // add all to models users
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String username = cursor.getString(cursor.getColumnIndexOrThrow("USERNAME"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("PASS"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                String avatarUrl = cursor.getString(cursor.getColumnIndexOrThrow("AVATAR_URL"));
                String avatarDefault = cursor.getString(cursor.getColumnIndexOrThrow("DEFAULT_AVATAR"));
                Users user = new Users(id, username, password, email, avatarUrl, avatarDefault);
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }
}
