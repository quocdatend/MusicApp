package com.example.musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.databinding.ActivityListMusicBinding;
import com.example.musicapp.fragments.NowPlayingFragmentBottom;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Session;
import com.example.musicapp.models.SessionManager;
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
    boolean check = false;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityListMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        songDao = new Song_DAO(dbHelper);
        frag_bottom_player = findViewById(R.id.frag_bottom_player);
        session = SessionManager.getInstance().getSession();
        loadData();
        addEvents();
        getdatafrommain();
        initbutonView();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadData() {
        songsAdapter = new SongsAdapter(ListMusicActivity.this, R.layout.item_song, initData());
        binding.lvSong.setAdapter(songsAdapter);
    }

    private List<Song> initData() {
        List<Song> songs = new ArrayList<>();
        Intent intent1 = getIntent();
        Bundle bundle = intent1.getExtras();
        if (bundle != null) {
            songs = (ArrayList<Song>) bundle.getSerializable("listsong");
            System.out.println(songs.toString());
        } else {
            songs = songDao.getAllMusicRecords();
            check = true;
        }
        return songs;
    }

    private void addEvents() {
        binding.lvSong.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ListMusicActivity.this, PlayingSongActivity.class);
            Song song = (Song) songsAdapter.getItem(position);
            intent.putExtra("position", position);
            Bundle bundle = new Bundle();
            List<Song> songs1 = new ArrayList<>();
            songs1 = initData();
            bundle.putSerializable("songs", (Serializable) songs1);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void getdatafrommain() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Song song1 = new Song();
        if (bundle != null) {
            song1 = (Song) bundle.getSerializable("song");
            if (song1 != null) { // Kiểm tra null
                int timecurrent = bundle.getInt("timeMusic");
                NowPlayingFragmentBottom nowPlayingFragmentBottom = new NowPlayingFragmentBottom();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("songInList", song1);
                bundle1.putInt("songTimecurrent", timecurrent);
                nowPlayingFragmentBottom.setArguments(bundle1);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_bottom_player, nowPlayingFragmentBottom)
                        .commit();
            } else {
                Log.e("ListMusicActivity", "Song object is null");
                hideBottomPlayer();
            }
        } else {
            Log.e("ListMusicActivity", "Bundle is null");
            hideBottomPlayer();
        }
    }
    public void initbutonView() {
        ViewPager2 viewPager2 = findViewById(R.id.view_paper_2);
        RecyclerView rcvCategory = findViewById(R.id.rcv_category);
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnBrowser = findViewById(R.id.btnBrowser);
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        ImageButton btnLibrary = findViewById(R.id.btnLibrary);

        btnBrowser.setColorFilter(getResources().getColor(R.color.black));

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        btnBrowser.setOnClickListener(v -> {
            List<Song> categorysong = songDao.getAllSongsByUserId(session.getCode());
            Intent intent = new Intent(this, ListMusicActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("listsong", (Serializable) categorysong);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindMusic.class);
            startActivity(intent);
        });
        btnLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
    }
    private void hideBottomPlayer() {
        if (frag_bottom_player != null) {
            frag_bottom_player.setVisibility(View.GONE);
        }
    }
}