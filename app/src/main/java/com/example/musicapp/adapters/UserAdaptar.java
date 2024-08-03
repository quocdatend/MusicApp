package com.example.musicapp.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.models.DatabaseHelper;

public class UserAdaptar {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public UserAdaptar(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void close() {
        dbHelper.close();
    }

    // add users
    public void addUser(String username, String password, String email, String phone) {
        db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO USERS (USERNAME, PASS, EMAIL, PHONE, AVATAR_URL, DEFAULT_AVATAR) VALUES (?, ?, ?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{username, password, email, phone,null,"default.png"});
        db.close();
    }
}
