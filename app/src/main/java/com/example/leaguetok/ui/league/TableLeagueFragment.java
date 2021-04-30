package com.example.leaguetok.ui.league;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.UploadVideoFragmentArgs;
import com.example.leaguetok.ui.home.HomeFragmentDirections;
import com.example.leaguetok.ui.home.HomeViewModel;
import com.example.leaguetok.ui.home.adapters.OrigVideoAdapter;
import com.example.leaguetok.ui.league.adapters.ImitVideoAdapter;

import java.util.List;

public class TableLeagueFragment extends Fragment {

    public TableLeagueFragment() {
        // Required empty public constructor
    }

    private LeagueViewModel leagueViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        leagueViewModel = new ViewModelProvider(this).get(LeagueViewModel.class);
        View root = inflater.inflate(R.layout.fragment_table_league, container, false);
        String origVideoId = UploadVideoFragmentArgs.fromBundle(getArguments()).getOriginalVideoID();
        RecyclerView list = root.findViewById(R.id.imitVideosList);


        list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager((layoutManager));

//        SwipeRefreshLayout swiper = root.findViewById(R.id.home_swipe_to_refresh);

//        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swiper.setRefreshing(true);
//                Model.instance.refreshAllOrigVideos(new Model.AsyncListener() {
//                    @Override
//                    public void onComplete(Object data) {
//                        swiper.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onError(Object error) {
//                        Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_SHORT).show();
//                        swiper.setRefreshing(false);
//                    }
//                });
//            }
//        });

        ImitVideoAdapter adapter = new ImitVideoAdapter(leagueViewModel);
        list.setAdapter(adapter);

        leagueViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<ImitationVideo>>() {
            @Override
            public void onChanged(List<ImitationVideo> imitationVideos) {
                adapter.notifyDataSetChanged();
            }
        });

        return root;
    }
}