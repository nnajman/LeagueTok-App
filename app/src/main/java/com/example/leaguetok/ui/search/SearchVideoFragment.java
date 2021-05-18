package com.example.leaguetok.ui.search;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.R;
import com.example.leaguetok.model.OriginalVideo;

public class SearchVideoFragment extends Fragment {

    VideoView videoView;
    TextView txtVideoTitle;
    TextView txtCountTries;
    ProgressBar loading;
    ImageView imgPlay;
    LinearLayout btnTryIt;
    LinearLayout btnLeagueTable;
    public SearchVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_row, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        setHasOptionsMenu(false);
        OriginalVideo originalVideo = SearchVideoFragmentArgs.fromBundle(getArguments()).getOriginalVideo();
        videoView = view.findViewById(R.id.listrow_video);
        txtVideoTitle = view.findViewById(R.id.listrow_video_title);
        txtCountTries = view.findViewById(R.id.listrow_count_tries);
        loading = view.findViewById(R.id.listrow_loading);
        imgPlay = view.findViewById(R.id.listrow_play_btn);
        btnTryIt = view.findViewById(R.id.listrow_try_btn);
        btnLeagueTable = view.findViewById(R.id.listrow_league_table_btn);

        txtVideoTitle.setText(originalVideo.getName());
        setVideoPlayer(originalVideo.getUri());
        return view;
    }

    public void setVideoPlayer(String videoUri) {
        videoView.setVideoPath(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                loading.setVisibility(View.GONE);
                imgPlay.setVisibility(View.INVISIBLE);
                mp.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                imgPlay.setVisibility(View.INVISIBLE);
                mp.start();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(LeagueTokApplication.context, "Can't play this video", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                return true;
            }
        });
    }
}