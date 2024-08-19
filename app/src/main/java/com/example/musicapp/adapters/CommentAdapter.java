package com.example.musicapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<String> {

    public CommentAdapter(Context context, List<String> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        String comment = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(comment);

        return convertView;
    }
}