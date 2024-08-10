package com.example.musicapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.DAO.AlbumDAO;
import com.example.musicapp.R;
import com.example.musicapp.adapters.AlbumAdapter;
import com.example.musicapp.databinding.ActivityCrudAlbumBinding;
import com.example.musicapp.models.Album;
import com.example.musicapp.models.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Crud_album extends AppCompatActivity {

    private ActivityCrudAlbumBinding binding;
    private AlbumAdapter albumAdapter;
    private AlbumDAO albumDAO;
    private Album selectedAlbum;
    private EditText etAlbumTitle;
    private EditText etReleaseDate;
    Album album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCrudAlbumBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Database Helper and DAO
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        albumDAO = new AlbumDAO(dbHelper);

        // Initialize UI components and setup events
        setupUI();
        loadData();
        addEventListeners();
    }

    private void setupUI() {
        etAlbumTitle = findViewById(R.id.etAlbumTitle);
        etReleaseDate = findViewById(R.id.etReleaseDate);
    }

    private void addEventListeners() {
        binding.btnAddAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addAlbum()) {
                    Toast.makeText(Crud_album.this, "Album Added Successfully", Toast.LENGTH_SHORT).show();
                    loadData(); // Refresh the list after adding
                } else {
                    Toast.makeText(Crud_album.this, "Failed to Add Album", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnDeleteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumDAO.deleteAlbum(album.getId());
                loadData(); // Refresh the list after adding
            }
        });
        binding.btnEditAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Album album1 = new Album();
                album1.setId(album.getId());
                album1.setReleaseDate(etReleaseDate.getText().toString());
                album1.setTitle(etAlbumTitle.getText().toString());
                albumDAO.editAlbum(album1);
                loadData(); // Refresh the list after adding
            }
        });

        binding.lvAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album1 = (Album) albumAdapter.getItem(position);
                album = album1;
                etAlbumTitle.setText(album.getTitle());
                etReleaseDate.setText(album.getReleaseDate());
                loadData();
            }
        });
    }

    private void loadData() {
        albumAdapter = new AlbumAdapter(Crud_album.this, R.layout.item_song, initData());
        binding.lvAlbums.setAdapter(albumAdapter);
    }

    private List<Album> initData() {
        if (albumDAO != null) {
            return albumDAO.getaddAlbum();
        } else {
            Log.e("Crud_album", "AlbumDAO is not initialized.");
            return new ArrayList<>();
        }
    }
    private boolean addAlbum() {
        String title = etAlbumTitle.getText().toString().trim();
        String releaseDate = etReleaseDate.getText().toString().trim();
        if (title.isEmpty() || releaseDate.isEmpty()) {
            return false;
        }
        Album album = new Album();
        album.setTitle(title);
        album.setReleaseDate(releaseDate);
        return albumDAO.addAlbum(album);
    }
}
