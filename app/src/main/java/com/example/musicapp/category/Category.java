package com.example.musicapp.category;

import com.example.musicapp.models.Song;

import java.util.List;



public class Category {
    private String nameCategory;
    private List<Song> music;

    public Category(String nameCategory, List<Song> music){
        this.nameCategory = nameCategory;
        this.music = music;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<Song> getMusic() {
        return music;
    }

    public void setMusic(List<Song> music) {
        this.music = music;
    }
}