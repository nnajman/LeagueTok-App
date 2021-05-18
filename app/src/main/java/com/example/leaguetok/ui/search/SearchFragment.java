package com.example.leaguetok.ui.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.leaguetok.R;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.home.HomeViewModel;
import com.example.leaguetok.ui.search.adapters.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private RecyclerView courseRV;
    private SearchAdapter adapter;
    private List<OriginalVideo> videos;
    private List<OriginalVideo> filteredVideos;
    private MenuItem searchItem;
    public SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);
        courseRV = view.findViewById(R.id.idRVCourses);

        homeViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<OriginalVideo>>() {
            @Override
            public void onChanged(List<OriginalVideo> originalVideos) {
                videos = originalVideos;
                buildRecyclerView();
                adapter.notifyDataSetChanged();
                adapter.setOnItemClickListener((position) -> {
                    OriginalVideo video = filteredVideos.get(position);
                    SearchAdapter.currentSearch = searchView.getQuery().toString();
                    NavDirections direction = SearchFragmentDirections.actionSearchFragmentToSearchVideoFragment(video);
                    Navigation.findNavController(view).navigate(direction);
                });

            }
        });

        return view;
    }

    // calling on create option menu
    // layout to inflate our menu file.
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu);

        // below line is to get our menu item.
        searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        searchView = (SearchView) searchItem.getActionView();
//        searchView.setQuery((String) SearchAdapter.currentSearch, true);
        searchView.post(new Runnable() {

            @Override
            public void run() {
                searchView.setQuery((String) SearchAdapter.currentSearch, true);
            }
        });

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        List<OriginalVideo> filteredlist = new ArrayList<OriginalVideo>();

        // running a for loop to compare elements.
        for (OriginalVideo video : videos) {
            // checking if the entered string matched with any item of our recycler view.
            if (video.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(video);
            }
        }
        filteredVideos = filteredlist;
        if (filteredlist.isEmpty() && text.length() == 0) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Log.d("TAG", "Empty");
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }

    private void buildRecyclerView() {

        // initializing our adapter class.
        adapter = new SearchAdapter(videos, getContext());

        // adding layout manager to our recycler view.
//        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        courseRV.setHasFixedSize(true);

        // setting layout manager
        // to our recycler view.
        courseRV.setLayoutManager(manager);

        // setting adapter to
        // our recycler view.
        courseRV.setAdapter(adapter);
    }

}