package com.example.leaguetok.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.ui.profile.adapters.ProfileAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private List<ImitationVideo> videos;
    private ProfileViewModel profileViewModel;
    private String uid;
    private ImageView profileImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        setHasOptionsMenu(false);
        recyclerView = view.findViewById(R.id.profile_videos);
        profileImg = view.findViewById(R.id.profile_img);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/leaguetok.appspot.com/o/logo_pic.jpeg?alt=media&token=6da71041-0ff7-4e1a-83b6-0b83ade8ed57").into(profileImg);

        try {
            uid = ProfileFragmentArgs.fromBundle(getArguments()).getUid();
        }
        catch (Exception e) {
            uid = mAuth.getCurrentUser().getUid();
        }

        profileViewModel.getList(uid).observe(getViewLifecycleOwner(), new Observer<List<ImitationVideo>>() {
            @Override
            public void onChanged(List<ImitationVideo> imitationVideos) {
                videos = imitationVideos;
                buildRecyclerView();
                adapter.notifyDataSetChanged();
                adapter.setOnItemClickListener((position) -> {
                    ImitationVideo video = videos.get(position);
                    NavDirections direction = ProfileFragmentDirections.actionProfileFragmentToProfileVideoFragment(video);
                    Navigation.findNavController(view).navigate(direction);
                });

            }
        });

        return view;
    }

    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new ProfileAdapter(videos, getContext());

        // adding layout manager to our recycler view.
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        recyclerView.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recyclerView.setAdapter(adapter);
    }
}