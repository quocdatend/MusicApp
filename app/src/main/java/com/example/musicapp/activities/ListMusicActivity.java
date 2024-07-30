package com.example.musicapp.activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.models.Song;

import java.util.ArrayList;

public class ListMusicActivity extends AppCompatActivity {

    ArrayList<Song> songArrayList;
    ListView lvSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_music);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lvSongs = findViewById(R.id.lvSong);

        songArrayList = new ArrayList<>();
        for (int i = 1 ; i <= 10 ; i++)
            songArrayList.add(new Song("Song" + i, "Artist"+ i, "Path "+ i));

        SongsAdapter songsAdapter = new SongsAdapter(this,songArrayList);
        lvSongs.setAdapter(songsAdapter);
    }
}