package com.example.leaguetok.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImitationVideoDao {
    @Query("select * from ImitationVideo order by uploadDate desc")
    LiveData<List<ImitationVideo>> getAllImitationVideos();

    @Query("select * from ImitationVideo where sourceId = :sourceID order by uploadDate desc")
    LiveData<List<ImitationVideo>> getAllImitVideosBySourceID(String sourceID);

    @Query("select count(*) from ImitationVideo where sourceId = :sourceID")
    Integer getNumOfImitBySourceId(String sourceID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ImitationVideo... imitationVideos);

    @Delete
    void delete(ImitationVideo imitationVideo);
}
