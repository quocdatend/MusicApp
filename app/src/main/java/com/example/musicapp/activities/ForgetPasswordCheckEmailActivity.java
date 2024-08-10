package com.example.musicapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.DAO.User_DAO;
import com.example.musicapp.R;
import com.example.musicapp.adapters.SendEmail;
import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.databinding.ActivityForgetPasswordCheckEmailBinding;

public class ForgetPasswordCheckEmailActivity extends AppCompatActivity {
    ActivityForgetPasswordCheckEmailBinding binding;
    User_DAO userDao;
    private static int resetCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForgetPasswordCheckEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userDao = new User_DAO(this);
        addEvents();
    }
    private void addEvents() {
        // Xử lý sự kiện khi người dùng nhấn nút Submit
        binding.btnSubmit.setOnClickListener(v -> {
            SendEmail se = new SendEmail();
            resetCode = se.generateResetCode();
            EditText email = findViewById(R.id.editTextEmail);
            String subject = "Reset Password";
            String message = "Chúng tôi đã nhận được yêu cầu thay đôi mật khẩu từ bạn trên app của chúng tôi. Để hoàn tất quá trình thay đổi mật khẩu, vui lòng sử dụng mã xác thực dưới đây:\n" +
                    "\n" +
                    "Mã xác thực: " + resetCode + "\n" +
                    "\n" +
                    "Vui lòng nhập mã này vào trang xác thực tài khoản trong vòng 10 phút kể từ khi nhận được email này. Nếu bạn không yêu cầu thay đổi mật khẩu, xin vui lòng bỏ qua email này.\n" +
                    "\n" +
                    "Nếu bạn gặp bất kỳ vấn đề gì hoặc cần hỗ trợ thêm, vui lòng liên hệ với bộ phận hỗ trợ khách hàng của chúng tôi.\n" +
                    "\n" +
                    "Trân trọng,\n" +
                    "Nhóm 5";
            SendEmail sendEmail;
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_error);
            TextView txtError = dialog.findViewById(R.id.txtError);
            // check not null
            if (email.getText().length() == 0) {
                txtError.setText("Please enter your email");
                dialog.show();
            } else {
                // check email
                if(userDao.getUserByEmail(email.getText().toString()).toArray().length != 0) {
                    sendEmail = new SendEmail(email.getText().toString(), subject, message);
                    sendEmail.execute();
                    Intent intent = new Intent(ForgetPasswordCheckEmailActivity.this, ForgetPasswordCheckCodeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("resetCode", resetCode);
                    //Log.d("resetCode", String.valueOf(resetCode));
                    bundle.putString("email", email.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
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