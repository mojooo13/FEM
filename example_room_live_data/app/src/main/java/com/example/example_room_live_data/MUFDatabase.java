package com.example.example_room_live_data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.example_room_live_data.dao.UserDao;
import com.example.example_room_live_data.data.User;

// @Database(entities = {User.class, xyz.class}, version = 1)
@Database(entities = {User.class}, version = 1)
public abstract class MUFDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
}
