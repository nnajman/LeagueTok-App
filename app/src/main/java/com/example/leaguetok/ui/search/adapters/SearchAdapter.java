package com.example.leaguetok.ui.search.adapters;

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
import com.example.leaguetok.model.OriginalVideo;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<OriginalVideo> videos;
    private Context context;
    private OnItemClickListener mListener;
    public static String currentSearch;
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    // creating a constructor for our variables.
    public SearchAdapter(List<OriginalVideo> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    // method for filtering our recyclerview items.
    public void filterList(List<OriginalVideo> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        videos = filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is to inflate our layout.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_row, parent, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        holder.bind(videos.get(position));
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.
        private TextView searchRowName;
        private ImageView searchRowVideo;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // initializing our views with their ids.
            searchRowName = itemView.findViewById(R.id.video_list_row_name);
            searchRowVideo = itemView.findViewById(R.id.video_list_row_video);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            });
        }

        public void bind(OriginalVideo video) {
            searchRowName.setText(video.getName());
            Glide.with(context).load(video.getUri()).thumbnail(Glide.with(context).load(R.drawable.loading_spinner)).into(searchRowVideo);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
