package com.example.leaguetok.model;

import android.graphics.Bitmap;
import android.net.Uri;

public class Model {
    public interface DataAsyncListener<T> {
        void onComplete(T data);
        void onProgress(int progress);
    }
    public final static Model instance = new Model();
    ModelFirebase modelFirebase = new ModelFirebase();

    public void uploadVideo(Uri videoUri, String uid, String origName,  DataAsyncListener<String> listener) {
        modelFirebase.uploadVideo(videoUri, uid, origName, listener);
    }
}
