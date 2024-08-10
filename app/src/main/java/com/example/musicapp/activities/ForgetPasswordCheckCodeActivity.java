package com.example.musicapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityForgetPasswordCheckCodeBinding;
import com.example.musicapp.databinding.ActivityForgetPasswordCheckEmailBinding;

public class ForgetPasswordCheckCodeActivity extends AppCompatActivity {
    ActivityForgetPasswordCheckCodeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgetPasswordCheckCodeBinding.inflate(getLayoutInflater());
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
        binding.btnSubmit.setOnClickListener(v -> {
            EditText code = findViewById(R.id.editTextCode);
            EditText confirmCode = findViewById(R.id.editTextConfirmCode);
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_error);
            TextView txtError = dialog.findViewById(R.id.txtError);
            // lay ma code tu intent
            int code_intent = getIntent().getIntExtra("resetCode", 0);
            String email_intent = getIntent().getStringExtra("email");
            // check not null
            if(code.getText().length() == 0 || confirmCode.getText().length() == 0) {
                txtError.setText("Please enter your code");
                dialog.show();
            } else if(code.getText().toString().equals(confirmCode.getText().toString())) {
                if(Integer.parseInt(code.getText().toString()) == code_intent) {
                    // Xử lý logic khi người dùng nhấn nút Submit
                    Intent intent = new Intent(ForgetPasswordCheckCodeActivity.this, ForgetPasswordChangePassActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email", email_intent);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    txtError.setText("Code is not correct");
                    dialog.show();
                }
            } else {
                txtError.setText("Code not match");
                dialog.show();
            }
        });
    }
}