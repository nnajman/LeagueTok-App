package com.example.leaguetok.ui.home.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.R;

public class OrigVideoViewHolder extends RecyclerView.ViewHolder {
    VideoView originalVideo;
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

        originalVideo = itemView.findViewById(R.id.listrow_video);
        MediaController controller = new MediaController(originalVideo.getContext());
        controller.setMediaPlayer(originalVideo);
        originalVideo.setMediaController(controller);
        txtVideoTitle = itemView.findViewById(R.id.listrow_video_title);
        txtCountTries = itemView.findViewById(R.id.listrow_count_tries);
        btnTryIt = itemView.findViewById(R.id.listrow_try_btn);
        btnLeagueTable = itemView.findViewById(R.id.listrow_league_table_btn);
    }
}
