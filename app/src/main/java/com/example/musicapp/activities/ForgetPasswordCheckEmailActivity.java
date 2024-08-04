package com.example.musicapp.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityForgetPasswordCheckEmailBinding;

public class ForgetPasswordCheckEmailActivity extends AppCompatActivity {
    ActivityForgetPasswordCheckEmailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgetPasswordCheckEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addEvents();
    }
    private void addEvents() {
        
    }
}