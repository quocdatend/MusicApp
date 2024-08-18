package com.example.musicapp.activities.ui.artists;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicapp.R;
import com.example.musicapp.adapters.ArtistAdapter;
import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.databinding.FragmentArtistsBinding;
import com.example.musicapp.databinding.FragmentUsersBinding;
import com.example.musicapp.models.Artists;
import com.example.musicapp.models.Users;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment {
    private FragmentArtistsBinding binding;
    public ArtistAdapter artistAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentArtistsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final ListView listView = binding.lvArtist;
        artistAdapter = new ArtistAdapter(requireContext());
        ArrayList<Artists> artists = (ArrayList<Artists>) artistAdapter.getAllArtists();
        ArrayAdapter<Artists> adapter = new ArrayAdapter<Artists>(requireContext(), R.layout.content_user_admin, artists) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.content_user_admin, parent, false);
                }

                Artists artist = getItem(position);
                ImageView imgAvatar = convertView.findViewById(R.id.imgAvatar);
                TextView txtUser = convertView.findViewById(R.id.txtUsername);
                TextView txtEmail = convertView.findViewById(R.id.txtEmail);

                if (artist != null) {
                    if (artist.getAvatar() != null) {
                        imgAvatar.setImageURI(Uri.parse(artist.getAvatar()));
                    } else {
                        imgAvatar.setImageResource(R.drawable.baseline_account_circle_24);
                    }
                    txtUser.setText("Name: " + artist.getName());
                    txtEmail.setText("Email: " + artist.getEmail());
                }

                return convertView;
            }
        };

        listView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
