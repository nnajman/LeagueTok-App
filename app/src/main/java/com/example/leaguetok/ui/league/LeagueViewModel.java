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

import java.util.List;

public class LeagueViewModel extends ViewModel {
    private LiveData<List<ImitationVideo>> stList;

    public LeagueViewModel() {
        stList = Model.instance.getAllImitationVideos(new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {}

            @Override
            public void onError(Object error) {
                Toast.makeText(LeagueTokApplication.context, "Network connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void filter(String sourceId) {
        stList = Transformations.switchMap(stList, list -> Model.instance.getAllImitVideosBySourceID(sourceId, null));
    }

    public LiveData<List<ImitationVideo>> getList() {
        return stList;
    }
}