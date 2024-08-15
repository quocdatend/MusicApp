package com.example.musicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.DAO.Song_DAO;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Song;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTime, tvDuration, tvSongName, tvSongTitle;
    private SeekBar seekBarTime, seekBarVolume;
    private Button btnPlay;
    private MediaPlayer musicPlayer;
    private String songLinks;
    private Song currentSong;
    private int timemusic = 0;
    private ActivityMainBinding binding;
    private List<Song> songs;
    private int position;
    private boolean isFavorite = false;
    private Song_DAO songDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        songDao = new Song_DAO(dbHelper);

        setupUI();
        getDataFromList();
        initializeMediaPlayer();
        configureSeekBars();
        startUpdateThread();
        addEventListeners();
        updateSongInfo();
    }

    private void updateSongInfo() {
        currentSong = songs.get(position);
        tvSongName.setText(currentSong.getName());
        tvSongTitle.setText(currentSong.getTitle());
        updateFavoriteButtonStatus();
        prepareMediaPlayer();
    }

    private void updateFavoriteButtonStatus() {
        isFavorite = songDao.checkfavMusic(1, currentSong.getId());
        Log.e("isFavorite",String.valueOf(isFavorite));
        binding.btnfavorate.setImageResource(isFavorite ? R.drawable.baseline_favorite_24 : R.drawable.baseline_favorite_border_24);
    }

    private void getDataFromList() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            position = intent.getIntExtra("position", 0);
            songs = (ArrayList<Song>) bundle.getSerializable("songs");

            if (songs != null) {
                currentSong = getCurrentSong();
                updateSongInfo();
            } else {
                Log.e("MainActivity", "Songs list is null");
            }
        }
    }

    private Song getCurrentSong() {
        return songs.get(position);
    }

    private void addEventListeners() {
        binding.btnNext.setOnClickListener(v -> switchSong(1));
        binding.btnback.setOnClickListener(v -> switchSong(-1));
        binding.btnfavorate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite == true){
                    songDao.deleteFavorate_Song(1,currentSong.getId());
                }else {
                    songDao.addfavMusic(1,currentSong.getId());
                }
                updateFavoriteButtonStatus();
            }
        });
    }

    private void switchSong(int direction) {
        if (musicPlayer != null) {
            if (musicPlayer.isPlaying()) {
                musicPlayer.stop();
            }
            musicPlayer.release();
            musicPlayer = null;
        }
        position = (position + direction + songs.size()) % songs.size();
        updateSongInfo();
        initializeMediaPlayer();
        configureSeekBars();
        if (musicPlayer != null) {
            musicPlayer.start();
        }
        seekBarTime.setMax(musicPlayer.getDuration());
        startUpdateThread();
    }

    private void setupUI() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTime = findViewById(R.id.tvTime);
        tvDuration = findViewById(R.id.tvDuration);
        seekBarTime = findViewById(R.id.seekBarTime);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        tvSongName = findViewById(R.id.tvSongName);
        tvSongTitle = findViewById(R.id.tvSongTitle);
    }

    private void initializeMediaPlayer() {
        if (musicPlayer != null) {
            musicPlayer.release();
        }
        musicPlayer = new MediaPlayer();
        prepareMediaPlayer();
    }

    private void prepareMediaPlayer() {
        if (musicPlayer == null) return;

        musicPlayer.reset();
        songLinks = currentSong.getLinkMusic();

        if (songLinks != null) {
            try {
                if (isValidUrl(songLinks)) {
                    musicPlayer.setDataSource(songLinks);
                    musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    musicPlayer.setOnPreparedListener(mp -> {
                        String duration = millisecondsToString(mp.getDuration());
                        tvDuration.setText(duration);
                        seekBarTime.setMax(mp.getDuration());
                        musicPlayer.start();
                    });
                    musicPlayer.prepareAsync();
                    musicPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.ic_pause);


                } else {
                    int resourceId = getRawResourceId(songLinks);
                    musicPlayer = MediaPlayer.create(this, resourceId);
                    musicPlayer.setLooping(true);
                    musicPlayer.setVolume(0.5f, 0.5f);
                    String duration = millisecondsToString(musicPlayer.getDuration());
                    tvDuration.setText(duration);
                    seekBarTime.setMax(musicPlayer.getDuration());
                    musicPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.ic_pause);

                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading audio from URL", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid song link", Toast.LENGTH_SHORT).show();
        }

        musicPlayer.setLooping(true);
        musicPlayer.setVolume(0.5f, 0.5f);
    }

    private void configureSeekBars() {
        seekBarVolume.setProgress(50);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if (isFromUser) {
                    float volume = progress / 100f;
                    musicPlayer.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if (isFromUser) {
                    musicPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void startUpdateThread() {
        new Thread(() -> {
            while (true) {
                try {
                    runOnUiThread(() -> {
                        if (musicPlayer != null && musicPlayer.isPlaying()) {
                            int current = musicPlayer.getCurrentPosition();
                            String elapsedTime = millisecondsToString(current);
                            tvTime.setText(elapsedTime);
                            seekBarTime.setProgress(current);
                            timemusic = current;
                        }
                    });
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    private String millisecondsToString(int time) {
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnPlay) {
            if (musicPlayer.isPlaying()) {
                musicPlayer.pause();
                btnPlay.setBackgroundResource(R.drawable.ic_play);
            } else {
                musicPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.ic_pause);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }
    }

    private boolean isValidUrl(String urlString) {
        try {
            new URL(urlString).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int getRawResourceId(String resourceName) {
        Context context = getApplicationContext();
        return context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, ListMusicActivity.class);
        Song song = currentSong;
        Bundle bundle = new Bundle();
        bundle.putSerializable("song", song);
        bundle.putInt("timeMusic", timemusic);
        intent.putExtras(bundle);
        startActivity(intent);
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }
        super.onBackPressed(); // Gọi phương thức mặc định để trở về màn hình trước đó
    }
}