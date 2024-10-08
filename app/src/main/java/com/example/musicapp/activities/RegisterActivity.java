package com.example.musicapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
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
import com.example.musicapp.databinding.ActivityRegisterBinding;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Users;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    User_DAO userDao;
    Admin_DAO adminDao;
    Artist_DAO artistDao;
    public static DatabaseHelper dbHelper;
    public static SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userDao = new User_DAO(this);
        adminDao = new Admin_DAO(this);
        artistDao = new Artist_DAO(this);
        dbHelper = new DatabaseHelper(this);
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
        // Xử lý logic khi người dùng nhấn nút Register
        binding.btnRegister.setOnClickListener(v -> {
           EditText username = findViewById(R.id.editTextTextPersonName);
           EditText email = findViewById(R.id.editTextEmail);
           EditText password = findViewById(R.id.editTextTextPassword);
           EditText confirmPassword = findViewById(R.id.editTextTextConfirmPassword);
           Dialog dialog = new Dialog(this);
           dialog.setContentView(R.layout.dialog_error);
           TextView txtError = dialog.findViewById(R.id.txtError);
           // Kiểm tra các trường dữ liệu có rỗng hay không
           if (username.getText().length() == 0 || email.getText().length() == 0 || password.getText().length() == 0 || confirmPassword.getText().length() == 0) {
               // Hiển thị thông báo lỗi và thay text view lỗi
               txtError.setText("Please fill in all fields" + " " + username.getText().toString() + " " + email.getText().toString() + " " + password.getText().toString() + " " + confirmPassword.getText().toString());
               dialog.show();
           } else {
               // Username must be at least 6 characters long
               if (username.length() < 6) {
                   // Hiển thị thông báo lỗi và thay text view lỗi
                   txtError.setText("Username must be at least 6 characters long");
                   dialog.show();
               }
               // Email must be a valid email address
               else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                   // Hiển thị thông báo lỗi và thay text view lỗi
                   txtError.setText("Email must be a valid email address");
                   dialog.show();
               }
               // Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit
               else if (password.length() < 8 || !password.getText().toString().matches(".*[A-Z].*") || !password.getText().toString().matches(".*[a-z].*") || !password.getText().toString().matches(".*\\d.*")) {
                   // Hiển thị thông báo lỗi và thay text view lỗi
                   txtError.setText("Passwords must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
                   dialog.show();
               }
               // Kiểm tra mật khẩu và xác nhận mật khẩu có khớp hay không
               else if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                   // Hiển thị thông báo lỗi và thay text view lỗi
                   txtError.setText("Passwords do not match");
                   dialog.show();
               } else {
                   // Kiểm tra username đã tồn tại hay chưa
                   if(userDao.getUserByUsername(username.getText().toString()).toArray().length != 0 || adminDao.getAdminByName(username.getText().toString()).toArray().length != 0 || artistDao.getArtistByName(username.getText().toString()).toArray().length != 0) {
                       // Hiển thị thông báo lỗi và thay text view lỗi
                       txtError.setText("Username already exists");
                       dialog.show();
                   }
                   // Kiểm tra email đã tồn tại hay chưa
                   else if(userDao.getUserByEmail(email.getText().toString()).toArray().length != 0 || adminDao.getAdminByEmail(email.getText().toString()).toArray().length != 0 || artistDao.getArtistByEmail(email.getText().toString()).toArray().length != 0) {
                       // Hiển thị thông báo lỗi và thay text view lỗi
                       txtError.setText("Email already exists");
                       dialog.show();
                   } else {
                       // Thêm người dùng vào database
                       SendEmail se = new SendEmail();
                       String newPass = se.hashPassword(password.getText().toString());
                       userDao.addUser(username.getText().toString(), newPass, email.getText().toString());
                       List<Users> userList = userDao.getUserByUsername(username.getText().toString());
                       db = dbHelper.getWritableDatabase();
                       // insert data to table user_role
                       String sql1 = "INSERT INTO USER_ROLE (USER_ID, ROLE_ID) VALUES (?, ?)";
                       db.execSQL(sql1, new Object[]{userList.get(0).getId(), 2});
                       finish();
                   }
               }
           }
        });
    }
}