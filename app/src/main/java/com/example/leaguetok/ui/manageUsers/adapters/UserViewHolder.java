package com.example.leaguetok.ui.manageUsers.adapters;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.leaguetok.LeagueTokApplication;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.User;
import com.google.android.material.chip.Chip;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private ImageView userImage;
    private TextView txtName;
    private Chip chipAdmin;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.users_listrow_img);
        txtName = itemView.findViewById(R.id.users_listrow_name);
        chipAdmin = itemView.findViewById(R.id.users_listrow_admin_chip);
    }

    public void bind(User user) {
        Glide.with(LeagueTokApplication.context).load(user.getPhotoUrl()).placeholder(R.drawable.profile_placeholder).into(userImage);
        txtName.setText(user.getName());
        chipAdmin.setChecked(user.isAdmin());

        if(user.isAdmin()) {
            chipAdmin.setChipBackgroundColorResource(R.color.green);
            chipAdmin.setChipStrokeWidth(0);
        }
        else {
            chipAdmin.setChipBackgroundColorResource(R.color.white);
            chipAdmin.setChipStrokeWidth(4);
        }

        chipAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CircularProgressDrawable cpDrawable = new CircularProgressDrawable(view.getContext());
                cpDrawable.setStyle(CircularProgressDrawable.DEFAULT);
                cpDrawable.start();

                chipAdmin.setChipIcon(cpDrawable);
                chipAdmin.setChipIconVisible(true);

                Model.instance.setIsAdmin(user.getId(), chipAdmin.isChecked(), new Model.AsyncListener() {
                    @Override
                    public void onComplete(Object data) {
                        chipAdmin.setChipIconVisible(false);
                        if (chipAdmin.isChecked()) {
                            chipAdmin.setChipBackgroundColorResource(R.color.green);
                            chipAdmin.setChipStrokeWidth(0);

                        }
                        else {
                            chipAdmin.setChipBackgroundColorResource(R.color.white);
                            chipAdmin.setChipStrokeWidth(4);
                        }
                    }

                    @Override
                    public void onError(Object error) {
                        chipAdmin.setChecked(!chipAdmin.isChecked());
                        chipAdmin.setChipIconVisible(false);
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Oops...")
                                .setMessage("We are sorry but there was a problem while updating user " + user.getName())
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                });
            }
        });
    }
}
