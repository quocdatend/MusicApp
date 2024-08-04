package com.example.musicapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addEvents();
    }
    private void setupRegisterLink() {
        TextView textView = findViewById(R.id.txtLogin);
        SpannableString spannableString = new SpannableString("Do you have an account? Login");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Xử lý sự kiện khi người dùng nhấn vào "Login"
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
        // Đặt vị trí của từ "Login" trong chuỗi
        int startIndex = spannableString.toString().indexOf("Login");
        int endIndex = startIndex + "Login".length();
        // Áp dụng ClickableSpan cho từ "Login"
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Tùy chọn: Thêm màu và gạch chân cho link
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Đặt text cho TextView
        textView.setText(spannableString);
        // Đảm bảo TextView có thể nhận sự kiện click
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private void addEvents() {
        setupRegisterLink();
    }
}