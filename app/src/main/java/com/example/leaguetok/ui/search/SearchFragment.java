package com.example.leaguetok.ui.search;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.leaguetok.R;
import com.example.leaguetok.model.OriginalVideo;
import com.example.leaguetok.ui.home.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    HomeViewModel mViewModel;
    SearchView searchView;
    ListView listView;
    String[] nameList = {"Tomer", "Sara", "Nadav", "Gal", "Ofek", "Arik", "Sigal", "Roee"};
    ArrayAdapter<String> arrayAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        searchView = view.findViewById(R.id.search_bar);
        listView = view.findViewById(R.id.search_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "You clicked - " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG);
            }
        });

        mViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<OriginalVideo>>() {
            @Override
            public void onChanged(List<OriginalVideo> originalVideos) {
                List<String> names = new ArrayList<String>();
                for (OriginalVideo video : originalVideos) {
                    names.add(video.getName());
                }

                arrayAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_list_item_1,
                        names);
                listView.setAdapter(arrayAdapter);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return view;
    }

}