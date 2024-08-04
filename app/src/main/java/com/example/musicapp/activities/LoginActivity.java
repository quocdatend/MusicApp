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
import com.example.musicapp.adapters.UserAdaptar;
import com.example.musicapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    UserAdaptar userAdaptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userAdaptar = new UserAdaptar(this);
        addEvents();

    }
    private void setupRegisterLink() {
        TextView textView = findViewById(R.id.txtRegister);
        SpannableString spannableString = new SpannableString("Don't have an account? Register");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // Xử lý sự kiện khi người dùng nhấn vào "Register"
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        };
        // Đặt vị trí của từ "Register" trong chuỗi
        int startIndex = spannableString.toString().indexOf("Register");
        int endIndex = startIndex + "Register".length();
        // Áp dụng ClickableSpan cho từ "Register"
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Tùy chọn: Thêm màu và gạch chân cho link
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Đặt text cho TextView
        textView.setText(spannableString);
        // Đảm bảo TextView có thể nhận sự kiện click
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        // Xử lý sự kiện khi người dùng nhấn nút đăng ký
    }
    private void addEvents() {
        setupRegisterLink();
        // Sử lý sự kiện khi người dùng nhấn link forgot password trong textview
        binding.txtForgotPassword.setOnClickListener(v -> {
            // Xử lý sự kiện khi người dùng nhấn nút quên mật khẩu
//            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
//            startActivity(intent);
        });
    }
}