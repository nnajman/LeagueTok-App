package com.example.leaguetok.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.example.leaguetok.AuthenticationActivity;
import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.NodeService;
import com.example.leaguetok.ui.profile.adapters.ProfileAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private List<ImitationVideo> videos;
    private ProfileViewModel profileViewModel;
    private String uid;
    private ImageView profileImg;
    private TextView profileName;
    private MenuItem uploadVideoBtn;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.profile_videos);
        profileImg = view.findViewById(R.id.profile_img);
        profileName = view.findViewById(R.id.profile_name);

        try {
            uid = ProfileFragmentArgs.fromBundle(getArguments()).getUid();
        }
        catch (Exception e) {
            uid = mAuth.getCurrentUser().getUid();
        }

        Model.instance.getUserById(uid, new NodeService.RequestListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                profileName.setText(response.get("name").toString());
                Glide.with(getContext()).load(response.get("photoUrl").toString()).into(profileImg);
                if (response.get("isAdmin").toString() != "true") {
                    uploadVideoBtn.setVisible(false);
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);

        uploadVideoBtn = menu.getItem(0);
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
            case R.id.action_upload_video:
                NavDirections direction =
                        ProfileFragmentDirections.actionProfileFragmentToUploadOriginalVideoFragment((String) profileName.getText());
                Navigation.findNavController(view).navigate(direction);
        }

        return super.onOptionsItemSelected(item);
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