package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.espace_ads.R;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class BusinessProfileBuy extends Fragment {
    View view;
    private AppCompatImageView productImage;
    private MaterialTextView itemName, itemPrice, itemDescr;

    public BusinessProfileBuy() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String productName = getArguments().getString("productName");
        String description = getArguments().getString("descrip");
        String image = getArguments().getString("image");
        int price = getArguments().getInt("price");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_business_profile_buy, container, false);
        productImage = view.findViewById(R.id.product_image);
        itemPrice = view.findViewById(R.id.text_item_price);
        itemName = view.findViewById(R.id.text_item_name);
        itemDescr = view.findViewById(R.id.text_product_description);

        try {
            URL url = new URL(image);
            Picasso.with(getContext()).load(String.valueOf(url)).into(productImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        itemPrice.setText("MKW "+ price);
        itemName.setText(productName);
        itemDescr.setText(description);

        return view;
    }
}