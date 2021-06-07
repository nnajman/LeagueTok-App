package com.example.leaguetok.ui.profile;

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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.NodeService;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.upload.UploadVideoFragmentArgs;
import com.example.leaguetok.ui.upload.UploadVideoFragmentDirections;
import com.eyalbira.loadingdots.LoadingDots;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class UploadOriginalVideoFragment extends Fragment {
    CircularProgressBar progressBar;
    ImageView uploadImg;
    View view;
    TextInputEditText videoNameEt;
    TextInputEditText performerEt;
    TextView percentageText;
    LinearLayout uploadImgAndText;
    TextView statusText;
    LoadingDots dots;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload_original_video, container, false);
        progressBar = view.findViewById(R.id.upload_original_video_progress_bar);
        percentageText = view.findViewById(R.id.upload_original_video_percentage);
        uploadImgAndText = view.findViewById(R.id.upload_original_video_img_and_text);
        uploadImg = view.findViewById(R.id.upload_original_video_img);
        videoNameEt = view.findViewById(R.id.upload_original_name_et);
        performerEt = view.findViewById(R.id.upload_original_performer_et);
        statusText = view.findViewById(R.id.upload_original_video_status_text);
        dots = view.findViewById(R.id.upload_original_video_loadingDots);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Upload Challenge Video");
        setHasOptionsMenu(true);

        progressBar.setOnProgressChangeListener(new Function1<Float, Unit>() {
            @Override
            public Unit invoke(Float aFloat) {
                percentageText.setText(getString(R.string.upload_percentage, Math.round(aFloat)));
                return Unit.INSTANCE;
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoNameEt.getText().toString().length() == 0 || performerEt.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

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
            Uri selectedVideo = data.getData();
            String name = videoNameEt.getText().toString();
            String performer = performerEt.getText().toString();
            progressBar.setIndeterminateMode(false);
            progressBar.setVisibility(View.VISIBLE);

            Model.instance.uploadOriginalVideo(selectedVideo, name, performer, new Model.DataAsyncListener<String>() {
                @Override
                public void onComplete(String data) {
                    statusText.setText(R.string.upload_status_process);
                    progressBar.setIndeterminateMode(true);
                    percentageText.setVisibility(View.INVISIBLE);
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
                                statusText.setVisibility(View.INVISIBLE);
                                dots.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
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
                    progressBar.setProgressWithAnimation((float)progress, 1000l);
                }
            });
        }
    }
}