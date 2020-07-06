package com.example.muf_projekt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StatsFragment extends Fragment {

    private Database database;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        Bundle args = getArguments();
        StatsFragmentArgs statsFragmentArgs = null;

        if(args != null){
            statsFragmentArgs = StatsFragmentArgs.fromBundle(args);
        }

        if(statsFragmentArgs != null) {
            TextView tv = view.findViewById(R.id.stats);
            tv.setText(statsFragmentArgs.getDisplayString());
        }

        return view;
    }
}
