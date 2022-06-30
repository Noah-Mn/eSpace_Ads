package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.espace_ads.R;
import com.example.espace_ads.algorithms.DocumentIDGenerator;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNewVISA extends Fragment {

    MaterialRadioButton visa, mastercard;
    View view;
    TextInputEditText cardNumber, cardVerification, cardExpiration;
    MaterialCardView save;
    String cardType, expDate, cardNum, cardCode;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser currentUser;
    String docID = DocumentIDGenerator.generateRandomString(20);

    public AddNewVISA() {
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
        view = inflater.inflate(R.layout.fragment_add_new_v_i_s_a, container, false);
        visa = view.findViewById(R.id.visa);
        mastercard = view.findViewById(R.id.mastercard);
        cardExpiration = view.findViewById(R.id.card_expiration_date);
        cardNumber = view.findViewById(R.id.card_number);
        cardVerification = view.findViewById(R.id.verification_code);
        save = view.findViewById(R.id.save);


        setDataToDatabase();
        return view;
    }

    private void getInfo(){
        cardNum = Objects.requireNonNull(cardNumber.getText()).toString().trim();
        expDate = Objects.requireNonNull(cardExpiration.getText()).toString().trim();
        cardCode = Objects.requireNonNull(cardVerification.getText()).toString().trim();
        if (visa.isChecked()){
            cardType = "Visa";
        }else {
            visa.setChecked(false);
        }
        if (mastercard.isChecked()){
            cardType = "Mastercard";
        }else {
            mastercard.setChecked(false);
        }
    }

    private void setDataToDatabase(){
        getInfo();

        firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> Item = new HashMap<>();

        Item.put("Card Number", cardNum);
        Item.put("Card Expiration Date", expDate);
        Item.put("Verification Code", cardCode);
        Item.put("Email Address", getEmail());
        Item.put("Card Type", cardType);

        firebaseFirestore.collection("Billing Method")
                .document(docID)
                .set(Item)
                .addOnSuccessListener(documentReference -> {
//                                    Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());

    }
    public String getEmail() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String emailAddress;
        assert currentUser != null;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }
}