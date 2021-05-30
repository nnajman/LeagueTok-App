package com.example.leaguetok.model;

import android.graphics.Bitmap;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class StoreModel {

    public interface Listener {
        void onSuccess(String url);
        void onFail();
    }

    public static void uploadImage(Bitmap imageBmp, String name, final Listener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("profile_photos").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener((exception) -> {
            listener.onFail();
        }).addOnSuccessListener((taskSnapshot -> {
            imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                listener.onSuccess(uri.toString());
            });
        }));
    }
}
