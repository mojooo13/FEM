package com.example.example_room_live_data.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.example_room_live_data.R;
import com.example.example_room_live_data.viemodel.UserViewModel;

public class UserDisplayFragment extends Fragment {
    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(
                getActivity(),
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())
        ).get(UserViewModel.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_userdisplay, container, false);
        TextView email = v.findViewById(R.id.email_display);
        TextView name = v.findViewById(R.id.name_display);
        userViewModel.userInserted().observe(this, user -> {
            email.setText(user.getEmail());
            name.setText(user.getName());
        });
        return v;
    }
}
