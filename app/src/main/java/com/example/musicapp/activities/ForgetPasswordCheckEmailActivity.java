package com.example.musicapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.adapters.SendEmail;
import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.databinding.ActivityForgetPasswordCheckEmailBinding;

public class ForgetPasswordCheckEmailActivity extends AppCompatActivity {
    ActivityForgetPasswordCheckEmailBinding binding;
    UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgetPasswordCheckEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userAdapter = new UserAdapter(this);
        addEvents();
    }
    private void addEvents() {
        // Xử lý sự kiện khi người dùng nhấn nút Submit
        binding.btnSubmit.setOnClickListener(v -> {
            String email = binding.editTextEmail.getText().toString();
            String subject = "Reset Password";
            String message = "";
            SendEmail sendEmail;
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_error);
            TextView txtError = dialog.findViewById(R.id.txtError);
            if (email.isEmpty()) {
                txtError.setText("Please enter your email");
                dialog.show();
            } else {
                // check email
                if(userAdapter.getUserByEmail(email) != null) {
                    sendEmail = new SendEmail(email, subject, message);
                    sendEmail.sendEmail();
                } else {
                    txtError.setText("Email not found");
                    dialog.show();
                }
            }
        });
        // Xử lý sự kiện khi người dùng nhấn nút Back
        binding.imgBack.setOnClickListener(v -> {
            // Xử lý logic khi người dùng nhấn nút Back
            finish();
        });
    }
}