package com.example.musicapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicapp.DAO.Song_DAO;
import com.example.musicapp.DAO.StyleDAO;
import com.example.musicapp.R;
import com.example.musicapp.adapters.SongsAdapter;
import com.example.musicapp.adapters.StylesAdapter;
import com.example.musicapp.databinding.ActivityAddMusicBinding;
import com.example.musicapp.databinding.ActivityEditStyleMusicBinding;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Song;
import com.example.musicapp.models.Style;
import java.util.ArrayList;
import java.util.List;

public class edit_Style_Music extends AppCompatActivity {
    ActivityEditStyleMusicBinding binding;
    StylesAdapter styleAdapter;
    private StyleDAO styleDao;
    Style style;
    EditText Nametv,Detaltv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEditStyleMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        styleDao = new StyleDAO(dbHelper);
        initData();
        loadData();
        addEvent();
        seupUI();
    }

    private void seupUI() {
        Nametv = findViewById(R.id.etStyleName);
        Detaltv = findViewById(R.id.etStyleDetail);
    }

    private boolean addStyle() {
        Style style = new Style();
        String name = binding.etStyleName.getText().toString().trim();
        String detail = binding.etStyleDetail.getText().toString().trim();
        if (name.isEmpty() || detail.isEmpty()) {
            return false;
        }
        style.setName(name);
        style.setDetail(detail);
        return styleDao.addStyle(style);
    }

    private void addEvent() {
        binding.btnAddStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addStyle()) {
                    Toast.makeText(edit_Style_Music.this, "Success", Toast.LENGTH_SHORT).show();
                    loadData(); // Refresh the list after adding
                } else {
                    Toast.makeText(edit_Style_Music.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.btnDeleteStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                styleDao.deleteStyle(style.getId());
            }
        });
        binding.lvStyles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Style style1 = (Style) styleAdapter.getItem(position);
                style = style1;
                Nametv.setText(style.getName());
                Detaltv.setText(style.getDetail());
                loadData();
            }
        });
    }

    private void loadData() {
        styleAdapter = new StylesAdapter(edit_Style_Music.this, R.layout.item_song, initData());
        binding.lvStyles.setAdapter(styleAdapter);
    }

    private List<Style> initData() {
        if (styleDao != null) {
            return styleDao.getAllStyles();
        } else {
            Log.e("EditStyleMusicActivity", "StyleDAO is not initialized.");
            return new ArrayList<>();
        }
    }

}