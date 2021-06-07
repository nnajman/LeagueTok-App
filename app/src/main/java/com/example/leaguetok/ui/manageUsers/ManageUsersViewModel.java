package com.example.leaguetok.ui.manageUsers;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.User;

import java.util.List;

public class ManageUsersViewModel extends ViewModel {
    private LiveData<List<User>> usersList;

    public ManageUsersViewModel() {
        this.usersList = Model.instance.getAllUsers(new Model.AsyncListener() {
            @Override
            public void onComplete(Object data) {

            }

            @Override
            public void onError(Object error) {
                Toast.makeText(LeagueTokApplication.context, "Network connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public LiveData<List<User>> getList() { return usersList; }
}