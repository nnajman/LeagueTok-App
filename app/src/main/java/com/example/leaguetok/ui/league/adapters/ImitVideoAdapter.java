package com.example.leaguetok.ui.league.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.ui.league.LeagueViewModel;
import com.example.leaguetok.ui.league.adapters.ImitVideoAdapter;
import com.example.leaguetok.ui.league.adapters.ImitVideoViewHolder;

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
                holder.txtImitPlace.setText(String.valueOf(position+1));
                holder.imgImitPlace.setVisibility(View.INVISIBLE);
                break;
        }
        holder.txtImitName.setText(imitationVideo.getUid());
        holder.txtImitGrade.setText(String.valueOf(imitationVideo.getScore()));
    }

    @Override
    public int getItemCount() {
        if(data.getList().getValue() == null) {
            return 0;
        }

        return data.getList().getValue().size();
    }
}
