package com.example.leaguetok.ui.league;

import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.model.User;

import java.util.List;

public class LeagueViewModel extends ViewModel {
    private LiveData<List<ImitationVideo>> imitList;
    private LiveData<OriginalVideo> originalVideo;
    private LiveData<List<User>> usersList;

    public LeagueViewModel() {
        imitList = Model.instance.getAllImitationVideos(new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {}

            @Override
            public void onError(Object error) {
                Toast.makeText(LeagueTokApplication.context, "Network connection error", Toast.LENGTH_SHORT).show();
            }
        });
        usersList = Model.instance.getAllUsers(new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {}

            @Override
            public void onError(Object error) {}
        });
    }

    public void filter(String sourceId) {
        imitList = Transformations.switchMap(imitList, list -> Model.instance.getAllImitVideosBySourceID(sourceId));
        originalVideo = Model.instance.getOrigVideoById(sourceId);
    }

    public LiveData<List<ImitationVideo>> getList() {
        return imitList;
    }

    public LiveData<OriginalVideo> getOriginalVideo() {
        return originalVideo;
    }

    public LiveData<List<User>> getUsersList() {
        return usersList;
    }
}