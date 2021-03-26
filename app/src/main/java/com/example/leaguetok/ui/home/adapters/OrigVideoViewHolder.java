package com.example.leaguetok.ui.home.adapters;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.R;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class OrigVideoViewHolder extends RecyclerView.ViewHolder {
    VideoView videoView;
    TextView txtVideoTitle;
    TextView txtCountTries;
    ProgressBar loading;
    ImageView imgPlay;
    Button btnTryIt;
    Button btnLeagueTable;
    boolean bIsPlaying = true;

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

        videoView = itemView.findViewById(R.id.listrow_video);
        txtVideoTitle = itemView.findViewById(R.id.listrow_video_title);
        txtCountTries = itemView.findViewById(R.id.listrow_count_tries);
        loading = itemView.findViewById(R.id.listrow_loading);
        imgPlay = itemView.findViewById(R.id.listrow_play_btn);
        //btnTryIt = itemView.findViewById(R.id.listrow_try_btn);
        //btnLeagueTable = itemView.findViewById(R.id.listrow_league_table_btn);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("TAG", "pressed");
                if (videoView.isPlaying()) {
                    videoView.pause();
                    imgPlay.setVisibility(View.VISIBLE);
                } else {
                    videoView.start();
                    imgPlay.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    public void setVideoPlayer(String videoUri) {
        videoView.setVideoPath(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                loading.setVisibility(View.GONE);
                mp.start();

                float videoRatio = mp.getVideoWidth() / (float)mp.getVideoHeight();
                float screenRatio = videoView.getWidth() / (float)videoView.getHeight();
                float scale  = videoRatio / screenRatio;
                if (scale >= 1f){
                    videoView.setScaleX(scale);
                }else {
                    videoView.setScaleY(1f / scale);
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
    }
}
