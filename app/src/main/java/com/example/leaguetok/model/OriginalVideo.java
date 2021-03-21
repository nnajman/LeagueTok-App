package com.example.leaguetok.model;

import androidx.room.Entity;

@Entity
public class OriginalVideo {
    private String id;
    private String name;
    private String uri;
    private String performer;
    private Long lastUpdated;

    // region Constructors
    public OriginalVideo(String id, String name, String uri, String performer) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.performer = performer;
    }

    OriginalVideo(){}
    //endregion

    // region Getters
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

    public Long getLastUpdated() {
        return lastUpdated;
    }
    // endregion

    // region Setters
    public void setId(String id) {
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

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    // endregion

    // region Functions

    // endregion
}
