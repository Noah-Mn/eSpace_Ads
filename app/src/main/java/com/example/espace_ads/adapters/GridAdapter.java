package com.example.espace_ads.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.espace_ads.databinding.GridItemBinding;
import com.example.espace_ads.models.ItemsModel;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.StoreItemViewHolder> {

    public interface OnItemClickListener {
        void OnItemClick(ItemsModel item);
    }

    private ArrayList<ItemsModel> itemsModels;
    private final Context context;
    private final OnItemClickListener listener;

    public GridAdapter(ArrayList<ItemsModel> itemsModels, Context context, OnItemClickListener listener) {
        this.itemsModels = itemsModels;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GridAdapter.StoreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GridItemBinding gridItemBinding = GridItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StoreItemViewHolder(gridItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreItemViewHolder holder, int position) {
//        ItemsModel itemsModel = itemsModels.get(position);
        holder.bind(itemsModels.get(position), listener);
//        try {
//            URL url = new URL(itemsModel.getProductImage());
//            Picasso.with(context).load(String.valueOf(url)).into(holder.binding.gridItemImage);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }


    }

    @Override
    public int getItemCount() {
        return itemsModels.size();
    }

    class StoreItemViewHolder extends RecyclerView.ViewHolder {

        GridItemBinding binding;

        public StoreItemViewHolder(@NonNull GridItemBinding gridItemBinding) {
            super(gridItemBinding.getRoot());
            binding = gridItemBinding;
        }

        public void bind(final ItemsModel itemsModel, final OnItemClickListener onItemClickListener) {
            try {
                URL url = new URL(itemsModel.getProductImage());
                Picasso.with(context).load(String.valueOf(url)).into(binding.gridItemImage);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            binding.gridItemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(itemsModel);
                }
            });
        }
    }

//    String getItem(int id) {
//        return itemsModels[id];
//    }

    // allows clicks events to be caught
//    public void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }

//    public interface ItemClickListener {
//        void onItemClick(View view, int position);
//    }

    public void setStoreItemsList(ArrayList<ItemsModel> itemsList) {
        this.itemsModels = itemsList;
        notifyDataSetChanged();
    }
}
