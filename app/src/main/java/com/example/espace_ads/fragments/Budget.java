package com.example.espace_ads.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.espace_ads.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Budget extends Fragment {

    MaterialSpinner frequencySp, amountSp, amountPerViewSp;
    TextInputEditText customAmount, startTime, endTime, startDate, endDate;
    MaterialCardView custom, btnCalculate;
    private Calendar calendar;
    private int currentHour;
    private int currentMinute;
    private TimePickerDialog timePickerDialog;
    String startingDate, endingDate, startingTime, endingTime;
    private DatePickerDialog datePickerDialog;
    private OnFragmentInteractionListener mListener;
    final int REQUEST_CODE = 11;
    final int REQUEST_CODE_2 = 10;

    public Budget() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        startDate = view.findViewById(R.id.editText_date);
        endDate = view.findViewById(R.id.editText_end_date);



        setAmountSpinner(view);
        setFrequencySpinner(view);
        setAmountPerViewSpinner(view);
        loadTimePicker();
        datePicker();

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
                try {
                    calculateDays();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(getContext(), "Days "+ days, Toast.LENGTH_SHORT).show();
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
        disableSoftInputFromAppearing(startTime, startDate);
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

        disableSoftInputFromAppearing(endTime, endDate);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void datePicker(){
        disableSoftInputFromAppearing(startTime, startDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
//                 create the datePickerFragment
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(Budget.this, REQUEST_CODE);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
//                 create the datePickerFragment
                AppCompatDialogFragment newFragment = new DatePickerFragment();
                // set the targetFragment to receive the results, specifying the request code
                newFragment.setTargetFragment(Budget.this, REQUEST_CODE_2);
                // show the datePicker
                newFragment.show(fm, "datePicker");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for the results
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            String selectedDate = data.getStringExtra("selectedDate");
            // set the value of the editText
            startDate.setText(selectedDate);
        }else if (requestCode == REQUEST_CODE_2 && resultCode == Activity.RESULT_OK){
            String selectedDate = data.getStringExtra("selectedDate");
            endDate.setText(selectedDate);
        }
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static void disableSoftInputFromAppearing(TextInputEditText pTime, TextInputEditText date) {
        if (Build.VERSION.SDK_INT >= 11) {
            pTime.setRawInputType(InputType.TYPE_NULL);
            date.setRawInputType(InputType.TYPE_NULL);
            pTime.setTextIsSelectable(false);
            date.setTextIsSelectable(false);
        } else {
            pTime.setRawInputType(InputType.TYPE_NULL);
            date.setRawInputType(InputType.TYPE_NULL);
            pTime.setFocusable(false);
            date.setFocusable(false);
        }
    }
    public void getInfo(){
        startingTime = Objects.requireNonNull(startTime.getText()).toString();
        endingTime = Objects.requireNonNull(endTime.getText()).toString();
        startingDate = Objects.requireNonNull(startDate.getText()).toString();
        endingDate = Objects.requireNonNull(endDate.getText()).toString();
    }
    public void calculateDays() throws ParseException {
        getInfo();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date date1 = sdf.parse(startingDate);
        Date date2 = sdf.parse(endingDate);

        assert date1 != null;
        assert date2 != null;
        long difference = Math.abs(date2.getTime() - date1.getTime());
        long daysBetween = TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);

        Toast.makeText(getContext(), "Days" + daysBetween, Toast.LENGTH_SHORT).show();

    }

}