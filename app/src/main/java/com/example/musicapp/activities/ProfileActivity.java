package com.example.musicapp.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.DAO.Artist_DAO;
import com.example.musicapp.DAO.User_DAO;
import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityProfileBinding;
import com.example.musicapp.models.Artists;
import com.example.musicapp.models.Session;
import com.example.musicapp.models.SessionManager;
import com.example.musicapp.models.Users;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    User_DAO userDao;
    Artist_DAO artistDao;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userDao = new User_DAO(this);
        artistDao = new Artist_DAO(this);
        Intent intent = getIntent();
        session = SessionManager.getInstance().getSession();
        addEvents();
        loadData();
    }

    private void loadData() {
        // load data from database and set to view here
        // find user by email and set to view here
        Users user = userDao.getUserByEmail(session.getEmail()).get(0);
        Artists artist = artistDao.getArtistByEmail(session.getEmail()).get(0);
        if(artist != null){
            binding.txtArtist.setText("Yes");
            binding.txtName.setText(artist.getName());
            binding.txtEmail.setText(artist.getEmail());
            binding.txtIsAuthor.setText("Bạn đã là nhà sáng tác!");
            // close event click txtIsAuthor
            binding.txtIsAuthor.setOnClickListener(null);
            if(artist.getAvatar() != null){
                // set image to imgProfile by url
                // TO DO
            }
        } else {
            binding.txtArtist.setText("No");
            binding.txtName.setText(user.getUsername());
            binding.txtEmail.setText(user.getEmail());
            if(user.getAvatarUrl() != null){
                // set image to imgProfile by url
                // TO DO
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
            if(session.getRole() == 3){
                // change image to artist
                // TO DO
                //artistDao.updateArtistById(artistDao.getArtistByEmail(session.getEmail()).get(0).getId(), "default.png");
            } else {
                // change image to user
                // TO DO
                //userDao.updateUserById(userDao.getUserByEmail(session.getEmail()).get(0).getId(), "default.png");
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
}