package com.example.leaguetok.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.NodeService;
import com.example.leaguetok.model.OriginalVideo;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadVideoFragment extends Fragment {
    OriginalVideo origVideo;

    TextView gradeTitle;
    TextView gradeText;
    TextView errorText;
    TextView uploadTitle;
    CircularProgressIndicator progressBar;

    public UploadVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_video, container, false);

        Button uploadButton = view.findViewById(R.id.upload_video_btn);
        gradeTitle = view.findViewById(R.id.upload_grade_title);
        gradeText = view.findViewById(R.id.upload_grade_txt);
        errorText = view.findViewById(R.id.upload_error_label);

        uploadTitle = view.findViewById(R.id.upload_video_title);
        progressBar = view.findViewById(R.id.upload_progress_bar);

        String origVideoId = UploadVideoFragmentArgs.fromBundle(getArguments()).getOriginalVideoID();
        Model.instance.getOrigVideoById(origVideoId).observe(getActivity(), new Observer<OriginalVideo>() {
            @Override
            public void onChanged(OriginalVideo originalVideo) {
                origVideo = originalVideo;
                uploadTitle.setText(getString(R.string.upload_title, originalVideo.getName(), originalVideo.getPerformer()));
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickVideo = new Intent(Intent.ACTION_GET_CONTENT);
                pickVideo.setType("video/*");
                startActivityForResult(Intent.createChooser(pickVideo,"Select Video"), 1);
                gradeTitle.setVisibility(View.INVISIBLE);
                gradeText.setVisibility(View.INVISIBLE);
                errorText.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1 && data != null) {
            String uid = "123";
            String origName = origVideo.getName();
            String origVideoId = origVideo.getId();
            Uri selectedVideo = data.getData();
            Model.instance.uploadVideo(selectedVideo, uid, origName, new Model.DataAsyncListener<String>() {
                @Override
                public void onComplete(String data) {
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);
                    Model.instance.uploadVideoToServer(data, uid, origVideoId, new NodeService.RequestListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            progressBar.setVisibility(View.INVISIBLE);
                            try {
                                gradeTitle.setVisibility(View.VISIBLE);
                                gradeText.setVisibility(View.VISIBLE);
                                gradeText.setText(response.getString("result"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            errorText.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onProgress(int progress) {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgressCompat(progress, true);
                }
            });
        }
    }
}