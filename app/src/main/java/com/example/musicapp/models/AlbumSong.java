package com.example.musicapp.models;

public class AlbumSong {
    private int albumId;
    private int songId;

    public AlbumSong() {
    }

    public AlbumSong(int albumId, int songId) {
        this.albumId = albumId;
        this.songId = songId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    @Override
    public String toString() {
        return "AlbumSong{" +
                "albumId=" + albumId +
                ", songId=" + songId +
                '}';
    }
}