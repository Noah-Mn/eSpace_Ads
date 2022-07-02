package com.example.espace_ads.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.espace_ads.R;
import com.example.espace_ads.algorithms.DocumentIDGenerator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddNewVISA extends Fragment {

    LinearLayout visa, mastercard;
    RadioButton visaRadioButton, mastercardRadioButton;
    View view;
    TextInputEditText cardNumber, cardVerification, cardExpiration;
    MaterialCardView save;
    String cardType, expDate, cardNum, cardCode;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser currentUser;
    String docID = DocumentIDGenerator.generateRandomString(20);
    ProgressBar progressBar;
    RadioGroup radioGroup;
    private final String text1 = "VISA";
    private final String text2 = "Mastercard";


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
//        visa = view.findViewById(R.id.visa);
//        mastercard = view.findViewById(R.id.mastercard);
        cardExpiration = view.findViewById(R.id.card_expiration_date);
        cardNumber = view.findViewById(R.id.card_number);
        cardVerification = view.findViewById(R.id.verification_code);
        save = view.findViewById(R.id.save);
        visaRadioButton = view.findViewById(R.id.visa_radioButton);
        mastercardRadioButton = view.findViewById(R.id.mastercard_radioButton);
        progressBar = view.findViewById(R.id.content_loading_progressbar);
        radioGroup = view.findViewById(R.id.billing_methods);


        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                setDataToDatabase();
            }
        });

        return view;
    }

    private void getInfo() {
        cardNum = Objects.requireNonNull(cardNumber.getText()).toString().trim();
        expDate = Objects.requireNonNull(cardExpiration.getText()).toString().trim();
        cardCode = Objects.requireNonNull(cardVerification.getText()).toString().trim();

//        This is not working properly for now ,,,,,,,,,,,,,,,,.................................................
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == R.id.visa_radioButton){
//                   cardType = text1;
//                }else if (checkedId == R.id.mastercard_radioButton){
//                    cardType = text2;
//                }
//            }
//        });

        visaRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visaRadioButton.setChecked(true);
                cardType = text1;
            }
        });

        mastercardRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mastercardRadioButton.setChecked(true);
                cardType = text2;
            }
        });

        if (mastercardRadioButton.isChecked()) {
            cardType = text2;
        } else if (visaRadioButton.isChecked()) {
            cardType = text1;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDataToDatabase() {
        getInfo();

        if (!Objects.requireNonNull(cardVerification.getText()).toString().trim().matches("") &&
                !Objects.requireNonNull(cardExpiration.getText()).toString().trim().matches("") &&
                !Objects.requireNonNull(cardNumber.getText()).toString().trim().matches("") &&
                cardType != null) {

            if (cardNumber.getText().toString().trim().length() < 16 || cardNumber.getText().toString().trim().length() > 16) {
                Toast.makeText(getContext(), "Please enter a valid card number", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else if (cardVerification.getText().toString().trim().length() < 3 || cardVerification.getText().toString().trim().length() > 3) {
                Toast.makeText(getContext(), "Please enter a valid code", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else {
                YearMonth currentDate = YearMonth.now();
                if (currentDate.compareTo(YearMonth.parse(expDate)) > 0) {
                    Toast.makeText(getContext(), "Your card is expired", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else if (currentDate.compareTo(YearMonth.parse(expDate)) < 0) {

                    firebaseFirestore = FirebaseFirestore.getInstance();
                    Map<String, Object> Item = new HashMap<>();

                    Item.put("Card Number", cardNum);
                    Item.put("Card Expiration Date", expDate);
                    Item.put("Verification Code", cardCode);
                    Item.put("Email Address", getEmail());
                    Item.put("Card Type", cardType);
                    Item.put("Balance", 100000000L);

                    firebaseFirestore.collection("Billing Method")
                            .document(docID)
                            .set(Item)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void documentReference) {
                                    Toast.makeText(getContext(), "Data has been saved", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    replaceFragments(new BankDetails());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddNewVISA.this.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                } else if (currentDate.compareTo(YearMonth.parse(expDate)) == 0) {
                    Toast.makeText(getContext(), "Your card will expire today", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        } else {
            Toast.makeText(getContext(), "Data is empty!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        }

    }

    private void replaceFragments(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
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