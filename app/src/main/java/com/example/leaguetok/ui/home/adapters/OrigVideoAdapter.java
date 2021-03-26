package com.example.leaguetok.ui.home.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.R;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.home.HomeViewModel;


public class OrigVideoAdapter extends RecyclerView.Adapter<OrigVideoViewHolder> {
    public interface onLeagueClickListener{
        void onClick(int position);
    }

    public interface onTryClickListener{
        void onClick(int position);
    }

    HomeViewModel data;
    private onLeagueClickListener leagueListener;
    private onTryClickListener tryListener;

    public OrigVideoAdapter(HomeViewModel data) {
        this.data = data;
    }

    public void setOnLeagueClickListener(onLeagueClickListener listener) {
        this.leagueListener = listener;
    }

    public void setOnTryClickListener(onTryClickListener listener) {
        this.tryListener = listener;
    }

    @NonNull
    @Override
    public OrigVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        OrigVideoViewHolder holder = new OrigVideoViewHolder(view, this.leagueListener, this.tryListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrigVideoViewHolder holder, int position) {
        OriginalVideo originalVideo = data.getList().getValue().get(position);
        holder.txtVideoTitle.setText(originalVideo.getName());
        holder.setVideoPlayer(originalVideo.getUri());
    }

    @Override
    public int getItemCount() {
        if(data.getList().getValue() == null) {
            return 0;
        }

        return data.getList().getValue().size();
    }
}
