package com.example.leaguetok.ui.league.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.model.User;
import com.example.leaguetok.ui.league.LeagueViewModel;
import com.example.leaguetok.ui.league.adapters.ImitVideoAdapter;
import com.example.leaguetok.ui.league.adapters.ImitVideoViewHolder;
import com.google.firebase.auth.FirebaseAuth;

public class ImitVideoAdapter extends RecyclerView.Adapter<ImitVideoViewHolder> {

    LeagueViewModel data;

    public ImitVideoAdapter(LeagueViewModel data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ImitVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.league_list_row, parent, false);
        ImitVideoViewHolder holder = new ImitVideoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImitVideoViewHolder holder, int position) {
        ImitationVideo imitationVideo = data.getList().getValue().get(position);
        boolean isCurrUser = (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(imitationVideo.getUid()));
        int defaultColor = holder.txtImitPlace.getTextColors().getDefaultColor();
        int currUserColor = holder.txtImitName.getResources().getColor(R.color.pink);

        switch(position) {
            case 0:
                holder.imgImitPlace.setImageResource(R.drawable.ic_outline_looks_one_24);
                holder.imgImitPlace.setVisibility(View.VISIBLE);
                holder.txtImitPlace.setVisibility(View.INVISIBLE);
                break;
            case 1:
                holder.imgImitPlace.setImageResource(R.drawable.ic_outline_looks_two_24);
                holder.imgImitPlace.setVisibility(View.VISIBLE);
                holder.txtImitPlace.setVisibility(View.INVISIBLE);
                break;
            case 2:
                holder.imgImitPlace.setImageResource(R.drawable.ic_outline_looks_3_24);
                holder.imgImitPlace.setVisibility(View.VISIBLE);
                holder.txtImitPlace.setVisibility(View.INVISIBLE);
                break;
            default:
                holder.txtImitPlace.setTextColor((isCurrUser) ? currUserColor : defaultColor);
                holder.txtImitPlace.setTypeface(null, (isCurrUser) ? Typeface.BOLD : Typeface.NORMAL);
                holder.txtImitPlace.setText(String.valueOf(position+1));
                holder.txtImitPlace.setVisibility(View.VISIBLE);
                holder.imgImitPlace.setVisibility(View.INVISIBLE);
                break;
        }

        holder.txtImitName.setTextColor((isCurrUser) ? currUserColor : defaultColor);
        holder.txtImitName.setTypeface(null, (isCurrUser) ? Typeface.BOLD : Typeface.NORMAL);
        holder.txtImitName.setText(getUser(imitationVideo.getUid()).getName());

        holder.txtImitGrade.setTextColor((isCurrUser) ? currUserColor : defaultColor);
        holder.txtImitGrade.setTypeface(null, (isCurrUser) ? Typeface.BOLD : Typeface.NORMAL);
        holder.txtImitGrade.setText(String.valueOf(imitationVideo.getScore()));
    }

    @Override
    public int getItemCount() {
        if(data.getList().getValue() == null) {
            return 0;
        }

        return data.getList().getValue().size();
    }

    private User getUser(String id) {
        User searchUser = null;
        for (User user : data.getUsersList().getValue()) {
            if (user.getId().equals(id)) {
                searchUser = user;
                break;
            }
        }

        return searchUser;
    }
}
