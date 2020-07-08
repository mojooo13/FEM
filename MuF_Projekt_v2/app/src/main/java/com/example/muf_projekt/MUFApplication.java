package com.example.muf_projekt;

import android.app.Application;

import androidx.room.Room;

public class MUFApplication extends Application {

    private Database database;

    public Database getDataBase() {
        if (database==null){
            database = Room.databaseBuilder(this, Database.class, "app_db_2")
                    .allowMainThreadQueries() // <-- evil!!
                    .build();
        }
        return database;
    }
}