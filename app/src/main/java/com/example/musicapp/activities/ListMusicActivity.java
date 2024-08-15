package com.example.musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.databinding.ActivityListMusicBinding;
import com.example.musicapp.fragments.NowPlayingFragmentBottom;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Song;
import com.example.musicapp.DAO.Song_DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ListMusicActivity extends AppCompatActivity {
    ActivityListMusicBinding binding;
    SongsAdapter songsAdapter;
    ArrayList<Song> songs;
    FrameLayout frag_bottom_player;
    private Song_DAO songDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityListMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize DatabaseHelper with correct context
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        songDao = new Song_DAO(dbHelper);
        frag_bottom_player = findViewById(R.id.frag_bottom_player);
        loadData();
        addEvents();
        getdatafrommain();
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

        if (songDao != null) {
            List<Song> songs = new ArrayList<>();
            songs = songDao.getAllMusicRecords();
            return songs;
        } else {
            Log.e("ListMusicActivity", "Song_DAO không được khởi tạo.");
            return null;
        }
    }
    private void addEvents() {
        binding.lvSong.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ListMusicActivity.this, MainActivity.class);
            Song song = (Song) songsAdapter.getItem(position);
            intent.putExtra("position",position);
            Bundle bundle = new Bundle();
            List<Song> songs1 = new ArrayList<>();
            songs1 = songDao.getAllMusicRecords();
//            System.out.println(songs1.toString());
            bundle.putSerializable("songs", (Serializable) songs1);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
    private void getdatafrommain() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Song song1 = new Song();
        if (bundle != null){
            song1 = (Song) bundle.getSerializable("song");
            int timecurrent = bundle.getInt("timeMusic");
            Log.i("ádadas",song1.getLinkMusic());
            NowPlayingFragmentBottom nowPlayingFragmentBottom = new NowPlayingFragmentBottom();
            Bundle bundle1 = new Bundle();
            bundle1.putString("songName", song1.getName());
            bundle1.putString("songLink", song1.getLinkMusic());
            bundle1.putInt("songTimecurrent", timecurrent);
            nowPlayingFragmentBottom.setArguments(bundle1);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_bottom_player, nowPlayingFragmentBottom)
                    .commit();
        }else {
            hideBottomPlayer();
        }
    }
    private void hideBottomPlayer() {
        if (frag_bottom_player != null) {
            frag_bottom_player.setVisibility(View.GONE);
        }
    }
}