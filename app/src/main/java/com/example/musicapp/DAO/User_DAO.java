package com.example.musicapp.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Users;

import java.util.ArrayList;
import java.util.List;

public class User_DAO {
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public User_DAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    public void addUser(String username, String password, String email) {
        db = dbHelper.getWritableDatabase();
        String sql = "INSERT INTO USERS (USERNAME, PASS, EMAIL, AVATAR_URL, DEFAULT_AVATAR) VALUES (?, ?, ?, ?, ?)";
        db.execSQL(sql, new Object[]{username, password, email, null, "default.png"});
        db.close();
    }
    // get user by username
    public List<Users> getUserByUsername(String name) {
        List<Users> userList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE USERNAME = ?", new String[]{name});
        // add all to models users if cursor not null
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
    // get user by email
    public List<Users> getUserByEmail(String e) {
        List<Users> userList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERS WHERE EMAIL = ?", new String[]{e});
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
    // delete user by id
    public void deleteUserById(int id) {
        db = dbHelper.getWritableDatabase();
        db.delete("USERS", "ID = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    // delete user role by userId
    public void deleteUserRoleByUserId(int userId) {
        db = dbHelper.getWritableDatabase();
        db.delete("USER_ROLE", "USER_ID = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
    // update avatar user
    public void updateAvatarUser(int userId, String avatarUrl) {
        db = dbHelper.getWritableDatabase();
        db.execSQL("UPDATE USERS SET AVATAR_URL = ? WHERE ID = ?", new Object[]{avatarUrl, userId});
        db.close();
    }

}
