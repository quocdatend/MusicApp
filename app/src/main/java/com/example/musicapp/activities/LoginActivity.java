package com.example.musicapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapp.DAO.Admin_DAO;
import com.example.musicapp.DAO.Artist_DAO;
import com.example.musicapp.DAO.User_DAO;
import com.example.musicapp.R;
import com.example.musicapp.adapters.SendEmail;
import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.databinding.ActivityLoginBinding;
import com.example.musicapp.models.Admin;
import com.example.musicapp.models.Artists;
import com.example.musicapp.models.Session;
import com.example.musicapp.models.Users;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    User_DAO userDao;
    Admin_DAO adminDao;
    Artist_DAO artistDao;
    SendEmail se;
    public static Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userDao = new User_DAO(this);
        adminDao = new Admin_DAO(this);
        artistDao = new Artist_DAO(this);
        se = new SendEmail();
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
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordCheckEmailActivity.class);
            startActivity(intent);
        });
        binding.btnSignIn.setOnClickListener(v->{
            EditText edtUsername = findViewById(R.id.editTextTextPersonName);
            EditText edtPassword = findViewById(R.id.editTextTextPassword);
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            Dialog dialog = new Dialog(this);
            // Check not null
            if(username.isEmpty() || password.isEmpty()){
                dialog.setContentView(R.layout.dialog_error);
                TextView txtError = dialog.findViewById(R.id.txtError);
                txtError.setText("Please fill in all fields");
                dialog.show();
            }else{
                // hash pass
                password = se.hashPassword(password);
                // Check username and password
                List<Users> userList = userDao.getUserByUsername(username);
                List<Admin> adminList = adminDao.getAdminByName(username);
                List<Artists> artistsList = artistDao.getArtistByEmail(username);
                if((String.valueOf(userList.toArray().length).equals("0")) && (String.valueOf(adminList.toArray().length).equals("0")) && (String.valueOf(artistsList.toArray().length).equals("0"))) {
                    dialog.setContentView(R.layout.dialog_error);
                    TextView txtError = dialog.findViewById(R.id.txtError);
                    txtError.setText("Username not found");
                    dialog.show();
                } else if (userList.toArray().length != 0){
                    // check password
                    if(!userList.get(0).getPassword().equals(password)){
                        dialog.setContentView(R.layout.dialog_error);
                        TextView txtError = dialog.findViewById(R.id.txtError);
                        txtError.setText("Password incorrect");
                        dialog.show();
                    } else {
                        session = new Session();
                        session.setCode(userList.get(0).getId());
                        session.setName(username);
                        session.setPassword(password);
                        session.setEmail(userList.get(0).getEmail());
                        session.setRole(2);
                    }
                } else if (adminList.toArray().length != 0){
                    // check password
                    if(!adminList.get(0).getPassword().equals(password)){
                        dialog.setContentView(R.layout.dialog_error);
                        TextView txtError = dialog.findViewById(R.id.txtError);
                        txtError.setText("Password incorrect");
                        dialog.show();
                    } else {
                        session = new Session();
                        session.setCode(adminList.get(0).getId());
                        session.setName(username);
                        session.setPassword(password);
                        session.setEmail(adminList.get(0).getEmail());
                        session.setRole(1);
                        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (artistsList.toArray().length != 0){
                    // check password
                    if(!artistsList.get(0).getPassword().equals(password)){
                        dialog.setContentView(R.layout.dialog_error);
                        TextView txtError = dialog.findViewById(R.id.txtError);
                        txtError.setText("Password incorrect");
                        dialog.show();
                    } else {
                        session = new Session();
                        session.setCode(artistsList.get(0).getId());
                        session.setName(username);
                        session.setPassword(password);
                        session.setEmail(artistsList.get(0).getEmail());
                        session.setRole(3);
                    }
                }
            }
        });
    }
}