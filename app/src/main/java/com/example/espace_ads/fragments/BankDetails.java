package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.espace_ads.R;
import com.example.espace_ads.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class BankDetails extends Fragment {

    View view;
    MaterialCardView linkPaymentMethod, bankDetails;
    FirebaseUser currentUser;
    FirebaseFirestore firebaseFirestore;
    MaterialTextView noAccount, accountName, accountBalance;
    User user;


    public BankDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bank_details, container, false);
        linkPaymentMethod = view.findViewById(R.id.link_visa);
        bankDetails = view.findViewById(R.id.bank_details);
        noAccount = view.findViewById(R.id.no_account);
        accountName = view.findViewById(R.id.account_name);
        accountBalance = view.findViewById(R.id.account_balance);
        user = new User();

        linkPaymentMethod.setOnClickListener(v -> replaceFragments(new AddNewVISA()));

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Billing Method")
                .whereEqualTo("Email Address", getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                Long balance = documentSnapshot.getLong("Balance");
                                assert balance != null;
                                if (balance == 0){
                                    accountBalance.setTextColor(R.color.zero_balance);
                                }

                                bankDetails.setVisibility(View.VISIBLE);
                                noAccount.setVisibility(View.GONE);

//                                linkPaymentMethod.setVisibility(View.GONE);

                                if (user.getFullName() != null) {
                                    accountName.setText(user.getFullName());
                                }
                                if (!String.valueOf(balance).equals("")) {
                                    accountBalance.setText(String.valueOf(balance));

                                }

                            }
                        }
                    }
                }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show());
        return view;
    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.addToBackStack("Home");
        fragmentTransaction.commit();
    }

    public String getEmail() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String emailAddress;
        assert currentUser != null;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }
}