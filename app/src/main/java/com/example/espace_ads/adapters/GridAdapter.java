package com.example.espace_ads.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.espace_ads.R;

public class GridAdapter extends BaseAdapter {

    private String[] images;
    Context context;
    LayoutInflater layoutInflater;
    View view;

    public GridAdapter(String[] images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view = new View(context);
            view = layoutInflater.inflate(R.layout.grid_item, null);
            AppCompatImageView imageView = view.findViewById(R.id.grid_item_image);
            Glide.with(context).load(images[position]).into(imageView);

        }
        return view;
    }
}
