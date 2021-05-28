package com.example.leaguetok.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private Long lastUpdated;
    private boolean isDeleted;

    public User(@NonNull String id, String name, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public User(){}

    @NonNull
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
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
        result.put("name", name);
        result.put("isDeleted", isDeleted);
        return result;
    }

    public User fromMap(JSONObject json) throws JSONException {
        id = json.getString("id");
        name = json.getString("name");
        lastUpdated = json.getLong("lastUpdated");
        isDeleted = json.getBoolean("isDeleted");
        return this;
    }
}
