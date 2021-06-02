package com.example.leaguetok.ui.profile;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;

public class ProfileVideoFragment extends Fragment {

    VideoView videoView;
    TextView txtVideoTitle;
    TextView txtCountTries;
    ProgressBar loading;
    ImageView imgPlay;

    public ProfileVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.list_row, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);
        ImitationVideo imitationVideo = ProfileVideoFragmentArgs.fromBundle(getArguments()).getImitationVideo();
        videoView = view.findViewById(R.id.listrow_video);
        txtVideoTitle = view.findViewById(R.id.listrow_video_title);
        txtCountTries = view.findViewById(R.id.listrow_count_tries);
        loading = view.findViewById(R.id.listrow_loading);
        imgPlay = view.findViewById(R.id.listrow_play_btn);
        view.findViewById(R.id.listrow_try_btn).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.listrow_league_table_btn).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.listrow_count_tries).setVisibility(View.INVISIBLE);

//        txtVideoTitle.setText(imitationVideo.get());
        setVideoPlayer(imitationVideo.getUrl());
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(videoView).popBackStack();
        }
        return super.onOptionsItemSelected(item);
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