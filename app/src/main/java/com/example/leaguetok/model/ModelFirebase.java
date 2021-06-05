package com.example.leaguetok.model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ModelFirebase {
    final String IMIT_VID = "videos/Imitations";
    final String ORIG_VID = "videos/Originals";

    // For POC this method is for uploading imitation videos only.
    // Need to generalize in order to upload original videos as well. (add type of video as param, check which type, etc.)
    public void uploadVideo(Uri videoUri, String uid, String origVideoId, Model.DataAsyncListener<String> listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference videosRef = storage.getReference().child(IMIT_VID).child(uid + "_" + origVideoId);

        // Upload video
        videosRef.putFile(videoUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                videosRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // get uri for original video
                        // add post request to node server with original and imitation videos uri
                        listener.onComplete(uri.toString());
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                listener.onProgress((int)((100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount()));
            }
        });
    }

    public void uploadOriginalVideo(Uri videoUri, String name, String perfomer, Model.DataAsyncListener<String> listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference videosRef = storage.getReference().child(ORIG_VID).child(name + "_" + perfomer);

        // Upload video
        videosRef.putFile(videoUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                videosRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // get uri for original video
                        // add post request to node server with original and imitation videos uri
                        listener.onComplete(uri.toString());
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                listener.onProgress((int)((100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount()));
            }
        });
    }
}
