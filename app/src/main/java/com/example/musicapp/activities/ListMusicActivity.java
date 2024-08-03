package com.example.musicapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.databinding.ActivityListMusicBinding;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.models.Song;

import java.util.ArrayList;
import java.util.List;


public class ListMusicActivity extends AppCompatActivity {
    ActivityListMusicBinding binding;
    SongsAdapter songsAdapter;
    ArrayList<Song> songs;

    FrameLayout frag_bottom_player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityListMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadData();
        addEvents();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void loadData(){
        songsAdapter  = new SongsAdapter(ListMusicActivity.this, R.layout.item_song, initData());
        binding.lvSong.setAdapter(songsAdapter);
    }
    private List<Song> initData(){
        List<Song> songs = new ArrayList<>();
        songs.add(new Song(1, "Song 1", "Title 1", "3:30", "thumbnail1.png", "Lyrics 1", "English", 101, 201, 301, "2131689472"));
        songs.add(new Song(2, "Song 2", "Title 2", "4:00", "thumbnail2.png", "Lyrics 2", "Spanish", 102, 202, 302, "https://res.cloudinary.com/dap6ivvwp/video/upload/v1722321113/anhSaoVaBauTroi_m9jbux.mp3"));
        songs.add(new Song(3, "Song 3", "Title 3", "2:45", "thumbnail3.png", "Lyrics 3", "French", 103, 203, 303, "https://res.cloudinary.com/dap6ivvwp/video/upload/v1722668314/a_oxfmzk.mp3"));
        return songs;
    }
    private void addEvents() {
        binding.lvSong.setOnItemClickListener((parent, view, position, id) -> {
            Song song = (Song) songsAdapter.getItem(position);
            Intent intent = new Intent(ListMusicActivity.this, MainActivity.class);
            intent.putExtra("songName", song.getName());
            intent.putExtra("songTitle", song.getTitle());
            intent.putExtra("songlink", song.getLinkMusic());
            startActivity(intent);
        });
    }
}