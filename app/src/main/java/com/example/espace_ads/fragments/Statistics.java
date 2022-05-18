package com.example.espace_ads.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.espace_ads.R;
import com.example.espace_ads.adapters.CustomExpandedListAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Statistics extends Fragment {
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    private static final String STACK_1_LABEL = "Females";
    private static final String STACK_2_LABEL = "Males";

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

}