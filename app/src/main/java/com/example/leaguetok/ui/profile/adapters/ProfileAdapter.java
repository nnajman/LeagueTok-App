package com.example.leaguetok.ui.profile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<ImitationVideo> videos;
    private Context context;
    private OnItemClickListener mListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // creating a constructor for our variables.
    public ProfileAdapter(List<ImitationVideo> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_row, parent, false);
        return new ProfileAdapter.ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.
        private TextView rowName;
        private ImageView rowVideo;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // initializing our views with their ids.
            rowVideo = itemView.findViewById(R.id.video_list_row_video);
            rowName = itemView.findViewById(R.id.video_list_row_name);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            });
        }

        public void bind(ImitationVideo video) {
            Glide.with(context).load(video.getUrl()).into(rowVideo);
            rowName.setText("");
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
