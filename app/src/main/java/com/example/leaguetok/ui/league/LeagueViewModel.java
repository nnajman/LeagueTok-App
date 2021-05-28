package com.example.leaguetok.ui.league;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.ImitationVideo;

import java.util.List;

public class LeagueViewModel extends ViewModel {
    private LiveData<List<ImitationVideo>> stList;
//    String origVideoId = TableLeagueFragmentArgs.fromBundle(getArguments()).getOriginalVideoID();

    public LeagueViewModel() {

        stList = Model.instance.getAllImitationVideos(new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {
            }

            @Override
            public void onError(Object error) {
                Toast.makeText(LeagueTokApplication.context, "Network connection error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public LiveData<List<ImitationVideo>> getList() { return stList; }
    public LiveData<List<ImitationVideo>> getListByID(String currID) {
        stList = Model.instance.getAllImitVideosBySourceID(currID, new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {
            }

            @Override
            public void onError(Object error) {
                Toast.makeText(LeagueTokApplication.context, "Network connection error", Toast.LENGTH_SHORT).show();
            }
        });
        return stList;
    }
}