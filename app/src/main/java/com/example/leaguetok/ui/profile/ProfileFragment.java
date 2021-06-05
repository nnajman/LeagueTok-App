package com.example.leaguetok.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.leaguetok.AuthenticationActivity;
import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.ui.profile.adapters.ProfileAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private List<ImitationVideo> videos;
    private ProfileViewModel profileViewModel;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_profile);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.profile_videos);

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

                // add user at top of the list for profile details
                videos.add(0, new ImitationVideo());

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();
                GoogleSignIn.getClient(getContext(), gso).signOut();
                Intent i = new Intent(getActivity(), AuthenticationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new ProfileAdapter(videos, getContext());

        // adding layout manager to our recycler view.
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);

        // Create a custom SpanSizeLookup where the first item spans both columns
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });

        // setting layout manager
        // to our recycler view.
        recyclerView.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        recyclerView.setAdapter(adapter);
    }
}