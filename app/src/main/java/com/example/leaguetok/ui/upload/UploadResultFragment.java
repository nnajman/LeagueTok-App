package com.example.leaguetok.ui.upload;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.leaguetok.R;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class UploadResultFragment extends Fragment {

    TextView gradeTitle;
    TextView gradeText;
    Button leagueButton;

    public UploadResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_result, container, false);
        String imitVideoResult = UploadResultFragmentArgs.fromBundle(getArguments()).getImitVideoResult();
        String originalVideoID = UploadResultFragmentArgs.fromBundle(getArguments()).getOriginalVideoID();
        gradeTitle = view.findViewById(R.id.upload_grade_title);
        gradeText = view.findViewById(R.id.upload_grade_txt);
        leagueButton = view.findViewById(R.id.upload_league_button);
        final KonfettiView konfettiView = view.findViewById(R.id.viewKonfetti);

        gradeText.setText(String.valueOf(Math.round(Float.parseFloat(imitVideoResult))));
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