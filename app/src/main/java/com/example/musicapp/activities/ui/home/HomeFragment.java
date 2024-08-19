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

import com.example.musicapp.DAO.AlbumDAO;
import com.example.musicapp.DAO.Song_DAO;
import com.example.musicapp.adapters.AlbumAdapter;
import com.example.musicapp.adapters.ArtistAdapter;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.databinding.FragmentHomeBinding;
import com.example.musicapp.models.DatabaseHelper;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ArtistAdapter artistAdapter;
    private UserAdapter userAdapter;
    private Song_DAO songDao;
    private AlbumDAO albumDAO;

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
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        artistAdapter = new ArtistAdapter(requireContext());
        userAdapter = new UserAdapter(requireContext());
        songDao = new Song_DAO(dbHelper);
        albumDAO = new AlbumDAO(dbHelper);

        btnArtists.setText(artistAdapter.getAllArtists().size() + " Artists");
        btnUsers.setText(userAdapter.getAllUsers().size() + " Users");
        btnSongs.setText(songDao.getAllMusicRecords().size() + " Songs");
        btnAlbums.setText(albumDAO.getaddAlbum().size() + " Albums");

        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}