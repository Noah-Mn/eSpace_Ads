package com.example.espace_ads.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.espace_ads.R;
import com.example.espace_ads.models.ItemsModel;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<ItemsModel> {

    Context context;
    ArrayList<ItemsModel> itemsArrayList;

    public GridAdapter(@NonNull Context context, ArrayList<ItemsModel> itemsArrayList) {
        super(context, android.R.id.content, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.grid_item, parent);
        return view;
    }
}
