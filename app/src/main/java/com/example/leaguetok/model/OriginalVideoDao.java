package com.example.leaguetok.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OriginalVideoDao {
    @Query("select * from OriginalVideo order by uploadDate desc")
    LiveData<List<OriginalVideo>> getAllOriginalVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(OriginalVideo... originalVideos);

    @Delete
    void delete(OriginalVideo originalVideo);
}
