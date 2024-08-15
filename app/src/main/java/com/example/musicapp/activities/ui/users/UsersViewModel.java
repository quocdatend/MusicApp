package com.example.musicapp.activities.ui.users;

import static java.security.AccessController.getContext;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.models.Users;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {

    private final MutableLiveData<List<Users>> mText;
    private UsersFragment usersFragment = new UsersFragment();
    public UsersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(usersFragment.userAdapters.getAllUsers());
    }

    public LiveData<List<Users>> getUsers() {
        return mText;
    }
}
