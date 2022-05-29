package com.example.espace_ads.fragments;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.espace_ads.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Budget extends Fragment {

    MaterialSpinner frequencySp, amountSp, amountPerViewSp;
    TextInputEditText customAmount, startTime, endTime, startDate, endDate;
    MaterialCardView custom, btnCalculate;
    private Calendar calendar;
    private int currentHour;
    private int currentMinute;
    private TimePickerDialog timePickerDialog;
    String startingDate, endingDate, startingTime, endingTime;

    public Budget() {
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
        View view = inflater.inflate(R.layout.fragment_budget, container, false);

        frequencySp = view.findViewById(R.id.spinner_frequency);
        amountSp = view.findViewById(R.id.spinner_amount);
        amountPerViewSp = view.findViewById(R.id.spinner_amount_per_view);
        customAmount = view.findViewById(R.id.custom_amount);
        custom = view.findViewById(R.id.custom);
        startTime = view.findViewById(R.id.editText_time);
        endTime = view.findViewById(R.id.editText_end_time);
        btnCalculate = view.findViewById(R.id.calculate_btn);

        setAmountSpinner(view);
        setFrequencySpinner(view);
        setAmountPerViewSpinner(view);
        loadTimePicker();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }

    public void setAmountSpinner(View view) {

        String[] displayAmount = new String[]{"20,000", "50,000", "80,000", "110,000", "140,000", "Custom"};
        long[] amountValues = new long[]{20000L, 50000L, 80000L, 110000L, 140000L, 0L};

        ArrayAdapter<String> amountAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, displayAmount);
        amountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        amountSp.setAdapter(amountAdapter);

        amountSp.setOnItemSelectedListener((view1, position, id, item) -> {

                if (position == amountValues.length - 1) {
                    custom.setVisibility(View.VISIBLE);
                    /***<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<keeps giving an error>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ****/
//                    long value = Integer.parseInt(customAmount.getText().toString());
//                    Toast.makeText(getContext(), "Entered"+value, Toast.LENGTH_SHORT).show();


//                   will set value here
                } else {
                    custom.setVisibility(View.GONE);
                   long value = amountValues[position];
                    Toast.makeText(getContext(), "Selected"+value, Toast.LENGTH_SHORT).show();

                   /*******else another value here********/
                }

        });
        amountSp.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
//                if nothing selected default is position 0
                long value = amountValues[0];
            }
        });
        amountSp.setFocusable(false);
    }


    public void setFrequencySpinner(View view) {

        String[] frequency = new String[]{"Daily", "Weekly", "Monthly"};
//        these values are in hours to be changed if need be
        long[] values = new long[]{24L,168L, 672L};

        ArrayAdapter<String> frequencyAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, frequency);
        frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        frequencySp.setAdapter(frequencyAdapter);
//        frequencySp.setFocusedByDefault(true);
        frequencySp.setOnItemSelectedListener((view1, position, id, item) -> {

                long value = values[position];
                Toast.makeText(getContext(), "Selected"+value, Toast.LENGTH_SHORT).show();

        });
        frequencySp.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                long value = values[0];
                Toast.makeText(getContext(), "Selected" + value, Toast.LENGTH_SHORT).show();
            }
        });
        frequencySp.setFocusable(false);
    }

    public void setAmountPerViewSpinner(View view) {

        ArrayList<String> amountPerView = new ArrayList<>();

        amountPerView.add("Views- MKW5/view");
        amountPerView.add("Targeted Views- MKW10/view");
        amountPerView.add("Interaction- MKW15/view");
        amountPerView.add("Targeted Interaction- MKW20/view");

        ArrayAdapter<String> amountPerViewAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, amountPerView);
        amountPerViewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        amountPerViewSp.setAdapter(amountPerViewAdapter);
        amountPerViewSp.setFocusable(false);
    }

    public void loadTimePicker() {
        disableSoftInputFromAppearing(startTime);
        startTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        startTime.setText(String.format("%02d : %02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        disableSoftInputFromAppearing(endTime);
        endTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        endTime.setText(String.format("%02d : %02d", hourOfDay, minutes));
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

    }

    public static void disableSoftInputFromAppearing(TextInputEditText pTime) {
        if (Build.VERSION.SDK_INT >= 11) {
            pTime.setRawInputType(InputType.TYPE_NULL);
            pTime.setTextIsSelectable(false);
        } else {
            pTime.setRawInputType(InputType.TYPE_NULL);
            pTime.setFocusable(false);
        }
    }
    public void getInfo(){
        startingTime = startTime.getText().toString();
        endingTime = endTime.getText().toString();
    }
    public long calculate() throws ParseException {
        getInfo();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = sdf.parse(startingDate);
        Date date2 = sdf.parse(endingDate);

        assert date1 != null;
        assert date2 != null;
        long numDays = date1.getTime()- date2.getTime();

        Toast.makeText(getContext(), "Days" + numDays, Toast.LENGTH_SHORT).show();

        return numDays;

    }
}