package com.example.leaguetok.ui.upload;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.model.User;
import com.example.leaguetok.ui.league.LeagueViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class UploadResultFragment extends Fragment {

    TextView gradeTitle;
    TextView gradeText;
    TextView rankText;
    TextView lowerScoreText;
    ImageView rankImage;
    Button leagueButton;
    String imitVideoResult;

    public UploadResultFragment() {
        // Required empty public constructor
    }

    private UplaodViewModel uploadViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_result, container, false);
        imitVideoResult = UploadResultFragmentArgs.fromBundle(getArguments()).getImitVideoResult();
        String originalVideoID = UploadResultFragmentArgs.fromBundle(getArguments()).getOriginalVideoID();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uploadViewModel = new ViewModelProvider(this).get(UplaodViewModel.class);

        gradeTitle = view.findViewById(R.id.upload_grade_title);
        gradeText = view.findViewById(R.id.upload_grade_txt);
        leagueButton = view.findViewById(R.id.upload_league_button);
        rankText = view.findViewById(R.id.upload_rank_txt);
        rankImage = view.findViewById(R.id.upload_rank_img);
        lowerScoreText = view.findViewById(R.id.upload_lower_score_txt);
        lowerScoreText.setVisibility(View.INVISIBLE);
        final KonfettiView konfettiView = view.findViewById(R.id.viewKonfetti);
        gradeText.setText(String.valueOf(Math.round(Float.parseFloat(imitVideoResult))));

        uploadViewModel.filter(originalVideoID);
        uploadViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<ImitationVideo>>() {
            @Override
            public void onChanged(List<ImitationVideo> imitationVideos) {
                Integer rank = 0;
                Boolean isNewScoreBetter = true;
                for (ImitationVideo imitVideo : imitationVideos) {
                    rank++;
                    if (imitVideo.getUid().equals(uid)) {
                        if (Integer.parseInt(imitVideoResult) < imitVideo.getScore()) {
                            isNewScoreBetter = false;
                        }
                        break;
                    }
                }
                if (isNewScoreBetter) {
                    //              Set first places drawables
                    switch(rank) {
                        case 1:
                            rankImage.setImageResource(R.drawable.ic_outline_looks_one_24);
                            rankImage.setVisibility(View.VISIBLE);
                            rankText.setVisibility(View.INVISIBLE);
                            break;
                        case 2:
                            rankImage.setImageResource(R.drawable.ic_outline_looks_two_24);
                            rankImage.setVisibility(View.VISIBLE);
                            rankText.setVisibility(View.INVISIBLE);
                            break;
                        case 3:
                            rankImage.setImageResource(R.drawable.ic_outline_looks_3_24);
                            rankImage.setVisibility(View.VISIBLE);
                            rankText.setVisibility(View.INVISIBLE);
                            break;
                        default:
                            rankText.setText(String.valueOf(rank));
                            rankText.setVisibility(View.VISIBLE);
                            rankImage.setVisibility(View.INVISIBLE);
                            break;
                    }

                    if(Float.parseFloat(imitVideoResult) >= 50) {
                        konfettiView.build()
//                  In Int type by this order: Black, Blue, Red
                                .addColors(65793, 2487534, 16657493)
                                .setDirection(0.0, 359.0)
                                .setSpeed(1f, 5f)
                                .setFadeOutEnabled(true)
                                .setTimeToLive(500L)
                                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                                .addSizes(new Size(12, 5f))
                                .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                                .streamFor(300, 5000L);
                    }
                } else {
                    rankText.setVisibility(View.INVISIBLE);
                    lowerScoreText.setVisibility(View.VISIBLE);
                }
            }
        });




        leagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Navigation
                .findNavController(view)
                .navigate(UploadResultFragmentDirections.actionUploadResultFragment2ToTableLeagueFragment(originalVideoID));
            }
        });

        return view;
    }
}