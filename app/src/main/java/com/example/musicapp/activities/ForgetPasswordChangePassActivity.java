package com.example.musicapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityForgetPasswordChangePassBinding;

public class ForgetPasswordChangePassActivity extends AppCompatActivity {
    ActivityForgetPasswordChangePassBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgetPasswordChangePassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addEvents();
    }
    private void addEvents() {
        // Xử lý sự kiện khi người dùng nhấn nút Back
        binding.imgBack.setOnClickListener(v -> {
        // Xử lý logic khi người dùng nhấn nút Back
            finish();
        });
        // Xử lý sự kiện khi người dùng nhấn nút Submit
        binding.btnUpdate.setOnClickListener(v -> {
            // Xử lý logic khi người dùng nhấn nút Submit
        });
    }
}