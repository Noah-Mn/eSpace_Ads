package com.example.espace_ads.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.espace_ads.R;
import com.example.espace_ads.models.Upload;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends ArrayAdapter<Upload> {

    private final Context mContext;
    private final int layoutResourceId;
    private ArrayList<Upload> uploads = new ArrayList<Upload>();

    public GridAdapter(Context mContext, int layoutResourceId, ArrayList<Upload> uploads) {
        super(mContext, layoutResourceId, uploads);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.uploads = uploads;
    }


    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Upload> mGridData) {
        this.uploads = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
//            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (AppCompatImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Upload item = uploads.get(position);
//        holder.titleTextView.setText(Html.fromHtml(item.getTitle()));

        Picasso.with(mContext).load(item.getImageUrl()).into(holder.imageView);
        return row;
    }

    static class ViewHolder {
//        TextView titleTextView;
        AppCompatImageView imageView;
    }


}
