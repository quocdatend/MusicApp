package com.example.musicapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.example.musicapp.DAO.StyleDAO;
import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.adapters.StylesAdapter;
import com.example.musicapp.databinding.ActivityAddMusicBinding;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Song;
import com.example.musicapp.DAO.Song_DAO;
import com.example.musicapp.models.Style;
import com.example.musicapp.utils.ImageUploader;
import com.example.musicapp.utils.Mp3Uploader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_music extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private ActivityAddMusicBinding binding;
    private SongsAdapter songsAdapter;
    StylesAdapter styleAdapter;
    StylesAdapter styleAdapter1;
    private Song_DAO songDao;
    private StyleDAO styleDAO;
    private EditText etName, etTitle, etDuration, etLyrics, etLanguage, etIdAlbum, etIdSlink;
    private Button btnUploadLinkMusic, btnAddMusic, btnEditMusic, btnDeleteMusic;
    private Song songcmp;
    private ActivityResultLauncher<Intent> laun;
    private ActivityResultLauncher<Intent> laun2;
    private ImageView ivImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_MP3_REQUEST_CODE = 2;
    private Cloudinary cloudinary;
    private Uri uri, uriMusic;
    private Mp3Uploader mp3Uploader;
    private List<Style> styles;
    private Style styleselect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DatabaseHelper(this);
        songDao = new Song_DAO(dbHelper);
        styleDAO = new StyleDAO(dbHelper);
        mp3Uploader = new Mp3Uploader();
        // Initialize styles list
        styles = new ArrayList<>();
        // Initialize Cloudinary
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dap6ivvwp");
        config.put("api_key", "875469923979388");
        config.put("api_secret", "sT_lEC69UilqcB6NB6Fhn6kaZqU");
