package com.example.leaguetok.ui.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leaguetok.MainActivity;
import com.example.leaguetok.R;
import com.example.leaguetok.model.Model;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

public class SignInFragment extends Fragment {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1001;
    private CircularProgressIndicator progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        configureGoogleClient();

        TextInputEditText emailEt = view.findViewById(R.id.upload_original_name_et);
        TextInputEditText passwordEt = view.findViewById(R.id.sign_in_password_et);
        Button signInBtn = view.findViewById(R.id.sign_in_btn);
        Button signInWithGoogleBtn = view.findViewById(R.id.sign_in_with_google_btn);
        progressBar = view.findViewById(R.id.sign_in_progress_bar);

        ImageView logo = view.findViewById(R.id.sign_in_logo);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/leaguetok.appspot.com/o/logo_pic.jpeg?alt=media&token=6da71041-0ff7-4e1a-83b6-0b83ade8ed57").into(logo);

        signInBtn.setOnClickListener(b -> {
            login(emailEt.getText().toString(), passwordEt.getText().toString());
        });

        signInWithGoogleBtn.setOnClickListener(b -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        TextView navigationText = view.findViewById(R.id.sign_in_navigation_txt);
        navigationText.setOnClickListener(b -> {
            NavDirections direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment();
            Navigation.findNavController(getActivity(), R.id.authentication_nav_fragment).navigate(direction);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = null;
            try {
                account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("TAG", "signInWithCredential:success: currentUser: " + user.getEmail());
                            Model.instance.addNewUser(mAuth.getCurrentUser().getUid(),
                                    mAuth.getCurrentUser().getDisplayName(),
                                    mAuth.getCurrentUser().getPhotoUrl().toString(),
                                    new Model.AsyncListener() {
                                @Override
                                public void onComplete(Object data) {
                                    launchMainActivity();
                                }

                                @Override
                                public void onError(Object error) {

                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Login failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void login(String email, String password) {
        if (email.length() == 0 || password.length() == 0) {
            Toast.makeText(getActivity(), "Please fill all the fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            launchMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getActivity(), "Invalid Username/Password",
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

    private void configureGoogleClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }
}