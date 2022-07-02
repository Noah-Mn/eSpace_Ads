package com.example.espace_ads.activityClasses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.espace_ads.databinding.ActivityCreateAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccount extends AppCompatActivity {

    FirebaseFirestore db;
    ActivityCreateAccountBinding binding;
    FirebaseAuth firebaseAuth;
    String email, password, name, confirmPassword;
    private boolean valid;
    private static final String TAG = "Create Account";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        listeners();
    }

    public void listeners() {

        binding.createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (validateForm() && validateEmail()){
                   performSignUp();
               }
            }
        });
    }

    private void addUser() {

        if (validateForm())
        db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();

        user.put("Full Name", name);
        user.put("Email Address", email);
        user.put("Status", "Ad_Investor");

        db.collection("Ad_Investors")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {


                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String ids = documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "error! failed");
                    }
                });

    }

    private void performSignUp() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up please wait...");
        progressDialog.setTitle("UserSignup");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);

                    startActivity(new Intent(CreateAccount.this, HomePage.class));
                    Toast.makeText(getApplicationContext(), "Sign up successful", Toast.LENGTH_SHORT).show();
                    addUser();
                } else {
                    progressDialog.dismiss();
                    Log.d(TAG, " UserSignup:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "User Signup failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
    }

    private boolean validateEmail() {
        db.collection("Ad_Investors")
                .whereEqualTo("Email Address", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, "Email already exist!");
                                binding.materialEmailAddress.setError("Email address already in use");
                                valid = false;
                            }
                        } else {
                            Log.d(TAG, "Email address not in use", task.getException());
                            valid = true;
                        }
                    }
                });
        return valid;
    }
    private boolean validateForm(){
        valid = false;
        email = Objects.requireNonNull(binding.emailAddress.getText()).toString().trim();
        password = Objects.requireNonNull(Objects.requireNonNull(binding.password.getText()).toString());
        confirmPassword = Objects.requireNonNull(Objects.requireNonNull(binding.textConfirmPassword.getText()).toString());
        name = Objects.requireNonNull(binding.fullName.getText()).toString();

        try{
            if(email.isEmpty()){
                binding.materialEmailAddress.setError("Please enter email address");
            }
            else if (name.isEmpty()){
                binding.materialFullName.setError("Please enter your name");
            }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.materialEmailAddress.setError("Invalid email address");
            } else if(password.isEmpty()){
                binding.materialPassword.setError("Please enter password");
            } else if (password.length() <= 6) {
                binding.materialPassword.setError("Password must be more than 6 characters");
            } else if(!password.matches(confirmPassword)){
                binding.materialConfirmPassword.setError("Passwords do not match");
            } else {
                valid = true;
            }


        } catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, e.getMessage());
        }
        return valid;
    }

}