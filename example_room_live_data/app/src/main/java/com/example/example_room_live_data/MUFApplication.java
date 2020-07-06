package com.example.example_room_live_data;

import android.app.Application;

import androidx.room.Room;

public class MUFApplication extends Application {
    private MUFDatabase mufDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        mufDatabase = Room
                .databaseBuilder(this, MUFDatabase.class, "muf-projekt")
                .build();
    }

    public MUFDatabase getDatabase(){
        return mufDatabase;
    }
}
