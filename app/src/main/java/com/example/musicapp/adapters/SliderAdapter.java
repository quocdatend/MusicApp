package com.example.musicapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activities.Slider;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>{
    private final List<Slider> mListSlider;

    public SliderAdapter(List<Slider> mListSlider) {
        this.mListSlider = mListSlider;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Slider slider = mListSlider.get(position);
        if (slider == null){
            return;
        }
        holder.imgSlider.setImageResource(slider.getResourceId());
    }

    @Override
    public int getItemCount() {
        if (mListSlider != null){
            return mListSlider.size();
        }
        return 0;
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder{
        private final ImageView imgSlider;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSlider = itemView.findViewById(R.id.img_slider);
        }
    }
}