package com.example.leaguetok.ui.home;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.OriginalVideo;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private LiveData<List<OriginalVideo>> origList;

    public HomeViewModel() {
        origList = Model.instance.getAllOriginalVideos(new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {
            }

            @Override
            public void onError(Object error) {
                Toast.makeText(LeagueTokApplication.context, "Network connection error", Toast.LENGTH_SHORT).show();
            }
        });

        Model.instance.getAllImitationVideos(new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {}

            @Override
            public void onError(Object error) {}
        });
        Model.instance.getAllUsers(new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {}

            @Override
            public void onError(Object error) {}
        });
    }

    public LiveData<List<OriginalVideo>> getList() { return origList; }
}