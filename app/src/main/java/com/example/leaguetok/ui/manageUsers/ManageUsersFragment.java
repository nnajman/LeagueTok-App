package com.example.leaguetok.ui.manageUsers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.User;
import com.example.leaguetok.ui.manageUsers.adapters.UserAdapter;

import java.util.List;

public class ManageUsersFragment extends Fragment {

    private ManageUsersViewModel mViewModel;
    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ManageUsersViewModel.class);
        view =  inflater.inflate(R.layout.manage_users_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Manage Users");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);

        RecyclerView list = view.findViewById(R.id.manage_users_list);
        list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager((layoutManager));

        UserAdapter adapter = new UserAdapter(mViewModel);
        list.setAdapter(adapter);

        SwipeRefreshLayout swiper = view.findViewById(R.id.manage_users_swipe_to_refresh);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swiper.setRefreshing(true);
                Model.instance.refreshAllUsers(new Model.AsyncListener() {
                    @Override
                    public void onComplete(Object data) {
                        swiper.setRefreshing(false);
                    }

                    @Override
                    public void onError(Object error) {
                        Toast.makeText(getActivity(), "Network connection error", Toast.LENGTH_SHORT).show();
                        swiper.setRefreshing(false);
                    }
                });
            }
        });

        mViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.notifyDataSetChanged();
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
}