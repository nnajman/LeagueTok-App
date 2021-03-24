package com.example.leaguetok.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.OriginalVideo;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private LiveData<List<OriginalVideo>> stList;

    public HomeViewModel() {
        stList = Model.instance.getAllOriginalVideos(null);
    }

    public LiveData<List<OriginalVideo>> getList() { return stList; }
}