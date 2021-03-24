package com.example.leaguetok.ui.home.adapters;

import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.R;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.home.HomeViewModel;

import java.util.Comparator;
import java.util.List;

public class OrigVideoAdapter extends RecyclerView.Adapter<OrigVideoViewHolder> {
    public interface onItemClicklistener{
        void onClick(int position);
    }

    HomeViewModel data;
    private onItemClicklistener listener;

    public OrigVideoAdapter(HomeViewModel data) {
        this.data = data;
    }
    public void setOnItemClicklistener(onItemClicklistener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrigVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        OrigVideoViewHolder holder = new OrigVideoViewHolder(view, this.listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrigVideoViewHolder holder, int position) {
        OriginalVideo originalVideo = data.getList().getValue().get(position);
        holder.txtVideoTitle.setText(originalVideo.getName());
        holder.setVideoPlayer(Uri.parse(originalVideo.getUri()));
    }

    @Override
    public int getItemCount() {
        if(data.getList().getValue() == null) {
            return 0;
        }

        return data.getList().getValue().size();
    }
}
