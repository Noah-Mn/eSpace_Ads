package com.example.espace_ads.models;

public class AdData {
    public String primaryText;
    public String media;
    public String headline;
    public String description;
    public String destination;

    public AdData(String headline) {
        this.headline = headline;
    }

    public AdData() {
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
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

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
