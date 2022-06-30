package com.example.espace_ads.models;

public class PaymentModel {
    String fullName;
    long cardNumber;
    double balance;
    String expireDate, cardType;

    public PaymentModel(String fullName, long cardNumber, double balance, String expireDate, String cardType) {
        this.fullName = fullName;
        this.cardNumber = cardNumber;
        this.balance = balance;
        this.expireDate = expireDate;
        this.cardType = cardType;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
