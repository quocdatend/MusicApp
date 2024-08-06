package com.example.musicapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.models.Song;
import com.example.musicapp.models.Style;

import java.util.List;

public class StylesAdapter extends BaseAdapter {
    Activity context;
    int item_layout;
    List<Style> styles;

    public StylesAdapter(Activity context, int item_layout, List<Style> styles) {
        this.context = context;
        this.item_layout = item_layout;
        this.styles = styles;
    }

    @Override
    public int getCount() {
        return styles.size();
    }

    @Override
    public Object getItem(int position) {
        return styles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StylesAdapter.ViewHolder holder;
        if(convertView == null){
            holder = new StylesAdapter.ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(item_layout,null);
            holder.tvName = convertView.findViewById(R.id.tvArtist);
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            convertView.setTag(holder);
        }else {
            holder = (StylesAdapter.ViewHolder) convertView.getTag();
        }
        Style s  = styles.get(position);
        holder.tvName.setText(s.getName());
        holder.tvTitle.setText(s.getDetail());
        return convertView;
    }
    public  static class ViewHolder{
        TextView tvTitle,tvName;
    }
}
