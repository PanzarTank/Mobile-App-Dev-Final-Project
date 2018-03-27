package com.example.shelterconnect.model;

import java.util.Date;

/**
 * Donation object to represent model
 * Created by daniel on 2/12/18.
 */
public class Donation {

    private int donationID;

    private String creditCardNum;
    private Date expDate;
    private int ccv;
    private int donorID;
    private int requestID;
    private double amountDonated;

    public Donation(int donationID, String creditCardNum, Date expDate, int ccv, int donorID, int requestID, double amountDonated) {
        if (donationID < 0 || creditCardNum.length() != 16 || creditCardNum == null || expDate == null || ccv < 0 ||
                donorID < 0 || requestID < 0 || amountDonated < 0 ) {
            throw new IllegalArgumentException("Invalid input. Try again");
        }

        this.donationID = donationID;
        this.creditCardNum = creditCardNum;
        this.expDate = expDate;
        this.ccv = ccv;
        this.donorID = donorID;
        this.requestID = requestID;
        this.amountDonated = amountDonated;
    }

    public int getDonationID() {
        return donationID;
    }

    public void setDonationID(int donationID) {
        this.donationID = donationID;
    }

    public String getCreditCardNum() {
        return creditCardNum;
    }

    public void setCreditCardNum(String creditCardNum) {
        this.creditCardNum = creditCardNum;
    }

    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public int getCcv() {
        return ccv;
    }

    public void setCcv(int ccv) {
        this.ccv = ccv;
    }

    public int getDonorID() {
        return donorID;
    }

    public void setDonorID(int donorID) {
        this.donorID = donorID;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public double getAmountDonated() {
        return amountDonated;
    }

    public void setAmountDonated(double amountDonated) {
        this.amountDonated = amountDonated;
    }
}
