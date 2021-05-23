package com.example.leaguetok.ui.profile;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.OriginalVideo;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    public ProfileViewModel() {
    }

    public LiveData<List<ImitationVideo>> getList(String uid) {
        return Model.instance.getImitiationVideosByUid(uid, new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {
            }

            @Override
            public void onError(Object error) {
                Toast.makeText(LeagueTokApplication.context, "Network connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}