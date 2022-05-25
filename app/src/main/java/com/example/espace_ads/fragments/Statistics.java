package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.CustomExpandedListAdapter;
import com.example.espace_ads.adapters.LiveCampaignAdapter;
import com.example.espace_ads.models.LiveCampaignModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Statistics extends Fragment {
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    RecyclerView liveCampaignListView;
    ArrayList<LiveCampaignModel> liveCampaignModelList = new ArrayList<>();

    private static final String STACK_1_LABEL = "78% Females";
    private static final String STACK_2_LABEL = "22% Males";

    private static final String STACK_3_LABEL = "20% 10-24Yrs";
    private static final String STACK_4_LABEL = "35% 25-34Yrs";
    private static final String STACK_5_LABEL = "23% 35-64Yrs";
    private static final String STACK_6_LABEL = "7% 65Yrs +";

    private static final String SET_LABEL = " ";

    private HorizontalBarChart barChart1, barChart2;

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
//        expandableListView.setGroupIndicator(R.drawable.custom_expandable);
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        liveCampaignListView = view.findViewById(R.id.live_campaigns_list);

        barChart1 = view.findViewById(R.id.chart1);
        barChart2 = view.findViewById(R.id.chart2);

        BarData data = createChartData();
        configureChartAppearance();
        prepareChartData(data);

        BarData data1 = createChartData1();
        prepareChartData1(data1);

//        inflate continents
        expandableListTitle = new ArrayList<>();
        expandableListDetail = new HashMap<>();

        expandableListTitle.add("Africa");
        expandableListTitle.add("America");
        expandableListTitle.add("Europe");
        expandableListTitle.add("Asia");
        expandableListTitle.add("Australia");

        List<String> africa = new ArrayList<>();
        africa.add("Malawi" + " "+" 20000 impressions");
        africa.add("Zambia" + " "+" 20000 impressions");
        africa.add("Namibia" + " "+" 20000 impressions");

        List<String> america = new ArrayList<>();
        america.add("Malawi" + " "+" 20000 impressions");
        america.add("Zambia" + " "+" 20000 impressions");
        america.add("Namibia" + " "+" 20000 impressions");

        List<String> europe = new ArrayList<>();
        europe.add("Malawi" + " "+" 20000 impressions");
        europe.add("Zambia" + " "+" 20000 impressions");
        europe.add("Namibia" + " "+" 20000 impressions");

        List<String> asia = new ArrayList<>();
        asia.add("Malawi" + " "+" 20000 impressions");
        asia.add("Zambia" + " "+" 20000 impressions");
        asia.add("Namibia" + " "+" 20000 impressions");

        List<String> australia = new ArrayList<>();
        australia.add("Malawi" + " "+" 20000 impressions");
        australia.add("Zambia" + " "+" 20000 impressions");
        australia.add("Namibia" + " "+" 20000 impressions");

        expandableListDetail.put(expandableListTitle.get(0), africa);
        expandableListDetail.put(expandableListTitle.get(1), america);
        expandableListDetail.put(expandableListTitle.get(2), europe);
        expandableListDetail.put(expandableListTitle.get(3), asia);
        expandableListDetail.put(expandableListTitle.get(4), australia);

        expandableListAdapter = new CustomExpandedListAdapter(getContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        LiveCampaignAdapter adapter = new LiveCampaignAdapter(liveCampaignModelList, getContext());
        liveCampaignListView.setHasFixedSize(true);
        liveCampaignListView.setAdapter(adapter);


        db.collection("Advert")
                .whereEqualTo("Email Address", getEmail())
                .whereEqualTo("Status", "Live")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && task.getResult() != null) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String headline = documentSnapshot.getString("Headline");
                                String primaryText = documentSnapshot.getString("Primary Text");
                                LiveCampaignModel liveCampaignModel = new LiveCampaignModel(headline, primaryText);
                                liveCampaignModelList.add(liveCampaignModel);
                            }
                            adapter.setLiveCampaignList(liveCampaignModelList);
                        } else {
                            Toast.makeText(getContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return view;
    }
    private void configureChartAppearance() {
        barChart1.setDrawGridBackground(false);
        barChart1.setDrawValueAboveBar(false);
        barChart1.getDescription().setEnabled(false);

        XAxis xAxis = barChart1.getXAxis();
        xAxis.setGranularity(1f);

        YAxis leftAxis = barChart1.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        barChart1.getXAxis().setDrawGridLines(false);
        barChart1.getXAxis().setEnabled(false);
        barChart1.getAxisLeft().setEnabled(false);
        barChart1.getAxisRight().setEnabled(false);

        barChart2.setDrawGridBackground(false);
        barChart2.setDrawValueAboveBar(false);
        barChart2.getXAxis().setDrawGridLines(false);
        barChart2.getXAxis().setEnabled(false);
        barChart2.getAxisLeft().setEnabled(false);
        barChart2.getAxisRight().setEnabled(false);

        barChart2.getDescription().setEnabled(false);

        XAxis xAxis1 = barChart2.getXAxis();
        xAxis1.setGranularity(1f);

        YAxis leftAxis1 = barChart2.getAxisLeft();
        leftAxis1.setDrawGridLines(false);

    }

    private BarData createChartData() {
        ArrayList<BarEntry> values = new ArrayList<>();

            float value1 = 10;
            float value2 = 15;

            values.add(new BarEntry( 1, new float[]{value1, value2}));


        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        set1.setColors(new int[] {getResources().getColor(R.color.vibrant_pink), getResources().getColor(R.color.light_skyBlue)});
        set1.setStackLabels(new String[] {STACK_1_LABEL, STACK_2_LABEL});

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        barChart1.setData(data);
        barChart1.getLegend().setTextColor(getResources().getColor(R.color.white));
        barChart1.getLegend().setWordWrapEnabled(true);
        data.setDrawValues(false);
        return data;
    }

    private BarData createChartData1() {
        ArrayList<BarEntry> values = new ArrayList<>();

        float value1 = 7;
        float value2 = 15;
        float value3 = 45;
        float value4 = 20;

        values.add(new BarEntry( 1, new float[]{value1, value2, value3, value4}));

        BarDataSet set1 = new BarDataSet(values, SET_LABEL);

        set1.setColors(new int[] {getResources().getColor(R.color.vibrant_pink), getResources().getColor(R.color.rainBow_orange), getResources().getColor(R.color.light_skyBlue), getResources().getColor(R.color.whatever_color)});
        set1.setStackLabels(new String[] {STACK_3_LABEL, STACK_4_LABEL,STACK_5_LABEL,STACK_6_LABEL});

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        barChart2.setData(data);
        barChart2.getLegend().setTextColor(getResources().getColor(R.color.white));

        barChart2.getLegend().setWordWrapEnabled(true);
        Legend legend = barChart2.getLegend();
        data.setDrawValues(false);
        return data;
    }

    private void prepareChartData(BarData data) {
        data.setValueTextSize(12f);
        barChart1.setData(data);
        barChart1.invalidate();
    }
    private void prepareChartData1(BarData data) {
        data.setValueTextSize(12f);
        barChart2.setData(data);
        barChart2.invalidate();
    }

    public String getEmail() {
        String emailAddress;
        emailAddress = currentUser.getEmail();
        return emailAddress;
    }

}