//        MediaManager.init(this, config);
        cloudinary = new Cloudinary(config);
        Spinner spinnerStyle = findViewById(R.id.spinnerAlbums);
        setupUI();
        loadData();
        Addevent();
        loadSnipper();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        laun = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), results -> {
            if (results.getResultCode() == RESULT_OK && results.getData() != null) {
                Uri selectedUri = results.getData().getData();
                if (selectedUri != null) {
                    if (results.getResultCode() == PICK_IMAGE_REQUEST) {
                        uri = selectedUri;
                        binding.ivImage.setImageURI(uri);
                    }
                }
            }
        });
        laun2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), results -> {
            if (results.getResultCode() == RESULT_OK && results.getData() != null) {
                Uri selectedUri = results.getData().getData();
                uriMusic = selectedUri;
            }
        });
    }

    private void loadSnipper() {
        List<Style> allStyles = styleDAO.getAllStyles();
        if (allStyles != null && !allStyles.isEmpty()) {
            styleAdapter = new StylesAdapter(this, R.layout.item_song, allStyles);
            binding.spinnerStyles.setAdapter(styleAdapter);
        } else {
            Toast.makeText(this, "No styles available", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        songsAdapter = new SongsAdapter(this, R.layout.item_song, initData());
        binding.lvSong.setAdapter(songsAdapter);

        if (styles != null) {
            styleAdapter1 = new StylesAdapter(this, R.layout.item_song, styles);
            binding.lvStyles.setAdapter(styleAdapter1);
        }
    }
    private List<Song> initData() {
        if (songDao != null) {
            return songDao.getAllMusicRecords();
        } else {
            Log.e("ListMusicActivity", "Song_DAO không được khởi tạo.");
            return new ArrayList<>();
        }
    }

    private void Addevent() {
        binding.lvSong.setOnItemClickListener((parent, view, position, id) -> {
            Song song = (Song) songsAdapter.getItem(position);
            songcmp = song;
            etName.setText(song.getName());
            etTitle.setText(song.getTitle());
            etDuration.setText(song.getDuration());
            etLyrics.setText(song.getLyrics());
            etLanguage.setText(song.getLanguage());
            etIdAlbum.setText(String.valueOf(song.getIdAlbum()));
            etIdSlink.setText(String.valueOf(song.getIdSlink()));
            styles = styleDAO.getStylesBySongId(song.getId());
            String fullUrl = song.getThumbnailUrl();
            if (fullUrl != null) {
                int lastSlashIndex = fullUrl.lastIndexOf("/");
                int lastDotIndex = fullUrl.lastIndexOf(".");
                if (lastSlashIndex != -1 && lastDotIndex != -1 && lastDotIndex > lastSlashIndex) {
                    String publicId = fullUrl.substring(lastSlashIndex + 1, lastDotIndex);
                    String cloudinaryUrl = MediaManager.get().url().generate(publicId);
                    Picasso.get().load(cloudinaryUrl).into(ivImage);
                } else {
                    ivImage.setImageResource(R.drawable.icon_music);
                }
            } else {
                ivImage.setImageResource(R.drawable.icon_music);
            }
            loadData();
            Toast.makeText(this, "MP3 upload " + song.getThumbnailUrl(), Toast.LENGTH_SHORT).show();
        });

        binding.btnDeleteMusic.setOnClickListener(v -> {
            if (songcmp != null) {
                songDao.deleteMusicRecord(songcmp.getId());
                loadData();
            }
        });

        binding.btnAddMusic.setOnClickListener(v -> {
            Song song = new Song();
            // Set the song attributes from the input fields
            song.setName(etName.getText().toString());
            song.setDuration(etDuration.getText().toString());
            song.setTitle(etTitle.getText().toString());
            song.setLyrics(etLyrics.getText().toString());
            song.setLanguage(etLanguage.getText().toString());
            song.setIdAlbum(Integer.parseInt(etIdAlbum.getText().toString()));
            song.setIdSlink(Integer.parseInt(etIdSlink.getText().toString()));

            // Check if an image URI is provided
            if (uri != null) {
                ImageUploader uploader = new ImageUploader();
                uploader.uploadImage(this, uri, new ImageUploader.UploadCallbackListener() {
                    @Override
                    public void onUploadSuccess(String url) {
                        song.setThumbnailUrl(url);

                        // Check if an MP3 URI is provided
                        if (uriMusic != null) {
                            mp3Uploader.uploadMp3(add_music.this, uriMusic, new Mp3Uploader.UploadCallbackListener() {
                                @Override
                                public void onUploadSuccess(String mp3Url) {
                                    song.setLinkMusic(mp3Url);
                                    Log.e("aksjdkasjdk MP3LINK", mp3Url);
                                    songDao.addMusicRecord(song);
                                    Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    loadData();
                                }

                                @Override
                                public void onUploadError(String error) {
                                    Toast.makeText(add_music.this, "MP3 upload failed: " + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If no MP3 file is selected, save the song with only the image
                            songDao.addMusicRecord(song);
                            Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            loadData();
                        }
                    }

                    @Override
                    public void onUploadError(String error) {
                        Toast.makeText(add_music.this, "Image upload failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (uriMusic != null) {
                // If only MP3 is provided
                mp3Uploader.uploadMp3(add_music.this, uriMusic, new Mp3Uploader.UploadCallbackListener() {
                    @Override
                    public void onUploadSuccess(String mp3Url) {
                        song.setLinkMusic(mp3Url);
                        Log.e("aksjdkasjdk MP3LINK", mp3Url);
                        songDao.addMusicRecord(song);
                        Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadData();
                    }

                    @Override
                    public void onUploadError(String error) {
                        Toast.makeText(add_music.this, "MP3 upload failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // If neither image nor MP3 is provided, just save the song
                songDao.addMusicRecord(song);
                Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });

        binding.btnEditMusic.setOnClickListener(v -> {
            songcmp.setName(etName.getText().toString());
            songcmp.setDuration(etDuration.getText().toString());
            songcmp.setTitle(etTitle.getText().toString());
            songcmp.setLyrics(etLyrics.getText().toString());
            songcmp.setLanguage(etLanguage.getText().toString());
            songcmp.setIdAlbum(Integer.parseInt(etIdAlbum.getText().toString()));
            songcmp.setIdSlink(Integer.parseInt(etIdSlink.getText().toString()));
            if (uri != null) {
                ImageUploader uploader = new ImageUploader();
                uploader.uploadImage(this, uri, new ImageUploader.UploadCallbackListener() {
                    @Override
                    public void onUploadSuccess(String url) {
                        songcmp.setThumbnailUrl(url);

                        // Check if an MP3 URI is provided
                        if (uriMusic != null) {
                            mp3Uploader.uploadMp3(add_music.this, uriMusic, new Mp3Uploader.UploadCallbackListener() {
                                @Override
                                public void onUploadSuccess(String mp3Url) {
                                    songcmp.setLinkMusic(mp3Url);
                                    Log.e("aksjdkasjdk MP3LINK", mp3Url);
                                    songDao.addMusicRecord(songcmp);
                                    Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    loadData();
                                }

                                @Override
                                public void onUploadError(String error) {
                                    Toast.makeText(add_music.this, "MP3 upload failed: " + error, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // If no MP3 file is selected, save the song with only the image
                            songDao.addMusicRecord(songcmp);
                            Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            loadData();
                        }
                    }

                    @Override
                    public void onUploadError(String error) {
                        Toast.makeText(add_music.this, "Image upload failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (uriMusic != null) {
                // If only MP3 is provided
                mp3Uploader.uploadMp3(add_music.this, uriMusic, new Mp3Uploader.UploadCallbackListener() {
                    @Override
                    public void onUploadSuccess(String mp3Url) {
                        songcmp.setLinkMusic(mp3Url);
                        Log.e("aksjdkasjdk MP3LINK", mp3Url);
                        songDao.addMusicRecord(songcmp);
                        Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadData();
                    }

                    @Override
                    public void onUploadError(String error) {
                        Toast.makeText(add_music.this, "MP3 upload failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // If neither image nor MP3 is provided, just save the song
                songDao.addMusicRecord(songcmp);
                Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                loadData();
            }
        });

        binding.ivImage.setOnClickListener(v -> openImageChooser());

        binding.btnUploadLinkMusic.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/*"); // Set the type to audio to filter MP3 files
            laun2.launch(Intent.createChooser(intent, "Select MP3"));
        });

        binding.spinnerStyles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (styles != null && styleAdapter.getCount() > 0) {
                    Style selectedSong = (Style) styleAdapter.getItem(position);
                    styleselect = selectedSong;
                } else {
                    styleselect = null;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        binding.addStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ándklasdas",styleselect.toString());
                if (songcmp != null && styleselect != null) {
                    styleDAO.addSongStyle(songcmp.getId(), styleselect.getId());
                    loadData();
                } else {
                    Toast.makeText(add_music.this, "Please select a style first", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.lvStyles.setOnItemClickListener((parent, view, position, id) -> {
            Style style = (Style) styleAdapter1.getItem(position);
            styleDAO.deleteStyle_Song(style.getId(),songcmp.getId());
            loadData();
        });
    }
    private void setupUI() {
        etName = findViewById(R.id.etName);
        etTitle = findViewById(R.id.etTitle);
        etDuration = findViewById(R.id.etDuration);
        etLyrics = findViewById(R.id.etLyrics);
        etLanguage = findViewById(R.id.etLanguage);
        etIdAlbum = findViewById(R.id.etIdAlbum);
        etIdSlink = findViewById(R.id.etIdSlink);
        btnUploadLinkMusic = findViewById(R.id.btnUploadLinkMusic);
        btnAddMusic = findViewById(R.id.btnAddMusic);
        btnEditMusic = findViewById(R.id.btnEditMusic);
        btnDeleteMusic = findViewById(R.id.btnDeleteMusic);
        ivImage = findViewById(R.id.ivImage);
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivImage.setImageBitmap(bitmap);
                uri = imageUri;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}