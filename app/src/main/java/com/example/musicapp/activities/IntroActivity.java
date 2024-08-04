package com.example.musicapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.adapters.UserAdaptar;
import com.example.musicapp.databinding.ActivityIntroBinding;

public class IntroActivity extends AppCompatActivity {
    ActivityIntroBinding binding;
    UserAdaptar userAdaptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userAdaptar = new UserAdaptar(this);
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
}