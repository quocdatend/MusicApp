package com.example.musicapp.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.musicapp.R;

public class NowPlayingFragmentBottom extends Fragment {
    TextView song_name_miniPlayer;
    Button btnPlay;
    View view;
    public NowPlayingFragmentBottom() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_now_playing_bottom, container, false);
        song_name_miniPlayer = view.findViewById(R.id.song_name_miniPlayer);
        return view;
    }
}