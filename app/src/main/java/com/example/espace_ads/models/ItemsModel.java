package com.example.espace_ads.models;

import android.net.Uri;

public class ItemsModel {
    String name, description;
    int prices;
    Uri productImage;

    public ItemsModel() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrices() {
        return prices;
    }

    public void setPrices(int prices) {
        this.prices = prices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getProductImage() {
        return productImage;
    }

    public void setProductImage(Uri productImage) {
        this.productImage = productImage;
    }
}
