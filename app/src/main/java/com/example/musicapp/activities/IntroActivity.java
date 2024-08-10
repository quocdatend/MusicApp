package com.example.musicapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.databinding.ActivityIntroBinding;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;
    private static final String DATABASE_NAME = "AppMusic.db";
    private static final String DB_PATH = "/databases/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prepareDB();
        addEvents();
    }

    private void addEvents() {
        binding.btnStart.setOnClickListener(v -> {
            // Xử lý sự kiện khi người dùng nhấn nút Start
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        binding.btnRegister.setOnClickListener(v -> {
            // Xử lý sự kiện khi người dùng nhấn nút Register
            Intent intent = new Intent(IntroActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void prepareDB() {
        try{
            File file = getDatabasePath(DATABASE_NAME);
            if(!file.exists()) {
                if(CopyDB()) {
                    Toast.makeText(this, "Copy DB success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Copy DB fail", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("Error: ", e.toString());
        }
    }

    private boolean CopyDB() {
        String dbPath = getApplicationInfo().dataDir + DB_PATH + DATABASE_NAME;
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            File file = new File(getApplicationInfo().dataDir + DB_PATH );
            if(!file.exists()) {
                file.mkdir();
            }
            OutputStream outputStream = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                outputStream = Files.newOutputStream(Paths.get(dbPath));
            }
            byte[] buffer = new byte[1024];
            int lenght;
            while ((lenght=inputStream.read(buffer))>0){
                outputStream.write(buffer,0, lenght);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}