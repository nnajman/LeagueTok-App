package com.example.leaguetok.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ImitationVideo implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String url;
    private String uid;
    private String sourceId;
    private int score;
    private Long uploadDate;
    private Long lastUpdated;
    private boolean isDeleted;

    public ImitationVideo(@NonNull String id, String url, String uid, String sourceId, int score, Long uploadDate, boolean isDeleted) {
        this.id = id;
        this.url = url;
        this.uid = uid;
        this.sourceId = sourceId;
        this.score = score;
        this.uploadDate = uploadDate;
        this.isDeleted = isDeleted;
    }

    public ImitationVideo() {}

    @NonNull
    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getUid() {
        return uid;
    }

    public String getSourceId() {
        return sourceId;
    }

    public int getScore() {
        return score;
    }

    public Long getUploadDate() {
        return uploadDate;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setUploadDate(Long uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("url", url);
        result.put("uid", uid);
        result.put("sourceId", sourceId);
        result.put("score", score);
        result.put("uploadDate", uploadDate);
        result.put("isDeleted", isDeleted);
        return result;
    }

    public ImitationVideo fromMap(JSONObject json) throws JSONException {
        id = json.getString("id");
        url = json.getString("url");
        uid = json.getString("uid");
        sourceId = json.getString("sourceId");
        score = json.getInt("score");
        uploadDate = json.getLong("uploadDate");
        lastUpdated = json.getLong("lastUpdated");
        isDeleted = json.getBoolean("isDeleted");
        return this;
    }
}
