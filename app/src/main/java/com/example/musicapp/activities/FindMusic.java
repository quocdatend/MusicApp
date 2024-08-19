package com.example.musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.example.musicapp.DAO.Song_DAO;
import com.example.musicapp.DAO.StyleDAO;
import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.adapters.StylesAdapter;
import com.example.musicapp.databinding.ActivityFindMusicBinding;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Session;
import com.example.musicapp.models.SessionManager;
import com.example.musicapp.models.Song;
import com.example.musicapp.models.Style;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FindMusic extends AppCompatActivity {
    ActivityFindMusicBinding binding;
    SongsAdapter songsAdapter;
    ArrayList<Song> songs;
    private Style selectedStyle;
    private SearchView searchView;
    private ArrayList<Song> filteredList; // Changed to ArrayList
    private Song_DAO songDao;
    private Cloudinary cloudinary;
    private StyleDAO styleDAO;
    StylesAdapter styleAdapter;
    private List<Style> styles;
    private Style styleselect;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFindMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        searchView = binding.searchView;
        filteredList = new ArrayList<>();
        songs = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        styleDAO = new StyleDAO(dbHelper);
        session = SessionManager.getInstance().getSession();

        songDao = new Song_DAO(dbHelper);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        binding.spinnerStyles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (styleAdapter != null && styleAdapter.getCount() > 0) {
                    Style selectedStyle = (Style) styleAdapter.getItem(position);
                    if (selectedStyle != null) {
                        styleselect = selectedStyle;
                        System.out.println(styleselect);
                    } else {
                        styleselect = null;
                    }
                } else {
                    styleselect = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                styleselect = null;
            }
        });
        binding.lvSong.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(FindMusic.this, PlayingSongActivity.class);
            Song song = (Song) songsAdapter.getItem(position);
            intent.putExtra("position", position);
            Bundle bundle = new Bundle();
            List<Song> songs1 = new ArrayList<>();
            songs1 = initData();
            bundle.putSerializable("songs", (Serializable) songs1);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        binding.btnSelectStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (styleselect != null) {
                    filterS(styleselect.getId());
                } else {
                    Toast.makeText(FindMusic.this, "Please select a style first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initbutonView();
        loadData();
        loadSnipper();
    }

    private void loadData() {
        List<Song> songList = initData(); // Get the List from DAO
        if (songList != null) {
            songs.clear();
            songs.addAll(songList); // Convert List to ArrayList
            filteredList.clear();
            filteredList.addAll(songs);
            songsAdapter = new SongsAdapter(FindMusic.this, R.layout.item_song, filteredList);
            binding.lvSong.setAdapter(songsAdapter);
        }
    }

    private List<Song> initData() {
        if (songDao != null) {
            return songDao.getAllMusicRecords();
        } else {
            Log.e("FindMusic", "Song_DAO is not initialized.");
            return new ArrayList<>();
        }
    }

    private void filter(String query) {
        filteredList.clear();
        String normalizedQuery = normalizeVietnamese(query.toLowerCase());
        for (Song song : songs) {
            String normalizedTitle = normalizeVietnamese(song.getTitle().toLowerCase());
            if (normalizedTitle.contains(normalizedQuery)) {
                filteredList.add(song);
            }
        }
        songsAdapter.notifyDataSetChanged();
    }
    private void filterS(int query) {
        filteredList.clear();
        List<Song>songs1 = (ArrayList<Song>) styleDAO.getAllSongsByStyleId(query);
        for( Song song : songs1){
            filteredList.add(song);
        }
        songsAdapter.notifyDataSetChanged();
    }

    private String normalizeVietnamese(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    public void initbutonView() {
        ViewPager2 viewPager2 = findViewById(R.id.view_paper_2);
        RecyclerView rcvCategory = findViewById(R.id.rcv_category);
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnBrowser = findViewById(R.id.btnBrowser);
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        ImageButton btnLibrary = findViewById(R.id.btnLibrary);

        btnSearch.setColorFilter(getResources().getColor(R.color.black));

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
    private void loadSnipper() {
        List<Style> allStyles = styleDAO.getAllStyles();
        if (allStyles != null && !allStyles.isEmpty()) {
            styleAdapter = new StylesAdapter(this, R.layout.item_song, allStyles);
            binding.spinnerStyles.setAdapter(styleAdapter);
        } else {
            Toast.makeText(this, "No styles available", Toast.LENGTH_SHORT).show();
        }
    }
}