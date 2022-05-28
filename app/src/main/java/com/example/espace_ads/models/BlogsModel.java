package com.example.espace_ads.models;

import android.net.Uri;

import java.util.Date;

public class BlogsModel {
    String campaignLogo;
    String campaignDetail;
    String date;

    public BlogsModel(String campaignLogo, String campaignDetail, String date) {
        this.campaignLogo = campaignLogo;
        this.campaignDetail = campaignDetail;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCampaignLogo() {
        return campaignLogo;
    }

    public void setCampaignLogo(String campaignLogo) {
        this.campaignLogo = campaignLogo;
    }

    public String getCampaignDetail() {
        return campaignDetail;
    }

    public void setCampaignDetail(String campaignDetail) {
        this.campaignDetail = campaignDetail;
    }
}
