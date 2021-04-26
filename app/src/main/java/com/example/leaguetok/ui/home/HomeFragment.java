package com.example.leaguetok.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.home.adapters.OrigVideoAdapter;
import com.example.leaguetok.ui.league.TableLeagueFragment;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Hide action bar
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        //Button btnUpload = root.findViewById(R.id.home_upload_btn);
        //btnUpload.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_navigation_home_to_uploadVideoFragment));

        ViewPager2 list = root.findViewById(R.id.viewPagerVideos);
        SwipeRefreshLayout swiper = root.findViewById(R.id.home_swipe_to_refresh);

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiper.setRefreshing(true);
                Model.instance.refreshAllOrigVideos(new Model.AsyncListener() {
                    @Override
                    public void onComplete(Object data) {
                        swiper.setRefreshing(false);
                    }
                });
            }
        });

        OrigVideoAdapter adapter = new OrigVideoAdapter(homeViewModel);
        list.setAdapter(adapter);

        // League Table listener
        adapter.setOnLeagueClickListener(new OrigVideoAdapter.onLeagueClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG", homeViewModel.getList().getValue().get(position).getName());
                Navigation.findNavController(root)
                .navigate(HomeFragmentDirections.actionNavigationHomeToTableLeagueFragment(homeViewModel.getList().getValue().get(position).getId()));
            }
        });

        // Try It listener
        adapter.setOnTryClickListener(new OrigVideoAdapter.onTryClickListener() {
            @Override
            public void onClick(int position) {
                Navigation
                .findNavController(root)
                .navigate(HomeFragmentDirections.actionNavigationHomeToUploadVideoFragment(homeViewModel.getList().getValue().get(position).getId()));
            }
        });

        homeViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<OriginalVideo>>() {
            @Override
            public void onChanged(List<OriginalVideo> originalVideos) {
                adapter.notifyDataSetChanged();
            }
        });

        return root;
    }
}