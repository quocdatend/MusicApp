package com.example.musicapp.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AppMusic.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the table here
        // users
        db.execSQL("CREATE TABLE USERS (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " USERNAME TEXT NOT NULL," +
                " PASS TEXT NOT NULL," +
                " EMAIL TEXT NOT NULL," +
                " PHONE TEXT NOT NULL," +
                " AVATAR_URL TEXT," +
                " DEFAULT_AVATAR TEXT NOT NULL" +
                ")");
        // Admin
        db.execSQL("CREATE TABLE ADMIN (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL," +
                "EMAIL TEXT NOT NULL," +
                "PASS TEXT NOT NULL" +
                ")");
        // Role
        db.execSQL("CREATE TABLE ROLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT NOT NULL" +
                ")");
        // Admin Role
        db.execSQL("CREATE TABLE ADMIN_ROLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ADMIN_ID INTEGER NOT NULL," +
                "ROLE_ID INTEGER NOT NULL" +
                ")");
        // User Role
        db.execSQL("CREATE TABLE USER_ROLE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "USER_ID INTEGER NOT NULL," +
                "ROLE_ID INTEGER NOT NULL" +
                ")");
        // Album Song
        db.execSQL("CREATE TABLE ALBUM_SONG (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    ID_ALBUM INTEGER NOT NULL," +
                "    ID_SONG INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_ALBUM) REFERENCES ALBUMS(ID)," +
                "    FOREIGN KEY (ID_SONG) REFERENCES SONGS(ID)" +
                ")");
        // Albums
        db.execSQL("CREATE TABLE ALBUMS (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    TITLE TEXT NOT NULL," +
                "    RELEASE_DATE DATE" +
                ")");
        // Artist
        db.execSQL("CREATE TABLE ARTISTS (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    NAME TEXT NOT NULL," +
                "    EMAIL TEXT NOT NULL," +
                "    PHONE TEXT NOT NULL," +
                "    PASS TEXT NOT NULL," +
                "    BIOGRAPHY TEXT," +
                "    STAGE_NAME TEXT," +
                "    NATIONALITY TEXT," +
                "    DEBUG_YEAR TEXT," +
                "    ALBUMS_COUNT INTEGER NOT NULL," +
                "    WEBSITE TEXT," +
                "    GENRE_ID INTEGER," +
                "    FOREIGN KEY (GENRE_ID) REFERENCES STYLE_SONG(ID)" +
                ")");
        // Artist Role
        db.execSQL("CREATE TABLE ARTIST_ROLE (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    ID_ARTIST INTEGER NOT NULL," +
                "    ID_ROLE INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_ARTIST) REFERENCES ARTISTS(ID)," +
                "    FOREIGN KEY (ID_ROLE) REFERENCES ROLE(ID)" +
                ")");
        // Artist Song
        db.execSQL("CREATE TABLE ARTISTS_SONG (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    ID_ARTIST INTEGER NOT NULL," +
                "    ID_SONG INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_ARTIST) REFERENCES ARTISTS(ID)," +
                "    FOREIGN KEY (ID_SONG) REFERENCES SONGS(ID)" +
                ")");
        // Comments
        db.execSQL("CREATE TABLE COMMENTS (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    TITLE TEXT NOT NULL," +
                "    TIME_COMMENT TIME NOT NULL," +
                "    LIKES INTEGER NOT NULL," +
                "    DISLIKES INTEGER NOT NULL," +
                "    ID_USER INTEGER NOT NULL," +
                "    ID_SONG INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_USER) REFERENCES USERS(ID)," +
                "    FOREIGN KEY (ID_SONG) REFERENCES SONGS(ID)" +
                ")");
        // Favorites
        db.execSQL("CREATE TABLE FAVORITES (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    MARKED_DATETIMEE DATETIME NOT NULL," +
                "    ID_USER INTEGER NOT NULL," +
                "    ID_SONG INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_USER) REFERENCES USERS(ID)," +
                "    FOREIGN KEY (ID_SONG) REFERENCES SONGS(ID)" +
                ")");
        // Listening History
        db.execSQL("CREATE TABLE LISTENING_HISTORY (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    TIME_LISTENING TIME NOT NULL," +
                "    ID_USER INTEGER NOT NULL," +
                "    ID_SONG INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_USER) REFERENCES USERS(ID)," +
                "    FOREIGN KEY (ID_SONG) REFERENCES SONGS(ID)" +
                ")");
        // Payment History
        db.execSQL("CREATE TABLE PAYMENT_HISTORY (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    ID_USER INTEGER NOT NULL," +
                "    ID_SERVICE INTEGER NOT NULL," +
                "    TIME_PAY TIME NOT NULL," +
                "    FOREIGN KEY (ID_USER) REFERENCES USERS(ID)," +
                "    FOREIGN KEY (ID_SERVICE) REFERENCES SERVICES(ID)" +
                ")");
        // Playlist songs
        db.execSQL("CREATE TABLE PLAYLIST_SONGS (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    ID_SONG INTEGER NOT NULL," +
                "    ID_PLAYLIST INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_SONG) REFERENCES SONGS(ID)," +
                "    FOREIGN KEY (ID_PLAYLIST) REFERENCES PLAYLISTS(ID)" +
                ")");
        // Playlists
        db.execSQL("CREATE TABLE PLAYLISTS (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    NAME TEXT NOT NULL," +
                "    ID_USER INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_USER) REFERENCES USERS(ID)" +
                ")");
        // Services
        db.execSQL("CREATE TABLE SERVICES (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    NAME TEXT NOT NULL," +
                "    PRICE REAL NOT NULL" +
                ")");
        // Social Media
        db.execSQL("CREATE TABLE SOCIAL_MEDIA (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    NAME TEXT," +
                "    LINKSOCIAL TEXT," +
                "    ID_ARTIST INTEGER," +
                "    FOREIGN KEY (ID_ARTIST) REFERENCES ARTISTS(ID)" +
                ")");
        // Songs
        db.execSQL("CREATE TABLE SONGS (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    NAME TEXT NOT NULL," +
                "    TITLE TEXT NOT NULL," +
                "    DURATION TIME," +
                "    THUMBNAILURL TEXT," +
                "    LYRICS TEXT," +
                "    LANGUAGE TEXT," +
                "    ID_ALBUM INTEGER," +
                "    ID_SLINK INTEGER," +
                "    ID_STYLE INTEGER," +
                "    Link_Music TEXT," +
                "    FOREIGN KEY (ID_ALBUM) REFERENCES ALBUMS(ID)," +
                "    FOREIGN KEY (ID_STYLE) REFERENCES STYLE_SONG(ID)" +
                ")");
        // Style Song
        db.execSQL("CREATE TABLE SONGS_STYLES (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    ID_STYLE INTEGER NOT NULL," +
                "    ID_SONG INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_STYLE) REFERENCES STYLE_SONG(ID)," +
                "    FOREIGN KEY (ID_SONG) REFERENCES SONGS(ID)" +
                ")");
        // Streaming links
        db.execSQL("CREATE TABLE STREAMING_LINKS (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    LINK TEXT," +
                "    ID_SONG INTEGER NOT NULL," +
                "    FOREIGN KEY (ID_SONG) REFERENCES SONGS(ID)" +
                ")");
        // Style Song
        db.execSQL("CREATE TABLE STYLE_SONG (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    NAME TEXT," +
                "    DETAIL TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xử lý nâng cấp database nếu cần
    }
}
