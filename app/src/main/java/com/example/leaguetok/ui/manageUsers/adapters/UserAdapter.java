package com.example.leaguetok.ui.manageUsers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.R;
import com.example.leaguetok.ui.manageUsers.ManageUsersViewModel;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    ManageUsersViewModel data;

    public UserAdapter(ManageUsersViewModel data) {
        this.data = data;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_row, parent, false);
        UserViewHolder holder = new UserViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(data.getList().getValue().get(position));
    }

    @Override
    public int getItemCount() {
        if(data.getList().getValue() == null) {
            return 0;
        }

        return data.getList().getValue().size();
    }
}
