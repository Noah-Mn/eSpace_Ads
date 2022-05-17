package com.example.espace_ads.models;

public class AdModel {
    String destination, primaryText, headline, description, encodedImage;

    public AdModel(String destination, String primaryText, String headline, String description, String encodedImage) {
        this.destination = destination;
        this.primaryText = primaryText;
        this.headline = headline;
        this.description = description;
        this.encodedImage = encodedImage;
    }

    public AdModel() {

    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }
}
