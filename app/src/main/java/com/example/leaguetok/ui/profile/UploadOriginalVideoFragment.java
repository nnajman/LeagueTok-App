package com.example.leaguetok.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.NodeService;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.upload.UploadVideoFragmentArgs;
import com.example.leaguetok.ui.upload.UploadVideoFragmentDirections;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class UploadOriginalVideoFragment extends Fragment {
    TextView errorText;
    CircularProgressIndicator progressBar;
    ImageView uploadImg;
    View view;
    TextInputEditText videoNameEt;
    String performer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_original_video, container, false);
        errorText = view.findViewById(R.id.upload_original_error_label);
        progressBar = view.findViewById(R.id.upload_original_progress_bar);
        uploadImg = view.findViewById(R.id.upload_original_video_img);
        videoNameEt = view.findViewById(R.id.upload_original_name_et);
        performer = UploadOriginalVideoFragmentArgs.fromBundle(getArguments()).getPerformer();

        Intent pickVideo = new Intent(Intent.ACTION_GET_CONTENT);
        pickVideo.setType("video/*");

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
            Uri selectedVideo = data.getData();
            String name = videoNameEt.getText().toString();

            Model.instance.uploadOriginalVideo(selectedVideo, name, performer, new Model.DataAsyncListener<String>() {
                @Override
                public void onComplete(String data) {
                    progressBar.setVisibility(View.INVISIBLE);
                    progressBar.setIndeterminate(true);
                    progressBar.setVisibility(View.VISIBLE);
                    Model.instance.uploadOriginalVideoToServer(data, name, performer, new NodeService.RequestListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            if (isResumed()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                uploadImg.setVisibility(View.VISIBLE);
                                Navigation.findNavController(view).popBackStack();
                                Toast.makeText(getContext(), "The video was successfully uploaded", Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            if (isResumed()) {
                                progressBar.setVisibility(View.INVISIBLE);
                                errorText.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }

                @Override
                public void onProgress(int progress) {
                    uploadImg.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgressCompat(progress, true);
                }
            });
        }
    }
}