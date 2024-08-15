package com.example.musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.DAO.AlbumDAO;
import com.example.musicapp.DAO.Song_DAO;
import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.databinding.ActivityEditAlbumBinding;
import com.example.musicapp.models.Album;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Song;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Edit_Album extends AppCompatActivity {
    private TextView tvAlbumedit;
    private Spinner spinnerAlbums;
    private ActivityEditAlbumBinding binding;
    private AlbumDAO albumDAO;
    private SongsAdapter songsAdapter;
    private SongsAdapter songsAdapter1;
    private Song_DAO songDao;
    private Album album;
    static int idMusic;
    static int idMusicdelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        albumDAO = new AlbumDAO(dbHelper);
        songDao = new Song_DAO(dbHelper);
        Spinner spinnerAlbums = findViewById(R.id.spinnerAlbums);
        setupUI();
        loadData();
        addEventListeners();
    }

    private void addEventListeners() {
        binding.btnDeleteEditAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        binding.spinnerAlbums.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Song selectedSong = (Song) songsAdapter.getItem(position);
                if (selectedSong != null) {
                    idMusic = selectedSong.getId();
                    String songName = selectedSong.getName();
                    Log.d("Edit_Album", "Selected Song ID: " + idMusic);
                    Log.d("Edit_Album", "Selected Song Name: " + songName);
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.btnAddMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumDAO.AddMusic_Album(idMusic, album.getId());
                loadData();
            }
        });
        binding.lvSongA.setOnItemClickListener((parent, view, position, id) -> {
            songsAdapter1.setSelectedPosition(position);
            songsAdapter1.notifyDataSetChanged();
            Song song = (Song) songsAdapter1.getItem(position);
            idMusicdelete = song.getId();
        });
        binding.btnDeleteEditAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumDAO.deleteAlbum_Song(album.getId(),idMusicdelete);
                loadData();
            }
        });
    }

    private void setupUI() {
        tvAlbumedit = findViewById(R.id.tvAlbumedit);
        spinnerAlbums = findViewById(R.id.spinnerAlbums);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            album = (Album) bundle.getSerializable("album");
            if (album != null) {
                tvAlbumedit.setText(album.getTitle());
                SetupSpinner(); // Chỉ gọi SetupSpinner khi album không phải null
            } else {
                Log.e("Edit_Album", "Album is null");
                tvAlbumedit.setText("No album data available");
            }
        } else {
            Log.e("Edit_Album", "Bundle is null");
            tvAlbumedit.setText("No album data available");
        }
    }

    private void SetupSpinner() {
        if (album == null) {
            Log.e("Edit_Album", "Album is null, skipping spinner setup");
            return;
        }

    }

    private void loadData() {
        if (album == null) {
            Log.e("Edit_Album", "Album is null, skipping data load");
            return;
        }
        songsAdapter1 = new SongsAdapter(this, R.layout.item_song, initData());
        binding.lvSongA.setAdapter(songsAdapter1);
        List<Song> allSongs = songDao.getAllMusicRecords();
        List<Song> songs = difference(allSongs,initData());
        songsAdapter = new SongsAdapter(this, R.layout.item_song, songs);
        binding.spinnerAlbums.setAdapter(songsAdapter);
    }

    private List<Song> initData() {
        if (album == null) {
            Log.e("Edit_Album", "Album is null, cannot fetch songs");
            return new ArrayList<>();
        }
        if (songDao != null) {
            return songDao.getAllMusicByAlbumId(album.getId());
        } else {
            Log.e("Edit_Album", "Song_DAO không được khởi tạo.");
            return new ArrayList<>();
        }
    }
    public List<Song> difference(List<Song> allSongs, List<Song> selectedSongs) {
        List<Song> unselectedSongs = new ArrayList<>(allSongs);
        for (Song selectedSong : selectedSongs) {
            unselectedSongs.removeIf(song -> song.getId() == selectedSong.getId());
        }
        return unselectedSongs;
    }
}