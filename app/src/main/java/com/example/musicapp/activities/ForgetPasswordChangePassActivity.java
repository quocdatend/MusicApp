package com.example.musicapp.activities;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.adapters.SendEmail;
import com.example.musicapp.databinding.ActivityForgetPasswordChangePassBinding;
import com.example.musicapp.models.DatabaseHelper;

import java.sql.PreparedStatement;

public class ForgetPasswordChangePassActivity extends AppCompatActivity {
    ActivityForgetPasswordChangePassBinding binding;
    SQLiteDatabase db;
    DatabaseHelper dbHelper;
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
            EditText password = findViewById(R.id.editTextPassword);
            EditText confirmPassword = findViewById(R.id.editTextConfirmPassword);
            SendEmail se = new SendEmail();
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_error);
            TextView txtError = dialog.findViewById(R.id.txtError);
            String email = getIntent().getStringExtra("email");
            // check not null
            if(password.getText().length() == 0 || confirmPassword.getText().length() == 0) {
                txtError.setText("Please enter your password");
                dialog.show();
            } else if(password.getText().toString().equals(confirmPassword.getText().toString())) {
                if (password.length() < 8 || !password.getText().toString().matches(".*[A-Z].*") || !password.getText().toString().matches(".*[a-z].*") || !password.getText().toString().matches(".*\\d.*")) {
                    // Hiển thị thông báo lỗi và thay text view lỗi
                    txtError.setText("Passwords must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
                    dialog.show();
                } else {
                    // use query sql to change password
                    dbHelper = new DatabaseHelper(this);
                    db = dbHelper.getWritableDatabase();
                    String newPass = se.hashPassword(password.getText().toString());
                    String sql = "UPDATE USERS SET PASS = ? WHERE EMAIL = ?";
                    db.execSQL(sql, new Object[]{newPass, email});
                    finish();
                }
            } else {
                txtError.setText("Password not match");
                dialog.show();
            }
        });
    }
}