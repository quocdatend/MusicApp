package com.example.musicapp.activities.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.activities.ui.home.HomeViewModel;
import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.databinding.FragmentHomeBinding;
import com.example.musicapp.databinding.FragmentUsersBinding;

public class UsersFragment extends Fragment {
    private FragmentUsersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UsersViewModel usersViewModel =
                new ViewModelProvider(this).get(UsersViewModel.class);
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        UserAdapter userAdapter = new UserAdapter(getContext());
        final ListView listView = binding.lvUsers;
        usersViewModel.getUsers().observe(getViewLifecycleOwner(), users -> {
            ListAdapter listAdapter = (ListAdapter) userAdapter.getAllUsers();
            listView.setAdapter(listAdapter);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
