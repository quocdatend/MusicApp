package com.example.musicapp.activities;

import android.os.Bundle;
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
        binding.btnTestDatabase.setOnClickListener(v -> {
            userAdaptar.addUser("test", "test", "test", "test");
            Toast.makeText(IntroActivity.this, "User added", Toast.LENGTH_SHORT).show();
        });
    }
}