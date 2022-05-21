package com.example.espace_ads.models;

import java.io.Serializable;

public class LiveCampaignModel implements Serializable {
    String primaryText, headline, impressions;
    int impressionNumber;

    public LiveCampaignModel() {
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
