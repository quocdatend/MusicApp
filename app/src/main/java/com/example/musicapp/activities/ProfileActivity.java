package com.example.musicapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.example.musicapp.DAO.Artist_DAO;
import com.example.musicapp.DAO.User_DAO;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityProfileBinding;
import com.example.musicapp.models.Artists;
import com.example.musicapp.models.Session;
import com.example.musicapp.models.SessionManager;
import com.example.musicapp.models.Users;
import com.example.musicapp.utils.ImageUploader;
import com.example.musicapp.utils.Mp3Uploader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    User_DAO userDao;
    Artist_DAO artistDao;
    Session session;
    private Cloudinary cloudinary;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ActivityResultLauncher<Intent> laun;
    Uri uri;
    private boolean check_user = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userDao = new User_DAO(this);
        artistDao = new Artist_DAO(this);
        session = SessionManager.getInstance().getSession();
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dap6ivvwp");
        config.put("api_key", "875469923979388");
        config.put("api_secret", "sT_lEC69UilqcB6NB6Fhn6kaZqU");
//        MediaManager.init(this, config);
        cloudinary = new Cloudinary(config);
        addEvents();
        loadData();
    }
    private void loadData() {
        Users user = userDao.getUserByEmail("duydatphung7@gmail.com").get(0);
        Artists artist = artistDao.getArtistByEmail(session.getEmail()).get(0);
        if (artist != null) {
            binding.txtArtist.setText("Yes");
            binding.txtName.setText(artist.getName());
            binding.txtEmail.setText(artist.getEmail());
            binding.txtIsAuthor.setText("Bạn đã là nhà sáng tác!");
            // close event click txtIsAuthor
            binding.txtIsAuthor.setOnClickListener(null);
            String fullUrl = user.getAvatarUrl();
            if (fullUrl != null) {
                int lastSlashIndex = fullUrl.lastIndexOf("/");
                int lastDotIndex = fullUrl.lastIndexOf(".");
                if (lastSlashIndex != -1 && lastDotIndex != -1 && lastDotIndex > lastSlashIndex) {
                    String publicId = fullUrl.substring(lastSlashIndex + 1, lastDotIndex);
                    String cloudinaryUrl = MediaManager.get().url().generate(publicId);
                    Picasso.get().load(cloudinaryUrl).into(binding.imgProfile);
                } else {
                    binding.imgProfile.setImageResource(R.drawable.baseline_account_box_24);
                }
            } else {
                binding.imgProfile.setImageResource(R.drawable.baseline_account_box_24);
            }
        } else {
            binding.txtArtist.setText("No");
            binding.txtName.setText(user.getUsername());
            binding.txtEmail.setText(user.getEmail());
            String fullUrl = user.getAvatarUrl();
            if (fullUrl != null) {
                int lastSlashIndex = fullUrl.lastIndexOf("/");
                int lastDotIndex = fullUrl.lastIndexOf(".");
                if (lastSlashIndex != -1 && lastDotIndex != -1 && lastDotIndex > lastSlashIndex) {
                    String publicId = fullUrl.substring(lastSlashIndex + 1, lastDotIndex);
                    String cloudinaryUrl = MediaManager.get().url().generate(publicId);
                    Picasso.get().load(cloudinaryUrl).into(binding.imgProfile);
                } else {
                    binding.imgProfile.setImageResource(R.drawable.baseline_account_box_24);
                }
            } else {
                binding.imgProfile.setImageResource(R.drawable.baseline_account_box_24);
            }
        }

    }

    private void addEvents() {
        binding.txtForgetPassword.setOnClickListener(v -> {
            // show dialog "You must logout to change password from forget password in login page!"
            Toast.makeText(this, "You must logout to change password from forget password in login page!", Toast.LENGTH_SHORT).show();
        });
        binding.btnLogout.setOnClickListener(v -> {
            finish();
        });
        binding.btnUpload.setOnClickListener(v -> {
            // upload image ang set image to imgProfile here
            if (session.getRole() == 3) {
                openImageChooser();
            } else {
                check_user = true;
                openImageChooser();
            }
        });
        binding.txtIsAuthor.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure?\nWhen you Click Yes, You will login again to use this account!");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    // change account and role here
                    // find user by email
                    Users user = userDao.getUserByEmail(session.getEmail()).get(0);
                    // copy account user to artist
                    artistDao.addArtist(user.getUsername(), user.getEmail(), user.getPassword(), user.getAvatarUrl());
                    // add role user
                    artistDao.addRoleArtist(artistDao.getArtistByEmail(session.getEmail()).get(0).getId(), 3);
                    // delete role user
                    userDao.deleteUserRoleByUserId(user.getId());
                    // delete album and comment of user if exist

                    // TO DO

                    // delete account user
                    userDao.deleteUserById(user.getId());
                    // close activity
                    dialog.dismiss();
                    finish();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        });
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
                binding.imgProfile.setImageBitmap(bitmap);
                uri = imageUri;
                ImageUploader uploader = new ImageUploader();
                uploader.uploadImage(this, uri, new ImageUploader.UploadCallbackListener() {
                    @Override
                    public void onUploadSuccess(String url) {
                        if (check_user){
                            userDao.updateAvatarUser(1, url);
                        }else {
                            artistDao.updateAvatarArtist(1,url);
                        }

                        addEvents();
                        loadData();
                    }
                    @Override
                    public void onUploadError(String error) {
                        Toast.makeText(ProfileActivity.this, "Image upload failed: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}