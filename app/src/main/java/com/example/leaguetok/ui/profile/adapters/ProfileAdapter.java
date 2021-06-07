package com.example.leaguetok.ui.profile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.leaguetok.R;
import com.example.leaguetok.model.ImitationVideo;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.NodeService;
import com.example.leaguetok.model.OriginalVideo;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int PROFILE_DETAILS_VIEW_TYPE = 0;
    private final int IMIT_VIDEO_VIEW_TYPE = 1;
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

    @Override
    public int getItemViewType(int position) {
        // return 0 if first row (profile details)
        return position == 0 ? PROFILE_DETAILS_VIEW_TYPE : IMIT_VIDEO_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // check if view type is 0 indicating it's the first row (profile details)
        if (viewType == PROFILE_DETAILS_VIEW_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_row, parent, false);
            return new ProfileAdapter.ProfileDetailsViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_row, parent, false);
            return new ProfileAdapter.VideoViewHolder(view, mListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // check if view type is 0 indicating it's the first row (profile details)
        if (position == 0) {
            Model.instance.getUserById(FirebaseAuth.getInstance().getCurrentUser().getUid(), new NodeService.RequestListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject response) throws JSONException {
                    ((ProfileAdapter.ProfileDetailsViewHolder)holder).bind(response.get("name").toString(), response.get("photoUrl").toString());
                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        }
        else {
            ((ProfileAdapter.VideoViewHolder) holder).bind(videos.get(position));
        }
    }

    @Override
    public int getItemCount() {
        // returning the size of array list + profile details.
        return videos.size();
    }

    public class ProfileDetailsViewHolder extends RecyclerView.ViewHolder {

        private ImageView profileImage;
        private TextView txtName;

        public ProfileDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_listrow_img);
            txtName = itemView.findViewById(R.id.profile_listrow_name);
        }

        public void bind(String userName, String photoUrl) {
            Glide.with(context).load(photoUrl).into(profileImage);
            txtName.setText(userName);
        }
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder {

        // creating variables for our views.
        private TextView rowName;
        private ImageView rowVideo;

        public VideoViewHolder(@NonNull View itemView, OnItemClickListener listener) {
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
            Glide
                    .with(context)
                    .load(video.getUrl())
                    .thumbnail(Glide.with(context).load(R.drawable.spinner_100))
                    .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                    .fitCenter()
                    .into(rowVideo);

            Model.instance.getOrigVideoById(video.getSourceId(), new Model.AsyncListener<OriginalVideo>() {
                @Override
                public void onComplete(OriginalVideo data) {
                    rowName.setText(context.getString(R.string.upload_title, data.getName(), data.getPerformer()));
                }

                @Override
                public void onError(OriginalVideo error) {

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
