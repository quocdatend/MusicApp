package com.example.musicapp.models;

import java.io.Serializable;

public class Song implements Serializable {
    private int id;
    private String name;
    private String title;
    private String duration;
    private String thumbnailUrl;
    private String lyrics;
    private String language;
    private int idAlbum;
    private int idSlink;
    private int idStyle;
    private String linkMusic;

    public Song() {
    }

    public Song(int id, String name, String title, String duration, String thumbnailUrl, String lyrics, String language, int idAlbum, int idSlink, int idStyle, String linkMusic) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.duration = duration;
        this.thumbnailUrl = thumbnailUrl;
        this.lyrics = lyrics;
        this.language = language;
        this.idAlbum = idAlbum;
        this.idSlink = idSlink;
        this.idStyle = idStyle;
        this.linkMusic = linkMusic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }

    public int getIdSlink() {
        return idSlink;
    }

    public void setIdSlink(int idSlink) {
        this.idSlink = idSlink;
    }

    public int getIdStyle() {
        return idStyle;
    }

    public void setIdStyle(int idStyle) {
        this.idStyle = idStyle;
    }

    public String getLinkMusic() {
        return linkMusic;
    }

    public void setLinkMusic(String linkMusic) {
        this.linkMusic = linkMusic;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", language='" + language + '\'' +
                ", idAlbum=" + idAlbum +
                ", idSlink=" + idSlink +
                ", idStyle=" + idStyle +
                ", linkMusic='" + linkMusic + '\'' +
                '}';
    }
}