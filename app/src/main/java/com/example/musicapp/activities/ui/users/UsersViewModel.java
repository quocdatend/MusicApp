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

public class UsersViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Users>> mUsers;
    private UserAdapter userAdapter;

    public UsersViewModel(@NonNull Application application,MutableLiveData<List<Users>> mUsers) {
        super(application);
        this.mUsers = mUsers;
        mUsers = new MutableLiveData<>();
        userAdapter = new UserAdapter(application);
        mUsers.setValue(userAdapter.getAllUsers());
    }

    public LiveData<List<Users>> getUsers() {
        return mUsers;
    }
}
