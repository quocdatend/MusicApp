package com.example.musicapp.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicapp.models.Admin;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Users;

import java.util.ArrayList;
import java.util.List;

public class Admin_DAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    public Admin_DAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    public void close() {
        dbHelper.close();
    }
    // get Admin by Name
    public List<Admin> getAdminByName(String name) {
        List<Admin> adminList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM ADMIN WHERE NAME = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{name});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String adminName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("PASS"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                Admin admin = new Admin(id, adminName, email, password);
                adminList.add(admin);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return adminList;
    }
    // get Admin by Email
    public List<Admin> getAdminByEmail(String email) {
        List<Admin> adminList = new ArrayList<>();
        db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM ADMIN WHERE EMAIL = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{email});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String adminName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("PASS"));
                String adminEmail = cursor.getString(cursor.getColumnIndexOrThrow("EMAIL"));
                Admin admin = new Admin(id, adminName, password, adminEmail);
                adminList.add(admin);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return adminList;
    }
}
