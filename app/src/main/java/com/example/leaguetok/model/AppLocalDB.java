package com.example.leaguetok.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.leaguetok.LeagueTokApplication;

// If entity is modified, change the version to higher number
@Database(entities = {OriginalVideo.class}, version = 1)
abstract class AppLocalDBRepository extends RoomDatabase {
    public abstract OriginalVideoDao originalVideoDao();
}

public class AppLocalDB {
    static public AppLocalDBRepository db =
            Room.databaseBuilder(LeagueTokApplication.context,
                    AppLocalDBRepository.class,
                    "LeagueTok.db")
                    .fallbackToDestructiveMigration()
                    .build();
}