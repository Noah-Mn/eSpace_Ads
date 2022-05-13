package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.CustomExpandedListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Statistics extends Fragment {
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_statistics, container, false);
        expandableListView = view.findViewById(R.id.expandable_list_view);


//        inflate continents
        expandableListTitle = new ArrayList<>();
        expandableListDetail = new HashMap<>();

        expandableListTitle.add("Africa");
        expandableListTitle.add("America");
        expandableListTitle.add("Europe");
        expandableListTitle.add("Asia");
        expandableListTitle.add("Australia");

        List<String> africa = new ArrayList<>();
        africa.add("Malawi");
        africa.add("Zambia");
        africa.add("Namibia");

        List<String> america = new ArrayList<>();
        america.add("Malawi");
        america.add("Zambia");
        america.add("Namibia");

        List<String> europe = new ArrayList<>();
        europe.add("Malawi");
        europe.add("Zambia");
        europe.add("Namibia");

        List<String> asia = new ArrayList<>();
        asia.add("Malawi");
        asia.add("Zambia");
        asia.add("Namibia");

        List<String> australia = new ArrayList<>();
        australia.add("Malawi");
        australia.add("Zambia");
        australia.add("Namibia");

        expandableListDetail.put(expandableListTitle.get(0), africa);
        expandableListDetail.put(expandableListTitle.get(1), america);
        expandableListDetail.put(expandableListTitle.get(2), europe);
        expandableListDetail.put(expandableListTitle.get(3), asia);
        expandableListDetail.put(expandableListTitle.get(4), australia);

        expandableListAdapter = new CustomExpandedListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        return view;
    }
}