package com.example.leaguetok.ui.league.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leaguetok.R;

public class ImitVideoViewHolder extends RecyclerView.ViewHolder {
    ImageView imgImitPlace;
    TextView txtImitPlace;
    TextView txtImitName;
    TextView txtImitGrade;


    public ImitVideoViewHolder(@NonNull View itemView) {
        super(itemView);

        imgImitPlace = itemView.findViewById(R.id.league_listrow_imgplace);
        txtImitPlace = itemView.findViewById(R.id.league_lisitrow_rank);
        txtImitName = itemView.findViewById(R.id.league_lisitrow_name);
        txtImitGrade = itemView.findViewById(R.id.league_lisitrow_score);

    }
}
