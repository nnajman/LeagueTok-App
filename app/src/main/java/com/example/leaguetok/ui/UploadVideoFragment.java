package com.example.leaguetok.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.ui.home.HomeViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UploadVideoFragment extends Fragment {
    ProgressBar uploadProgress;
    TextView progressText;
    TextView gradeTitle;
    TextView gradeText;
    TextView errorText;

    private RequestQueue queue;
    final String postVideoURL = "http://192.168.1.107:8080/video"; // Server URL

    public UploadVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_video, container, false);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // For POC this code is for uploading video from galley.
        // After POC need to be transferred to upload video fragment
        Button uploadButton = view.findViewById(R.id.upload_video_btn);
        uploadProgress = view.findViewById(R.id.upload_progress);
        uploadProgress.setVisibility(View.INVISIBLE);
        progressText = view.findViewById(R.id.upload_progress_text);
        progressText.setVisibility(View.INVISIBLE);
        gradeTitle = view.findViewById(R.id.upload_grade_title);
        gradeText = view.findViewById(R.id.upload_grade_txt);
        errorText= view.findViewById(R.id.upload_error_label);

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
            String origName = "test";
            String soruceId = "1";
            Uri selectedVideo = data.getData();
            Model.instance.uploadVideo(selectedVideo, uid, origName, new Model.DataAsyncListener<String>() {
                @Override
                public void onComplete(String data) {
                    HashMap<String, String> params = new HashMap<String,String>();
                    params.put("link", data);
                    params.put("uid", uid);
                    params.put("sourceId", soruceId);

                    JsonObjectRequest jsObjRequest = new
                            JsonObjectRequest(Request.Method.POST,
                            postVideoURL,
                            new JSONObject(params),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        gradeTitle.setVisibility(View.VISIBLE);
                                        gradeText.setVisibility(View.VISIBLE);
                                        gradeText.setText(response.getString("result"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            errorText.setVisibility(View.VISIBLE);
                        }
                    });

                    jsObjRequest.setShouldCache(false);
                    jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    queue.add(jsObjRequest);
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