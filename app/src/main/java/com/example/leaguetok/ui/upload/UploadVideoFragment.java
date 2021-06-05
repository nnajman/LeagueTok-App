package com.example.leaguetok.ui.upload;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.NodeService;
import com.example.leaguetok.model.OriginalVideo;
import com.eyalbira.loadingdots.LoadingDots;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadVideoFragment extends Fragment {
    OriginalVideo origVideo;

    TextView uploadTitle;
    TextView waitText;
    ImageView uploadImg;
    TextView statusText;
    LoadingDots dots;
    View view;
    CircularProgressBar progressBar;
    LinearLayout uploadImgAndText;
    TextView percentageText;
    ImageView loading;

    public UploadVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_video, container, false);
        uploadTitle = view.findViewById(R.id.upload_video_title);
        uploadImgAndText = view.findViewById(R.id.upload_img_and_text);
        uploadImg = view.findViewById(R.id.upload_video_img);
        waitText = view.findViewById(R.id.upload_wait_text);
        statusText = view.findViewById(R.id.upload_status_text);
        dots = view.findViewById(R.id.upload_loadingDots);
        progressBar = view.findViewById(R.id.upload_progress_bar);
        percentageText = view.findViewById(R.id.upload_percentage);
        loading = view.findViewById(R.id.upload_loading);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Upload Video");
        setHasOptionsMenu(true);

        Intent pickVideo = new Intent(Intent.ACTION_GET_CONTENT);
        pickVideo.setType("video/*");

        String origVideoId = UploadVideoFragmentArgs.fromBundle(getArguments()).getOriginalVideoID();
        Model.instance.getOrigVideoById(origVideoId, new Model.AsyncListener<OriginalVideo>() {
            @Override
            public void onComplete(OriginalVideo data) {
                origVideo = data;
                uploadTitle.setText(getString(R.string.upload_title, origVideo.getName(), origVideo.getPerformer()));
            }

            @Override
            public void onError(OriginalVideo error) {

            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickVideo = new Intent(Intent.ACTION_PICK);
                pickVideo.setType("video/*");
                startActivityForResult(Intent.createChooser(pickVideo,"Select Video"), 1);
                Intent.createChooser(pickVideo,"Select Video");
            }
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Navigation.findNavController(view).popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1 && data != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String origVideoId = origVideo.getId();
            Uri selectedVideo = data.getData();
            progressBar.setVisibility(View.VISIBLE);
            Model.instance.uploadVideo(selectedVideo, uid, origVideoId, new Model.DataAsyncListener<String>() {
                @Override
                public void onComplete(String data) {
                    statusText.setText(R.string.upload_status_process);
                    waitText.setVisibility(View.VISIBLE);
                    waitText.setText(R.string.upload_status_wait);
                    progressBar.setVisibility(View.INVISIBLE);
                    percentageText.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.VISIBLE);
                    Glide.with(view).load(R.drawable.spinner).fitCenter().into(loading);
                    Model.instance.uploadVideoToServer(data, uid, origVideoId, new NodeService.RequestListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            if (isResumed()) {
                                Model.instance.refreshAllImitVideos(null);
                                Model.instance.refreshAllUsers(null);
                                progressBar.setVisibility(View.INVISIBLE);
                                uploadImg.setVisibility(View.VISIBLE);
                                try {
                                    Navigation
                                            .findNavController(view)
                                            .navigate(UploadVideoFragmentDirections.actionUploadVideoFragmentToUploadResultFragment2(response.getString("result"), origVideoId));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            if (isResumed()) {
                                statusText.setVisibility(View.INVISIBLE);
                                dots.setVisibility(View.INVISIBLE);
                                waitText.setVisibility(View.INVISIBLE);
                                loading.setVisibility(View.INVISIBLE);
                                uploadImgAndText.setVisibility(View.VISIBLE);
                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Oops...")
                                        .setMessage(getString(R.string.error_message))
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        }
                    });
                }

                @Override
                public void onProgress(int progress) {
                    statusText.setText(R.string.upload_status_video);
                    statusText.setVisibility(View.VISIBLE);
                    dots.setVisibility(View.VISIBLE);
                    uploadImgAndText.setVisibility(View.INVISIBLE);
                    percentageText.setVisibility(View.VISIBLE);
                    percentageText.setText(getString(R.string.upload_percentage, progress));
                    progressBar.setProgressWithAnimation((float)progress, 1000l);

                }
            });
        }
    }
}