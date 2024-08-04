package com.example.musicapp.activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.models.Song;

import java.io.IOException;

public class NowPlayingFragmentBottom extends Fragment {
    private TextView song_name_miniPlayer;
    private Button btnPlay;
    private MediaPlayer musicPlayer;
    Song song;
    int timecurrent;
    public NowPlayingFragmentBottom() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now_playing_bottom, container, false);
        song_name_miniPlayer = view.findViewById(R.id.song_name_miniPlayer);
        btnPlay = view.findViewById(R.id.btnPlay_mini);
        // Lấy Bundle từ Intent
        Bundle bundle = getArguments();
        if (bundle != null) {
            song_name_miniPlayer.setText(bundle.getString("songName"));
            initializeMediaPlayer(bundle.getString("songLink"));
            timecurrent = bundle.getInt("songTimecurrent");
            setupOnClickListeners();
        }

        return view;
    }

    private void initializeMediaPlayer(String songLinks) {
        if (musicPlayer == null) {
            musicPlayer = new MediaPlayer();
        }
        if (songLinks != null) {
            Toast.makeText(getContext(), songLinks, Toast.LENGTH_SHORT).show();
            try {
                musicPlayer.setDataSource(songLinks);
                musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                musicPlayer.setOnPreparedListener(mp -> {
                    mp.seekTo(timecurrent * 1000);
                    mp.start();
                });
                musicPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error loading audio from URL", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Invalid song link", Toast.LENGTH_SHORT).show();
        }
        musicPlayer.setLooping(true);
    }

    private void setupOnClickListeners() {
        btnPlay.setOnClickListener(v -> {
            if (musicPlayer != null) {
                if (musicPlayer.isPlaying()) {
                    musicPlayer.pause();
                    btnPlay.setBackgroundResource(R.drawable.ic_play);
                } else {
                    musicPlayer.start();
                    btnPlay.setBackgroundResource(R.drawable.ic_pause);
                }
            } else {
                Log.e("Error", "MediaPlayer is null");
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (musicPlayer != null) {
            musicPlayer.stop(); // Stop the music when the fragment is no longer visible
            musicPlayer.release(); // Release resources
            musicPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (musicPlayer != null) {
            musicPlayer.release();
            musicPlayer = null;
        }
    }
}
