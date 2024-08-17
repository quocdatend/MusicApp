package com.example.musicapp.utils;

import com.example.musicapp.category.Category;
import com.example.musicapp.models.Song;

public interface OnCategoryClickListener {
    void onCategoryClick(Category category);
    void onMusicClick(Song song);
}
