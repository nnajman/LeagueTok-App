package com.example.leaguetok.model;

import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class User implements Serializable {
    private String id;
    private String name;
    private String photoUrl;
    private Long lastUpdated;
    private boolean isDeleted;

    public User(String id, String name, String photoUrl, Long lastUpdated, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.lastUpdated = lastUpdated;
        this.isDeleted = isDeleted;
    }

    public User(){}

    @NotNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() { return photoUrl; }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public boolean isDeleted() {
        return isDeleted;
    }


    public void setId(@NotNull String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("photoUrl", photoUrl);
        result.put("isDeleted", isDeleted);
        return result;
    }

    public User fromMap(JSONObject json) throws JSONException {
        id = json.getString("id");
        name = json.getString("name");
        photoUrl = json.getString("photoUrl");
        lastUpdated = json.getLong("lastUpdated");
        isDeleted = json.getBoolean("isDeleted");
        return this;
    }
}
