package com.example.leaguetok.model;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

public interface OriginalVideoDao {
    @Query("select * from OriginalVideo")
    LiveData<List<OriginalVideo>> getAllOriginalVideos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(OriginalVideo... originalVideos);

    @Delete
    void delete(OriginalVideo originalVideo);
}
