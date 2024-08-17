package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.example.musicapp.R;
import com.example.musicapp.models.Song;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicAdapter extends RecyclerView.Adapter<adapters.MusicAdapter.MusicViewHolder>{

    private List<Song> mMusic;
    private Context mContext;
    private OnMusicClickListener mListener; // Add listener interface

    public interface OnMusicClickListener {
        void onMusicClick(Song song);
    }
    public MusicAdapter(Context context, OnMusicClickListener listener) {
        this.mContext = context;
        this.mListener = listener;

        // Initialize MediaManager
        if (MediaManager.get() == null) {
            MediaManager.init(mContext);
        }
    }
    public void setData(List<Song> list){
        this.mMusic = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music4you,parent,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {

        Song music = mMusic.get(position);
        if (music == null){
            return;
        }
        String fullUrl = music.getThumbnailUrl();
        if (fullUrl != null) {
            int lastSlashIndex = fullUrl.lastIndexOf("/");
            int lastDotIndex = fullUrl.lastIndexOf(".");
            if (lastSlashIndex != -1 && lastDotIndex != -1 && lastDotIndex > lastSlashIndex) {
                String publicId = fullUrl.substring(lastSlashIndex + 1, lastDotIndex);
                String cloudinaryUrl = MediaManager.get().url().generate(publicId);
                Picasso.get().load(cloudinaryUrl).into(holder.imgMusic);
            } else {
                holder.imgMusic.setImageResource(R.drawable.music4you_2);
            }
        } else {
            holder.imgMusic.setImageResource(R.drawable.icon_music);
        }
        holder.txtTitle.setText(music.getTitle());
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onMusicClick(music);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mMusic != null){
            return mMusic.size();
        }
        return 0;
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgMusic;
        private TextView txtTitle;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMusic = itemView.findViewById(R.id.img_music4you);
            txtTitle = itemView.findViewById(R.id.txt_title);

        }
    }
}