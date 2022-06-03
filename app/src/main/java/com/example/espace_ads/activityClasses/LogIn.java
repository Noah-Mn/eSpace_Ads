package com.example.espace_ads.activityClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.espace_ads.R;
import com.example.espace_ads.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogIn extends AppCompatActivity {

    ActivityLogInBinding binding;
    String logEmailAddress, logPassword;
    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);

        listeners();
    }

    private void listeners(){
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        binding.materialCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, CreateAccount.class));
            }
        });
    }

    private void performLogin(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        logEmailAddress = Objects.requireNonNull(binding.loginEmail.getText()).toString();
        logPassword = Objects.requireNonNull(binding.loginPassword.getText()).toString();

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(logEmailAddress).matches()){
            binding.materialLogEmail.setError("Please enter a valid email address");
        }else if (logEmailAddress.isEmpty()){
            binding.materialLogEmail.setError("Enter email address");
        } else if (logPassword.isEmpty()){
            binding.materialLogPassword.setError("Please enter password");
        }else{
            progressDialog.setMessage("Logging in please wait...");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(logEmailAddress, logPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                private static final String TAG = "Log In Class";

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        startActivity(new Intent(LogIn.this, HomePage.class));
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        LogIn.this.updateUI(user);

                    } else {
                        progressDialog.dismiss();
                        LogIn.this.updateUI(null);
                        Log.w(TAG, " Login:failure", task.getException());
                        Toast.makeText(LogIn.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void updateUI(FirebaseUser user) {
    }
}