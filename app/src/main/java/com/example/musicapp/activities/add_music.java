package com.example.musicapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.databinding.ActivityAddMusicBinding;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Song;
import com.example.musicapp.DAO.Song_DAO;

import java.util.ArrayList;
import java.util.List;

public class add_music extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    ActivityAddMusicBinding binding;
    SongsAdapter songsAdapter;
    private Song_DAO songDao;
    EditText etName, etTitle, etDuration, etThumbnailUrl, etLyrics, etLanguage, etIdAlbum, etIdSlink, etIdStyle;
    Button btnUploadLinkMusic, btnAddMusic, btnEditMusic, btnDeleteMusic;
    Song songcmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        songDao = new Song_DAO(dbHelper);
        setupUI();
        loadData();
        Addevent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadData() {
        songsAdapter = new SongsAdapter(add_music.this, R.layout.item_song, initData());
        binding.lvSong.setAdapter(songsAdapter);
    }

    private List<Song> initData() {
        if (songDao != null) {
            List<Song> songs = new ArrayList<>();
            songs = songDao.getAllMusicRecords();
            return songs;
            // Xử lý danh sách các bài hát
        } else {
            // Xử lý trường hợp songDao là null
            Log.e("ListMusicActivity", "Song_DAO không được khởi tạo.");
            return null;
        }
    }

    private void Addevent() {
        binding.lvSong.setOnItemClickListener((parent, view, position, id) -> {
            Song song = (Song) songsAdapter.getItem(position);
            songcmp = song;
            etName.setText(song.getName());
            etTitle.setText(song.getTitle());
            etDuration.setText(song.getDuration());
            etThumbnailUrl.setText(song.getThumbnailUrl());
            etLyrics.setText(song.getLyrics());
            etLanguage.setText(song.getLanguage());
            etIdAlbum.setText(String.valueOf(song.getIdAlbum()));
            etIdSlink.setText(String.valueOf(song.getIdSlink()));
            etIdStyle.setText(String.valueOf(song.getIdStyle()));
        });
        binding.btnDeleteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songDao.deleteMusicRecord(songcmp.getId());
                loadData();
            }
        });
        binding.btnAddMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songDao.addMusicRecord(songcmp);
                loadData();
            }
        });
        binding.btnEditMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songDao.updateMusicRecord(songcmp);
                loadData();
            }
        });
    }
    private void setupUI() {
        etName = findViewById(R.id.etName);
        etTitle = findViewById(R.id.etTitle);
        etDuration = findViewById(R.id.etDuration);
        etThumbnailUrl = findViewById(R.id.etThumbnailUrl);
        etLyrics = findViewById(R.id.etLyrics);
        etLanguage = findViewById(R.id.etLanguage);
        etIdAlbum = findViewById(R.id.etIdAlbum);
        etIdSlink = findViewById(R.id.etIdSlink);
        etIdStyle = findViewById(R.id.etIdStyle);
        btnUploadLinkMusic = findViewById(R.id.btnUploadLinkMusic);
        btnAddMusic = findViewById(R.id.btnAddMusic);
        btnEditMusic = findViewById(R.id.btnEditMusic);
        btnDeleteMusic = findViewById(R.id.btnDeleteMusic);
    }

}