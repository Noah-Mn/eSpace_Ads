package com.example.espace_ads.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.espace_ads.R;

public class Notifications extends Fragment {

    View view;

    public Notifications() {
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
        view = inflater.inflate(R.layout.fragment_notifications, container, false);

        notification();
        return view;
    }

    private void notification() {

//        NotificationManager notification = (NotificationManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().NOTIFICATION_SERVICE);
////        int icon = R.drawable.ic_launcher;
//        CharSequence tickerText = "your daily tip";
//        long when = System.currentTimeMillis();
//        Context context = getActivity();
//        CharSequence contentTitle = "AutoKit";
//        CharSequence contentText = "hi";
//        Intent notificationIntent = new Intent();
//        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
//        Notification notification1 = new Notification(icon, tickerText, when);
//        notification1.setLatestEventInfo(context, contextTitle, contentText, contentIntent);
//        NotificationManager.notify(1, notification);
    }
}