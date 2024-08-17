package com.example.musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.category.Category;
import com.example.musicapp.utils.OnCategoryClickListener;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{
    private Context mContext;
    private List<Category> mListCategory;
    private OnCategoryClickListener listener;
    public CategoryAdapter(Context mContext, OnCategoryClickListener listener) {
        this.mContext = mContext;
        this.listener = listener;  // Initialize the listener
    }


    public void setData(List<Category> list ){
        this.mListCategory = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = mListCategory.get(position);
        if (category == null){
            return;
        }
        holder.txtNameCategory.setText(category.getNameCategory());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);
        holder.rcvMusic.setLayoutManager(linearLayoutManager);
        adapters.MusicAdapter musicAdapter = new adapters.MusicAdapter(mContext, song -> {
            if (listener != null) {
                listener.onMusicClick(song);
            }
        });
        musicAdapter.setData(category.getMusic());
        holder.rcvMusic.setAdapter(musicAdapter);
        holder.txtNameCategory.setOnClickListener(v -> {
            listener.onCategoryClick(category);
        });

    }

    @Override
    public int getItemCount() {
        if(mListCategory != null){
            return mListCategory.size();
        }
        return 0;
    }



    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNameCategory;
        private RecyclerView rcvMusic;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNameCategory = itemView.findViewById(R.id.txt_MusicForYou);
            rcvMusic = itemView.findViewById(R.id.recycle_music);
        }
    }
}