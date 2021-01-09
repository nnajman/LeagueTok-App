package com.example.leaguetok.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ProgressBar uploadProgress;
    TextView progressText;
    EditText videoUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // For POC this code is for uploading video from galley.
        // After POC need to be transferred to upload video fragment
        Button uploadButton = root.findViewById(R.id.home_upload_video_btn);
        uploadProgress = root.findViewById(R.id.home_upload_progress);
        uploadProgress.setVisibility(View.INVISIBLE);
        progressText = root.findViewById(R.id.home_progress_text);
        progressText.setVisibility(View.INVISIBLE);
        videoUri = root.findViewById(R.id.home_uri_text);
        videoUri.setVisibility(View.INVISIBLE);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickVideo = new Intent(Intent.ACTION_GET_CONTENT);
                pickVideo.setType("video/*");
                startActivityForResult(Intent.createChooser(pickVideo,"Select Video"), 1);
            }
        });


        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 1 && data != null) {
            String uid = "123";
            String origName = "test";
            Uri selectedVideo = data.getData();
            Model.instance.uploadVideo(selectedVideo, uid, origName, new Model.DataAsyncListener<String>() {
                @Override
                public void onComplete(String data) {
                    videoUri.setVisibility(View.VISIBLE);
                    videoUri.setText(data);
                }

                @Override
                public void onProgress(int progress) {
                    uploadProgress.setVisibility(View.VISIBLE);
                    uploadProgress.setProgress(progress);
                    progressText.setVisibility(View.VISIBLE);
                    progressText.setText(progress + "%");
                }
            });
        }
    }
}