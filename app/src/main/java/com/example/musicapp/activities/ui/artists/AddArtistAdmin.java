package com.example.musicapp.activities.ui.artists;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.DAO.Admin_DAO;
import com.example.musicapp.DAO.Artist_DAO;
import com.example.musicapp.DAO.User_DAO;
import com.example.musicapp.R;
import com.example.musicapp.adapters.SendEmail;
import com.example.musicapp.databinding.ActivityAddArtistAdminBinding;
import com.example.musicapp.models.Artists;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Users;

import java.util.List;

public class AddArtistAdmin extends AppCompatActivity {
    ActivityAddArtistAdminBinding binding;
    User_DAO userDao;
    Admin_DAO adminDao;
    Artist_DAO artistDao;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddArtistAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);
        artistDao = new Artist_DAO(this);
        adminDao = new Admin_DAO(this);
        userDao = new User_DAO(this);
        addEvents();
    }

    private void addEvents() {
        binding.imgBack.setOnClickListener(v->{
            finish();
        });
        binding.btnSubmit.setOnClickListener(v->{
            EditText username = findViewById(R.id.edtName);
            EditText email = findViewById(R.id.edtEmail);
            EditText password = findViewById(R.id.edtPass);
            EditText confirmPassword = findViewById(R.id.edtConfirmPass);
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
                        artistDao.addArtist(username.getText().toString(), email.getText().toString(), newPass,  null);
                        List<Artists> artistList = artistDao.getArtistByName(username.getText().toString());
                        db = dbHelper.getWritableDatabase();
                        // insert data to table artist_role
                        artistDao.addRoleArtist(artistList.get(0).getId(), 3);
                        Dialog dialogSuccess = new Dialog(this);
                        dialogSuccess.setContentView(R.layout.dialog_success);
                        TextView txtSuccess = dialogSuccess.findViewById(R.id.txtSuccess);
                        txtSuccess.setText("Artist added successfully");
                        dialogSuccess.show();
                    }
                }
            }
        });
    }
}