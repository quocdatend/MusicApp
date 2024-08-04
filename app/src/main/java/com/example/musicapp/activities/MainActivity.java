package com.example.musicapp.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.models.Song;
import com.example.musicapp.utils.SQLServerConnector;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvTime, tvDuration, tvSongName, tvSongTitle;
    SeekBar seekBarTime, seekBarVolume;
    Button btnPlay;
    MediaPlayer musicPlayer;
    String songLinks;
    Song song1;
    int timemusic = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Cấu hình giao diện người dùng
        setupUI();
        // nhâận dữ liệu
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
             song1 = (Song) bundle.getSerializable("song");
            String songName = song1.getName();
            String songTitle = song1.getTitle();
            songLinks = song1.getLinkMusic();
            tvSongName.setText(songName);
            tvSongTitle.setText(songTitle);
        }
        // gửi dữ liệu

        // Khởi tạo MediaPlayer
        initializeMediaPlayer();

        // Cấu hình SeekBar
        configureSeekBars();

        // Bắt đầu Thread để cập nhật UI
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
        if (songLinks != null) {
            if (isValidUrl(songLinks)) {
                Toast.makeText(this, songLinks, Toast.LENGTH_SHORT).show();
                try {
                    String url = songLinks;
                    musicPlayer.setDataSource(url);
                    musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    musicPlayer.setOnPreparedListener(mp -> {
                        String duration = millisecondsToString(musicPlayer.getDuration());
                        tvDuration.setText(duration);
                        seekBarTime.setMax(musicPlayer.getDuration());
                    });
                    musicPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error loading audio from URL", Toast.LENGTH_SHORT).show();
                }
            } else {
                musicPlayer = MediaPlayer.create(this, Integer.parseInt(songLinks));
                musicPlayer.setLooping(true);
                musicPlayer.setVolume(0.5f, 0.5f);
                String duration = millisecondsToString(musicPlayer.getDuration());
                tvDuration.setText(duration);
                seekBarTime.setMax(musicPlayer.getDuration());
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
                float volume = progress / 100f;
                musicPlayer.setVolume(volume, volume);
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
                    seekBar.setProgress(progress);
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
                    // Đảm bảo kiểm tra và xử lý trạng thái MediaPlayer trong luồng chính
                    runOnUiThread(() -> {
                        if (musicPlayer != null) {
                            try {
                                if (musicPlayer.isPlaying()) {
                                    final int current = musicPlayer.getCurrentPosition();
                                    final String elapsedTime = millisecondsToString(current);
                                    tvTime.setText(elapsedTime);
                                    seekBarTime.setProgress(current);
                                    timemusic = current;
                                }
                            } catch (IllegalStateException e) {
                                // Xử lý lỗi khi MediaPlayer không hợp lệ
                                e.printStackTrace();
                            }
                        }
                    });
                    Thread.sleep(1000); // Cập nhật mỗi giây
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break; // Dừng luồng khi bị gián đoạn
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
        String packageName = context.getPackageName();
        return context.getResources().getIdentifier(resourceName, "raw", packageName);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, ListMusicActivity.class);
        Song song = song1;
        Bundle bundle = new Bundle();
        song.setDuration(String.valueOf(timemusic));
        bundle.putSerializable("song",song);
        intent.putExtras(bundle);
        startActivity(intent);
        super.onBackPressed(); // Gọi phương thức mặc định để trở về màn hình trước đó
    }
}
