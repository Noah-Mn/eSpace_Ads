package com.example.espace_ads.models;

import android.net.Uri;

public class ItemsModel {
    String name, description;
    int prices;
    String productImage;

    public ItemsModel() {
    }

    public ItemsModel(String name, int prices, String description, String productImage) {
        this.name = name;
        this.prices = prices;
        this.description = description;
        this.productImage = productImage;

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

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
