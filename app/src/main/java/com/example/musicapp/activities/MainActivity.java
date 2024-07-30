package com.example.musicapp.activities;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvTime, tvDuration;
    SeekBar seekBarTime, seekBarVolume;
    Button btnPlay;

    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Cấu hình giao diện người dùng
        setupUI();

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
    }

    private void initializeMediaPlayer() {
        musicPlayer = new MediaPlayer();
        if (1==3) {
            // Phát từ URL nếu có kết nối mạng
            try {
                String url = "https://res.cloudinary.com/dap6ivvwp/video/upload/v1722321113/anhSaoVaBauTroi_m9jbux.mp3";
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
            }
        } else {
            // Phát từ tài nguyên local nếu không có kết nối mạng
            musicPlayer = MediaPlayer.create(this, R.raw.a);
            musicPlayer.setLooping(true);
            musicPlayer.setVolume(0.5f, 0.5f);
            String duration = millisecondsToString(musicPlayer.getDuration());
            tvDuration.setText(duration);
            seekBarTime.setMax(musicPlayer.getDuration());
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
            while (musicPlayer != null) {
                if (musicPlayer.isPlaying()) {
                    try {
                        final int current = musicPlayer.getCurrentPosition();
                        final String elapsedTime = millisecondsToString(current);
                        runOnUiThread(() -> {
                            tvTime.setText(elapsedTime);
                            seekBarTime.setProgress(current);
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
}
