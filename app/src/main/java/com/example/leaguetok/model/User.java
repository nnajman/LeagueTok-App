package com.example.leaguetok.model;

import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User implements Serializable {    
	@PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String photoUrl;
    private Long lastUpdated;
    private boolean isDeleted;
    private boolean isAdmin;

    public User(String id, String name, String photoUrl, Long lastUpdated, boolean isDeleted, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.lastUpdated = lastUpdated;
		this.isDeleted = isDeleted;
		this.isAdmin = isAdmin;
	}

    public User(){}

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

    public boolean isAdmin() { return isAdmin; }

    public void setId(@NonNull String id) {
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

    public void setAdmin(boolean admin) { isAdmin = admin; }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("photoUrl", photoUrl);
        result.put("isDeleted", isDeleted);
        result.put("isAdmin", isAdmin);
        return result;
    }

    public User fromMap(JSONObject json) throws JSONException {
        id = json.getString("id");
        name = json.getString("name");
        photoUrl = json.getString("photoUrl");
        lastUpdated = json.getLong("lastUpdated");
        isDeleted = json.getBoolean("isDeleted");
        isAdmin = json.getBoolean("isAdmin");
        return this;
    }
}
