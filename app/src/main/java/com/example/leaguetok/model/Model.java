package com.example.leaguetok.model;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.leaguetok.LeagueTokApplication;

import org.json.JSONObject;

import java.util.List;

public class Model {
    public interface DataAsyncListener<T> {
        void onComplete(T data);
        void onProgress(int progress);
    }

    public interface AsyncListener<T> {
        void onComplete(T data);
        void onError(T error);
    }

    public final static Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();
    NodeService nodejsService = new NodeService();
    ModelSql modelSql = new ModelSql();
    private LiveData<List<OriginalVideo>> origVideosList;
    private LiveData<List<ImitationVideo>> imitVideosList;

    public void uploadVideo(Uri videoUri, String uid, String origName,  DataAsyncListener<String> listener) {
        modelFirebase.uploadVideo(videoUri, uid, origName, listener);
    }

    public void uploadVideoToServer(String uri, String uid, String origVideoId, NodeService.RequestListener<JSONObject> listener) {
        nodejsService.uploadVideo(uri, uid, origVideoId, listener);
    }

    public void addNewUser(String uid, String fullName, AsyncListener listener) {
        nodejsService.addNewUser(uid, fullName, listener);
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

            @Override
            public void onError(List<OriginalVideo> error) {
                listener.onError(null);
            }
        });
    }

    public LiveData<OriginalVideo> getOrigVideoById(String id) {
        return modelSql.getOrigVideoById(id);
    }

    public LiveData<List<ImitationVideo>> getImitiationVideosByUid(String uid, AsyncListener listener) {
        LiveData<List<ImitationVideo>> userImitVideos =
                AppLocalDB.db.imitationVideoDao().getAllImitVideosByUid(uid);

        refreshUserImitVideos(uid, listener);
        return userImitVideos;
    }

    public LiveData<List<ImitationVideo>> getAllImitationVideos(AsyncListener listener) {
        if (imitVideosList == null) {
            imitVideosList = AppLocalDB.db.imitationVideoDao().getAllImitationVideos();
            refreshAllImitVideos(listener);
        }
        else {
            if(listener != null) listener.onComplete(null);
        }

        return imitVideosList;
    }

    public LiveData<List<ImitationVideo>> getAllImitVideosBySourceID(String sourceID) {
        return AppLocalDB.db.imitationVideoDao().getAllImitVideosBySourceID(sourceID);
    }

    public void getNumOfImitBySourceId(String sourceID, AsyncListener<Integer> listener) {
        modelSql.getNumOfImitBySourceId(sourceID, listener);
    }

    public void refreshAllImitVideos(AsyncListener listener) {
        Long lastUpdated = LeagueTokApplication.context
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("imitVideosLastUpdateDate", 0);
        nodejsService.getAllImitVideos(lastUpdated, new AsyncListener<List<ImitationVideo>>() {
            @Override
            public void onComplete(List<ImitationVideo> data) {
                long lastUpdated = 0;

                for (ImitationVideo imitVideo : data) {
                    if (imitVideo.isDeleted()) {
                        modelSql.deleteImitVideo(imitVideo, null);
                    }
                    else {
                        modelSql.insertImitVideo(imitVideo, null);
                        if(imitVideo.getLastUpdated() > lastUpdated) {
                            lastUpdated = imitVideo.getLastUpdated();
                        }
                    }
                }

                LeagueTokApplication.context
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("imitVideosLastUpdateDate", lastUpdated)
                        .apply();

                if (listener != null) {
                    listener.onComplete(null);
                }
            }

            @Override
            public void onError(List<ImitationVideo> error) {
                listener.onError(null);
            }
        });
    }

    public void refreshUserImitVideos(String uid, AsyncListener listener) {
        Long lastUpdated = LeagueTokApplication.context
                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                .getLong("userImitVideosLastUpdateDate", 0);
        nodejsService.getUserImitVideos(uid, lastUpdated, new AsyncListener<List<ImitationVideo>>() {
            @Override
            public void onComplete(List<ImitationVideo> data) {
                long lastUpdated = 0;

                for (ImitationVideo imitVideo : data) {
                    if (imitVideo.isDeleted()) {
                        modelSql.deleteImitVideo(imitVideo, null);
                    }
                    else {
                        modelSql.insertImitVideo(imitVideo, null);
                        if(imitVideo.getLastUpdated() > lastUpdated) {
                            lastUpdated = imitVideo.getLastUpdated();
                        }
                    }
                }

                LeagueTokApplication.context
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("userImitVideosLastUpdateDate", lastUpdated)
                        .apply();

                if (listener != null) {
                    listener.onComplete(null);
                }
            }

            @Override
            public void onError(List<ImitationVideo> error) {
                listener.onError(null);
            }
        });
    }
}
