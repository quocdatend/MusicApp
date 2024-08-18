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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.example.musicapp.DAO.Artist_DAO;
import com.example.musicapp.DAO.Song_DAO;
import com.example.musicapp.DAO.StyleDAO;
import com.example.musicapp.DAO.User_DAO;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityPlayingSongBinding;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Session;
import com.example.musicapp.models.SessionManager;
import com.example.musicapp.models.Song;
import com.example.musicapp.models.Style;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayingSongActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvTime, tvDuration, tvSongName, tvSongTitle;
    private SeekBar seekBarTime, seekBarVolume;
    private Button btnPlay;
    private MediaPlayer musicPlayer;
    private String songLinks;
    private Song currentSong;
    private int timemusic = 0;
    private ActivityPlayingSongBinding binding;
    private List<Song> songs;
    private int position;
    private boolean isFavorite = false;
    private Song_DAO songDao;
    private ImageView ivSongImage;
    private Cloudinary cloudinary;
    private StyleDAO styleDAO;

    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPlayingSongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dap6ivvwp");
        config.put("api_key", "875469923979388");
        config.put("api_secret", "sT_lEC69UilqcB6NB6Fhn6kaZqU");
//        MediaManager.init(this, config);
        cloudinary = new Cloudinary(config);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        songDao = new Song_DAO(dbHelper);
        styleDAO = new StyleDAO(dbHelper);
        session = SessionManager.getInstance().getSession();
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
        String fullUrl = currentSong.getThumbnailUrl();
        if (fullUrl != null) {
            int lastSlashIndex = fullUrl.lastIndexOf("/");
            int lastDotIndex = fullUrl.lastIndexOf(".");
            if (lastSlashIndex != -1 && lastDotIndex != -1 && lastDotIndex > lastSlashIndex) {
                String publicId = fullUrl.substring(lastSlashIndex + 1, lastDotIndex);
                String cloudinaryUrl = MediaManager.get().url().generate(publicId);
                Picasso.get().load(cloudinaryUrl).into(ivSongImage);
            } else {
                ivSongImage.setImageResource(R.drawable.icon_music);
            }
        } else {
            ivSongImage.setImageResource(R.drawable.icon_music);
        }
        updateFavoriteButtonStatus();
        prepareMediaPlayer();
    }

    private void updateFavoriteButtonStatus() {
        isFavorite = songDao.checkfavMusic(session.getCode(), currentSong.getId());
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
        binding.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSongDetails(currentSong);
            }
        });
        binding.btnNext.setOnClickListener(v -> switchSong(1));
        binding.btnback.setOnClickListener(v -> switchSong(-1));
        binding.btnfavorate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite == true){
                    songDao.deleteFavorate_Song(session.getCode(),currentSong.getId());
                }else {
                    songDao.addfavMusic(session.getCode(),currentSong.getId());
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
        ivSongImage = findViewById(R.id.ivSongImage);
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
        Intent intent = new Intent(PlayingSongActivity.this, ListMusicActivity.class);
        Song song = currentSong;
        Bundle bundle = new Bundle();
        bundle.putSerializable("song", song);
        bundle.putInt("timeMusic", timemusic);
        bundle.putSerializable("listsong", (Serializable) songs);
        intent.putExtras(bundle);
        startActivity(intent);
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }
        super.onBackPressed(); // Gọi phương thức mặc định để trở về màn hình trước đó
    }
    private void showSongDetails(Song song) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.song_dialog, null);

        TextView titleTextView = dialogView.findViewById(R.id.song_title);
        TextView nameTextView = dialogView.findViewById(R.id.song_name);
        TextView durationTextView = dialogView.findViewById(R.id.song_duration);
        TextView languageTextView = dialogView.findViewById(R.id.song_language);
        TextView lyricsTextView = dialogView.findViewById(R.id.song_lyrics);
        TextView song_style = dialogView.findViewById(R.id.song_style);
        TextView song_album = dialogView.findViewById(R.id.song_album);

        List<Style> styles = styleDAO.getStylesBySongId(song.getId());
        System.out.println(styles.toString());
        titleTextView.setText(song.getTitle());
        nameTextView.setText(song.getName());
        durationTextView.setText("Duration: " + song.getDuration());
        languageTextView.setText("Language: " + song.getLanguage());
        lyricsTextView.setText(song.getLyrics());
        builder.setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}