package com.example.leaguetok.ui.upload;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.NodeService;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.home.HomeFragmentDirections;
import com.example.leaguetok.ui.upload.UploadVideoFragmentDirections;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadVideoFragment extends Fragment {
    OriginalVideo origVideo;

    TextView errorText;
    TextView uploadTitle;
    CircularProgressIndicator progressBar;
    ImageView uploadImg;
    View view;

    public UploadVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_video, container, false);
        errorText = view.findViewById(R.id.upload_error_label);
        uploadTitle = view.findViewById(R.id.upload_video_title);
        progressBar = view.findViewById(R.id.upload_progress_bar);
        uploadImg = view.findViewById(R.id.upload_video_img);

        Intent pickVideo = new Intent(Intent.ACTION_GET_CONTENT);
        pickVideo.setType("video/*");

        String origVideoId = UploadVideoFragmentArgs.fromBundle(getArguments()).getOriginalVideoID();
        Model.instance.getOrigVideoById(origVideoId).observe(getActivity(), new Observer<OriginalVideo>() {
            @Override
            public void onChanged(OriginalVideo originalVideo) {
                origVideo = originalVideo;
                uploadTitle.setText(getString(R.string.upload_title, originalVideo.getName(), originalVideo.getPerformer()));
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickVideo = new Intent(Intent.ACTION_GET_CONTENT);
                pickVideo.setType("video/*");
                startActivityForResult(Intent.createChooser(pickVideo,"Select Video"), 1);
                Intent.createChooser(pickVideo,"Select Video");
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == 1 && data != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String origVideoId = origVideo.getId();
            Uri selectedVideo = data.getData();
            Model.instance.uploadVideo(selectedVideo, uid, origVideoId, new Model.DataAsyncListener<String>() {
                @Override
                public void onComplete(String data) {
                    uploadImg.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);
                    Model.instance.uploadVideoToServer(data, uid, origVideoId, new NodeService.RequestListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Model.instance.refreshAllImitVideos(null);
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