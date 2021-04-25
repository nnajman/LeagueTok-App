package com.example.leaguetok.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Entity
public class OriginalVideo {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String uri;
    private String performer;
    private Long uploadDate;
    private Long lastUpdated;
    private boolean isDeleted;

    // region Constructors
    public OriginalVideo(@NotNull String id, String name, String uri, String performer, Long uploadDate, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.performer = performer;
        this.uploadDate = uploadDate;
        this.isDeleted = isDeleted;
    }

    OriginalVideo(){}
    //endregion

    // region Getters
    @NotNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getPerformer() {
        return performer;
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
    // endregion

    // region Setters
    public void setId(@NotNull String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
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
    // endregion

    // region Functions
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("uri", uri);
        result.put("performer", performer);
        result.put("uploadDate", uploadDate);
        result.put("isDeleted", isDeleted);
        return result;
    }

    public OriginalVideo fromMap(JSONObject json) throws JSONException {
        id = json.getString("id");
        name = json.getString("name");
        uri = json.getString("uri");
        performer = json.getString("performer");
        uploadDate = json.getLong("uploadDate");
        lastUpdated = json.getLong("lastUpdated");
        isDeleted = json.getBoolean("isDeleted");
        return this;
    }
    // endregion
}
