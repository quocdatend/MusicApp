package com.example.musicapp.models;

public class Song {
    private String title;
    private String artist;
    private String path;

    public Song() {
    }

    public Song(String title, String path, String artist) {
        this.title = title;
        this.path = path;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
