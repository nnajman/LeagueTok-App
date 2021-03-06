package com.example.leaguetok.ui.upload;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.model.User;

import java.util.List;

public class UplaodViewModel extends ViewModel {
    private LiveData<List<ImitationVideo>> imitList;

    public UplaodViewModel() {
        imitList = Model.instance.getAllImitationVideos(null);
    }

    public void filter(String sourceId) {
        imitList = Transformations.switchMap(imitList, list -> Model.instance.getAllImitVideosBySourceID(sourceId));
    }

    public LiveData<List<ImitationVideo>> getList() {
        return imitList;
    }

}