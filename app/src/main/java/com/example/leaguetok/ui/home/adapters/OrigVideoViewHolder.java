package com.example.leaguetok.ui.home.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class OrigVideoViewHolder extends RecyclerView.ViewHolder {
    SimpleExoPlayer exoPlayer;
    StyledPlayerView playerView;
    TextView txtVideoTitle;
    TextView txtCountTries;
    Button btnTryIt;
    Button btnLeagueTable;

    public OrigVideoViewHolder(@NonNull View itemView, final OrigVideoAdapter.onItemClicklistener listener) {
        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            }
        });

        playerView = itemView.findViewById(R.id.listrow_video);
        txtVideoTitle = itemView.findViewById(R.id.listrow_video_title);
        txtCountTries = itemView.findViewById(R.id.listrow_count_tries);
        //btnTryIt = itemView.findViewById(R.id.listrow_try_btn);
        //btnLeagueTable = itemView.findViewById(R.id.listrow_league_table_btn);
    }

    public void setVideoPlayer(Uri videoUri) {
        exoPlayer = new SimpleExoPlayer.Builder(itemView.getContext()).build();
        playerView.setPlayer(exoPlayer);
        Log.d("TAG", String.valueOf(videoUri));
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }
}
