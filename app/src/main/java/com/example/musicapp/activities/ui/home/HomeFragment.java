package com.example.musicapp.activities.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.adapters.AlbumAdapter;
import com.example.musicapp.adapters.ArtistAdapter;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArtistAdapter artistAdapter;
    private UserAdapter userAdapter;
    private SongsAdapter songsAdapter;
    private AlbumAdapter albumAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button btnArtists = binding.btnArtists;
        final Button btnUsers = binding.btnUsers;
        final Button btnSongs = binding.btnSongs;
        final Button btnAlbums = binding.btnAlbums;

        artistAdapter = new ArtistAdapter(requireContext());
        userAdapter = new UserAdapter(requireContext());
        //songsAdapter = new SongsAdapter(getContext());
        //albumAdapter = new AlbumAdapter(requireContext());

        btnArtists.setText(artistAdapter.getAllArtists().size() + " Artists");
        btnUsers.setText(userAdapter.getAllUsers().size() + " Users");
        //btnSongs.setText(songsAdapter.getAllSongs().size() + "Songs: ");
        //btnAlbums.setText(albumAdapter.getAllAlbums().size() + "Albums: ");

        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}