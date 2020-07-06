package com.example.example_room_live_data.viemodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.example_room_live_data.MUFApplication;
import com.example.example_room_live_data.MUFDatabase;

public abstract class BaseViewModel extends AndroidViewModel {
    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public MUFDatabase getDatabase() {
        return ((MUFApplication) getApplication()).getDatabase();
    }
}
