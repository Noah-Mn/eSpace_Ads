package com.example.espace_ads.models;

public class RecentCampaignModel {
    String headline, impressions;
    int impressionNumber;

    public RecentCampaignModel() {
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getImpressions() {
        return impressions;
    }

    public void setImpressions(String impressions) {
        this.impressions = impressions;
    }

    public int getImpressionNumber() {
        return impressionNumber;
    }

    public void setImpressionNumber(int impressionNumber) {
        this.impressionNumber = impressionNumber;
    }
}
