package com.example.musicapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.musicapp.R;
import com.example.musicapp.activities.ListMusicActivity;
import com.example.musicapp.models.Song;

import java.util.List;

public class SongsAdapter extends BaseAdapter {

    private Activity context;
    private int item_layout;
    private List<Song> songs;
    private int selectedPosition = -1; // -1 có nghĩa là không có item nào được chọn

    public SongsAdapter(Activity context, int item_layout, List<Song> songs) {
        this.context = context;
        this.item_layout = item_layout;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(item_layout, null);
            holder.tvName = convertView.findViewById(R.id.tvArtist);
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Cập nhật dữ liệu của item
        Song s = songs.get(position);
        holder.tvName.setText(s.getTitle());
        holder.tvTitle.setText(s.getName());


        return convertView;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public static class ViewHolder {
        TextView tvTitle, tvName;
    }
}
