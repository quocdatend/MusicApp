package com.example.musicapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.example.musicapp.DAO.AlbumDAO;
import com.example.musicapp.DAO.Song_DAO;
import com.example.musicapp.R;
import com.example.musicapp.adapters.CategoryAdapter;
import com.example.musicapp.adapters.SliderAdapter;
import com.example.musicapp.category.Category;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.models.Album;
import com.example.musicapp.models.DatabaseHelper;
import com.example.musicapp.models.Session;
import com.example.musicapp.models.SessionManager;
import com.example.musicapp.models.Song;
import com.example.musicapp.utils.OnCategoryClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MainActivity extends AppCompatActivity implements OnCategoryClickListener {
    private RecyclerView rcvCategory;
    private CategoryAdapter categoryAdapter;
    private ViewPager2 mviewPager2;
    private List<Slider> mListSlider;
    private Handler mhandler = new Handler(Looper.myLooper());
    ActivityMainBinding binding;
    private Cloudinary cloudinary;
    private TextView txt_MusicForYou;
    private AlbumDAO albumDAO;

    Session session;
    private Runnable mrunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mviewPager2.getCurrentItem();
            if (currentPosition == mListSlider.size() - 1) {
                mviewPager2.setCurrentItem(0);
            } else {
                mviewPager2.setCurrentItem(currentPosition + 1);
            }
        }
    };
    private Song_DAO songDao;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        songDao = new Song_DAO(dbHelper);
        albumDAO = new AlbumDAO(dbHelper);
        mviewPager2 = findViewById(R.id.view_paper_2);
        mListSlider = getListSlider();
        SliderAdapter sliderAdapter = new SliderAdapter(mListSlider);
        mviewPager2.setAdapter(sliderAdapter);
        mviewPager2.setOffscreenPageLimit(3);
        mviewPager2.setClipToPadding(false);
        mviewPager2.setClipChildren(false);
        session = SessionManager.getInstance().getSession();
        System.out.println(session.getCode());

        try {
            MediaManager.get();
        } catch (IllegalStateException e) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", "dap6ivvwp");
            config.put("api_key", "875469923979388");
            config.put("api_secret", "sT_lEC69UilqcB6NB6Fhn6kaZqU");
            MediaManager.init(this, config);
        }
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        mviewPager2.setPageTransformer(compositePageTransformer);
        mviewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mhandler.removeCallbacks(mrunnable);
                mhandler.postDelayed(mrunnable,2000);
            }
        });
        rcvCategory = findViewById(R.id.rcv_category);
        categoryAdapter = new CategoryAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvCategory.setLayoutManager(linearLayoutManager);
        categoryAdapter.setData(getListCategory());
        rcvCategory.setAdapter(categoryAdapter);
        txt_MusicForYou = findViewById(R.id.txt_MusicForYou);
        initbutonView();
    }


    private List<Category> getListCategory() {
        List<Category> listCategory = new ArrayList<>();
        List<Song> listMusic = new ArrayList<>();
        List<Song> listMusicbyAlbum = new ArrayList<>();
        listMusic = songDao.getAllMusicRecords();
        List<Album> albums = albumDAO.getaddAlbum();
        listCategory.add(new Category("Your Favorite Music", listMusic));
        listCategory.add(new Category("Recommend For You", listMusic));
        listCategory.add(new Category("Top Album ", listMusic));
        for (Album album: albums){
            listMusicbyAlbum = songDao.getAllMusicByAlbumId(album.getId());
            if (listMusicbyAlbum.size() > 0 ){
                listCategory.add(new Category("Best Of " + album.getTitle(), listMusicbyAlbum));
            }
        }
        return listCategory;
    }

    private List<Slider> getListSlider() {
        List<Slider> list = new ArrayList<>();
        list.add(new Slider(R.drawable.slider_1));
        list.add(new Slider(R.drawable.slider_2));
        list.add(new Slider(R.drawable.slider_3));
        list.add(new Slider(R.drawable.slider_4));
        return list;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mhandler.removeCallbacks(mrunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mhandler.postDelayed(mrunnable, 2000);
    }

    @Override
    public void onCategoryClick(Category category) {
        // Handle the category click event here
        List<Song> categorysong = category.getMusic();
        Intent intent = new Intent(this, ListMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("listsong", (Serializable) categorysong);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onMusicClick(Song song) {
        Intent intent = new Intent(MainActivity.this, PlayingSongActivity.class);
        intent.putExtra("position", song.getId());
        Bundle bundle = new Bundle();
        List<Song> songs1 = new ArrayList<>();
        songs1 = songDao.getAllMusicRecords();
        bundle.putSerializable("songs", (Serializable) songs1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void initbutonView() {
        ViewPager2 viewPager2 = findViewById(R.id.view_paper_2);
        RecyclerView rcvCategory = findViewById(R.id.rcv_category);
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnBrowser = findViewById(R.id.btnBrowser);
        ImageButton btnSearch = findViewById(R.id.btnSearch);
        ImageButton btnLibrary = findViewById(R.id.btnLibrary);

        btnHome.setColorFilter(getResources().getColor(R.color.black));

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        btnBrowser.setOnClickListener(v -> {
            List<Song> categorysong = songDao.getAllSongsByUserId(session.getCode());
            Intent intent = new Intent(this, ListMusicActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("listsong", (Serializable) categorysong);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(this, FindMusic.class);
            startActivity(intent);
        });
        btnLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}