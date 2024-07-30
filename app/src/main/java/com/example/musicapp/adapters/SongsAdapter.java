package com.example.musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.musicapp.R;
import com.example.musicapp.models.Song;

import java.util.List;

public class SongsAdapter extends ArrayAdapter<Song> {

    public SongsAdapter(@NonNull Context context,  @NonNull List<Song> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,null);

        Song song = getItem(position);

        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        TextView tvArtist = convertView.findViewById(R.id.tvArtist);

        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());
        return convertView;
    }
}
