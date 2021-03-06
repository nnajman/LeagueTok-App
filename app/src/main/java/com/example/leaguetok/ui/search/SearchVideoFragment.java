package com.example.leaguetok.ui.search;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.home.HomeFragmentDirections;

public class SearchVideoFragment extends Fragment {

    VideoView videoView;
    TextView txtVideoTitle;
    TextView txtCountTries;
    ImageView loading;
    ImageView imgPlay;
    LinearLayout btnTryIt;
    LinearLayout btnLeagueTable;
    View root;

    public SearchVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.list_row, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search");
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        OriginalVideo originalVideo = SearchVideoFragmentArgs.fromBundle(getArguments()).getOriginalVideo();
        videoView = root.findViewById(R.id.listrow_video);
        txtVideoTitle = root.findViewById(R.id.listrow_video_title);
        txtCountTries = root.findViewById(R.id.listrow_count_tries);
        loading = root.findViewById(R.id.listrow_loading_spinner);
        Glide.with(root).load(R.drawable.spinner).fitCenter().override(100, 100).into(loading);
        imgPlay = root.findViewById(R.id.listrow_play_btn);
        btnTryIt = root.findViewById(R.id.listrow_try_btn);
        btnLeagueTable = root.findViewById(R.id.listrow_league_table_btn);

        Model.instance.getNumOfImitBySourceId(originalVideo.getId(), new Model.AsyncListener<Integer>() {
            @Override
            public void onComplete(Integer data) {
                ((TextView)root.findViewById(R.id.listrow_count_tries)).setText(LeagueTokApplication.context.getString(R.string.count_tries, data));
            }

            @Override
            public void onError(Integer error) {}
        });

        btnTryIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation
                        .findNavController(root)
                        .navigate(SearchVideoFragmentDirections.actionSearchVideoFragmentToUploadVideoFragment(originalVideo.getId()));
            }
        });

        btnLeagueTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root)
                        .navigate(SearchVideoFragmentDirections.actionSearchVideoFragmentToTableLeagueFragment(originalVideo.getId()));
            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
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

        txtVideoTitle.setText(originalVideo.getName());
        setVideoPlayer(originalVideo.getUri());
        return root;
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