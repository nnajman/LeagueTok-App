package com.example.leaguetok.model;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.example.leaguetok.LeagueTokApplication;

import java.util.List;

public class Model {
    public interface DataAsyncListener<T> {
        void onComplete(T data);
        void onProgress(int progress);
    }

    public interface AsyncListener<T> {
        void onComplete(T data);
    }

    public final static Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    NodeService nodejsService = new NodeService();
    ModelSql modelSql = new ModelSql();
    private LiveData<List<OriginalVideo>> origVideosList;

    public void uploadVideo(Uri videoUri, String uid, String origName,  DataAsyncListener<String> listener) {
        modelFirebase.uploadVideo(videoUri, uid, origName, listener);
    }

    public LiveData<List<OriginalVideo>> getAllOriginalVideos(AsyncListener listener) {
        if (origVideosList == null) {
            origVideosList = AppLocalDB.db.originalVideoDao().getAllOriginalVideos();
            refreshAllOrigVideos(listener);
        }
        else {
            if(listener != null) listener.onComplete(null);
        }

        return origVideosList;
    }

    public void refreshAllOrigVideos(AsyncListener listener) {
        Long lastUpdated = LeagueTokApplication.context
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("origVideosLastUpdateDate", 0);
        nodejsService.getAllOrigVideos(lastUpdated, new AsyncListener<List<OriginalVideo>>() {
            @Override
            public void onComplete(List<OriginalVideo> data) {
                long lastUpdated = 0;

                for (OriginalVideo origVideo : data) {
                    if (origVideo.isDeleted()) {
                        modelSql.deleteOrigVideo(origVideo, null);
                    }
                    else {
                        modelSql.insertOrigVideo(origVideo, null);
                        if(origVideo.getLastUpdated() > lastUpdated) {
                            lastUpdated = origVideo.getLastUpdated();
                        }
                    }
                }

                LeagueTokApplication.context
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("origVideosLastUpdateDate", lastUpdated)
                        .apply();

                if (listener != null) {
                    listener.onComplete(null);
                }
            }
        });
    }

    public LiveData<OriginalVideo> getOrigVideoById(String id) {
        return modelSql.getOrigVideoById(id);
    }
}
