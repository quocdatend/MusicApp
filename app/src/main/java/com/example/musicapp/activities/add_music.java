package com.example.musicapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.databinding.ActivityAddMusicBinding;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Song;
import com.example.musicapp.DAO.Song_DAO;
import com.example.musicapp.utils.ImageUploader;
import com.squareup.picasso.Picasso;
import com.sun.mail.iap.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class add_music extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    ActivityAddMusicBinding binding;
    SongsAdapter songsAdapter;
    private Song_DAO songDao;
    EditText etName, etTitle, etDuration, etLyrics, etLanguage, etIdAlbum, etIdSlink, etIdStyle;
    Button btnUploadLinkMusic, btnAddMusic, btnEditMusic, btnDeleteMusic;
    Song songcmp;
    ActivityResultLauncher<Intent> laun;
    ImageView ivImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Cloudinary cloudinary;
    Uri uri;
    private static boolean check1 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        songDao = new Song_DAO(dbHelper);
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dap6ivvwp");
        config.put("api_key", "875469923979388");
        config.put("api_secret", "sT_lEC69UilqcB6NB6Fhn6kaZqU");
        MediaManager.init(this, config);
        cloudinary = new Cloudinary(config);
        setupUI();
        loadData();
        Addevent();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        laun = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), results -> {
            if (results.getResultCode() == RESULT_OK && results.getData() != null) {
                uri = results.getData().getData();
                binding.ivImage.setImageURI(uri);
                check1 = false;
            }
        });
    }

    private void loadData() {
        songsAdapter = new SongsAdapter(add_music.this, R.layout.item_song, initData());
        binding.lvSong.setAdapter(songsAdapter);
    }

    private List<Song> initData() {
        if (songDao != null) {
            List<Song> songs = new ArrayList<>();
            songs = songDao.getAllMusicRecords();
            return songs;
            // Xử lý danh sách các bài hát
        } else {
            // Xử lý trường hợp songDao là null
            Log.e("ListMusicActivity", "Song_DAO không được khởi tạo.");
            return null;
        }
    }

    private void Addevent() {
        binding.lvSong.setOnItemClickListener((parent, view, position, id) -> {
            Song song = (Song) songsAdapter.getItem(position);
            Log.e("lshdfjksdhf", song.toString());
            songcmp = song;
            etName.setText(song.getName());
            etTitle.setText(song.getTitle());
            etDuration.setText(song.getDuration());
            etLyrics.setText(song.getLyrics());
            etLanguage.setText(song.getLanguage());
            etIdAlbum.setText(String.valueOf(song.getIdAlbum()));
            etIdSlink.setText(String.valueOf(song.getIdSlink()));
            etIdStyle.setText(String.valueOf(song.getIdStyle()));
            String fullUrl = song.getThumbnailUrl();
            int lastSlashIndex = fullUrl.lastIndexOf("/");
            int lastDotIndex = fullUrl.lastIndexOf(".");
            if (lastSlashIndex != -1 && lastDotIndex != -1 && lastDotIndex > lastSlashIndex) {
                String publicId = fullUrl.substring(fullUrl.lastIndexOf("/") + 1, fullUrl.lastIndexOf("."));
                String cloudinaryUrl = MediaManager.get().url().generate(publicId);
                Picasso.get().load(cloudinaryUrl).into(ivImage);
            }
        });
        binding.btnDeleteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songDao.deleteMusicRecord(songcmp.getId());
                loadData();
            }
        });
        binding.btnAddMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song song = new Song();
                if (uri == null) {
                    Log.e("Error", "Image URI is null");
                }
                ImageUploader uploader = new ImageUploader();
                uploader.uploadImage(add_music.this, uri, new ImageUploader.UploadCallbackListener() {
                    @Override
                    public void onUploadSuccess(String url) {
                        System.out.println("Image uploaded successfully. URL: " + url);
                        song.setThumbnailUrl(url);
                        song.setName(etName.getText().toString());
                        song.setDuration(etDuration.getText().toString());
                        song.setTitle(etTitle.getText().toString());
                        song.setLyrics(etLyrics.getText().toString());
                        song.setLanguage(etLanguage.getText().toString());
                        song.setIdAlbum(Integer.parseInt(etIdAlbum.getText().toString()));
                        song.setIdSlink(Integer.parseInt(etIdSlink.getText().toString()));
                        song.setIdStyle(Integer.parseInt(etIdStyle.getText().toString()));
                        Log.e("song", song.toString());
                        songDao.addMusicRecord(song);
                        Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadData();
                    }

                    @Override
                    public void onUploadError(String error) {
                        System.err.println("Upload failed: " + error);
                    }
                });
            }
        });
        binding.btnEditMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song song = songcmp;
                if (uri == null) {
                    Log.e("Error", "Image URI is null");
                }
                ImageUploader uploader = new ImageUploader();
                uploader.uploadImage(add_music.this, uri, new ImageUploader.UploadCallbackListener() {
                    @Override
                    public void onUploadSuccess(String url) {
                        System.out.println("Image uploaded successfully. URL: " + url);
                        song.setThumbnailUrl(url);
                        song.setName(etName.getText().toString());
                        song.setDuration(etDuration.getText().toString());
                        song.setTitle(etTitle.getText().toString());
                        song.setLyrics(etLyrics.getText().toString());
                        song.setLanguage(etLanguage.getText().toString());
                        song.setIdAlbum(Integer.parseInt(etIdAlbum.getText().toString()));
                        song.setIdSlink(Integer.parseInt(etIdSlink.getText().toString()));
                        song.setIdStyle(Integer.parseInt(etIdStyle.getText().toString()));
                        Log.e("song", song.toString());
                        songDao.updateMusicRecord(song);
                        loadData();
                        Toast.makeText(add_music.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUploadError(String error) {
                        System.err.println("Upload failed: " + error);
                    }
                });
            }
        });
        binding.ivImage.setOnClickListener(v -> {
            openImageChooser();
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
        etIdStyle = findViewById(R.id.etIdStyle);
        btnUploadLinkMusic = findViewById(R.id.btnUploadLinkMusic);
        btnAddMusic = findViewById(R.id.btnAddMusic);
        btnEditMusic = findViewById(R.id.btnEditMusic);
        btnDeleteMusic = findViewById(R.id.btnDeleteMusic);
        ivImage = findViewById(R.id.ivImage);
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
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