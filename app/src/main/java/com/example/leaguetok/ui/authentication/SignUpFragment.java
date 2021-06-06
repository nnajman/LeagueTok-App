package com.example.leaguetok.ui.authentication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leaguetok.MainActivity;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.example.leaguetok.model.StoreModel;
import com.example.leaguetok.utils.Listeners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SignUpFragment extends Fragment {
    private FirebaseAuth mAuth;
    private View view;
    ImageView progressBar;
    private boolean isPhotoUploaded = false;
    public static final int GET_FROM_GALLERY = 3;
    private TextInputEditText fullNameEt;
    private String profilePhotoUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        progressBar = view.findViewById(R.id.sign_up_loading);
        Glide.with(view).load(R.drawable.spinner).fitCenter().override(150, 150).into(progressBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TextInputLayout fullNameLayout = view.findViewById(R.id.sign_up_full_name_layout);
        fullNameEt = view.findViewById(R.id.sign_up_full_name_et);
        Listeners.addListenerToRequiredEditText(fullNameLayout, fullNameEt);

        TextInputLayout emailLayout = view.findViewById(R.id.sign_up_email_layout);
        TextInputEditText emailEt = view.findViewById(R.id.sign_up_email_et);
        Listeners.addListenerToRequiredEditText(emailLayout, emailEt);

        TextInputLayout passwordLayout = view.findViewById(R.id.sign_up_password_layout);
        TextInputEditText passwordEt = view.findViewById(R.id.sign_up_password_et);
        Listeners.addListenerToRequiredEditText(passwordLayout, passwordEt);

        TextInputLayout repasswordLayout = view.findViewById(R.id.sign_up_repassword_layout);
        TextInputEditText repasswordEt = view.findViewById(R.id.sign_up_repassword_et);
        Listeners.addListenerToRequiredEditText(repasswordLayout, repasswordEt);
        Button registerBtn = view.findViewById(R.id.sign_up_btn);

        registerBtn.setOnClickListener(b -> {
            createAccount(fullNameEt.getText().toString(), emailEt.getText().toString(), passwordEt.getText().toString(), passwordEt.getText().toString());
        });

        TextView navigationText = view.findViewById(R.id.sign_up_navigation_txt);
        navigationText.setOnClickListener(b -> {
            NavController navCtrl = Navigation.findNavController(view);
            navCtrl.popBackStack();
        });

        ImageButton uploadPhotoBtn = view.findViewById(R.id.sign_up_upload_photo_btn);

        uploadPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        GET_FROM_GALLERY);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                StoreModel.uploadImage(bitmap,
                        fullNameEt.getText().toString() + new Date().getTime(), new StoreModel.Listener() {
                    @Override
                    public void onSuccess(String url) {
                        profilePhotoUrl = url;
                        isPhotoUploaded = true;
                        TextView uploadPhotoTxt = view.findViewById(R.id.sign_up_upload_photo_txt);
                        uploadPhotoTxt.setText("Photo was uploaded successfully!");
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(getActivity(), "Error uploading photo",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void createAccount(String fullName, String email, String password, String repassword) {
        if (fullName.length() == 0 ||
                email.length() == 0 ||
                password.length() == 0 ||
                repassword.length() == 0) {
            Toast.makeText(getActivity(), "Please fill all the fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(repassword)) {
            Toast.makeText(getActivity(), "Password doesn't match the repassword",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isPhotoUploaded) {
            Toast.makeText(getActivity(), "Please upload profile photo",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            if (signInMethods.size() > 0) {
                                // User can sign in with email/password
                                Toast.makeText(getActivity(), "The user already exists",
                                        Toast.LENGTH_SHORT).show();
                            }  else {
                                mAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Log.d("TAG", "createUserWithEmail:success");
                                                    Model.instance.addNewUser(mAuth.getCurrentUser().getUid(),
                                                            fullName,
                                                            profilePhotoUrl,
                                                            new Model.AsyncListener() {
                                                        @Override
                                                        public void onComplete(Object data) {
                                                            launchMainActivity();
                                                        }

                                                        @Override
                                                        public void onError(Object error) {

                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Invalid email",
                                    Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void launchMainActivity() {
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}