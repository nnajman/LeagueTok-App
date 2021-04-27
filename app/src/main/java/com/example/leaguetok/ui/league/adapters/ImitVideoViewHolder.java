package com.example.leaguetok.ui.league.adapters;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.R;
import com.example.leaguetok.ui.league.adapters.ImitVideoAdapter;

public class ImitVideoViewHolder extends RecyclerView.ViewHolder {
    TextView txtImitPlace;
    TextView txtImitName;
    TextView txtImitGrade;


    public ImitVideoViewHolder(@NonNull View itemView) {
        super(itemView);

        txtImitPlace = itemView.findViewById(R.id.league_lsitrow_place);
        txtImitName = itemView.findViewById(R.id.league_lsitrow_name);
        txtImitGrade = itemView.findViewById(R.id.league_lsitrow_grade);

    }
}
