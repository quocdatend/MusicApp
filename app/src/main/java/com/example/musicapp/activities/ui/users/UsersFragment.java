package com.example.musicapp.activities.ui.users;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicapp.R;
import com.example.musicapp.activities.ui.home.HomeViewModel;
import com.example.musicapp.adapters.UserAdapter;
import com.example.musicapp.databinding.FragmentHomeBinding;
import com.example.musicapp.databinding.FragmentUsersBinding;
import com.example.musicapp.models.Users;

import java.util.ArrayList;

public class UsersFragment extends Fragment {
    private FragmentUsersBinding binding;
    public UserAdapter userAdapters;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        UsersViewModel usersViewModel =
//                new ViewModelProvider(this).get(UsersViewModel.class);
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final ListView listView = binding.lvUsers;
        userAdapters = new UserAdapter(requireContext());
        ArrayList<Users> users = (ArrayList<Users>) userAdapters.getAllUsers();
        ArrayAdapter<Users> adapter = new ArrayAdapter<Users>(requireContext(), R.layout.content_user_admin, users) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.content_user_admin, parent, false);
                }

                Users user = getItem(position);
                ImageView imgAvatar = convertView.findViewById(R.id.imgAvatar);
                TextView txtUser = convertView.findViewById(R.id.txtUsername);
                TextView txtEmail = convertView.findViewById(R.id.txtEmail);

                if (user != null) {
                    if (user.getAvatarUrl() != null) {
                        imgAvatar.setImageURI(Uri.parse(user.getAvatarUrl()));
                    } else {
                        imgAvatar.setImageResource(R.drawable.baseline_account_circle_24);
                    }
                    txtUser.setText("Username: " + user.getUsername());
                    txtEmail.setText("Email: " + user.getEmail());
                }

                return convertView;
            }
        };

        listView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
