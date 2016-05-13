package com.media2359.nickel.model;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.media2359.nickel.utils.Const;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Xijun on 1/4/16.
 */

@Parcel(Parcel.Serialization.BEAN)
public class Transaction extends RealmObject{

    /**
     * The 5 stages of a transaction progress
     */
    public static final int TRANS_DRAFT = -1; // transaction is drafted, not confirmed
    public static final int TRANS_NEW_BORN = 0; // transaction is locked in, but payment has not made, so no receipt photo
    public static final int TRANS_PAYMENT_MADE = 1; // payment made, receipt photo taken but not uploaded
    public static final int TRANS_UPLOAD_COMPLETE = 2; // receipt photo uploaded
    public static final int TRANS_READY_COLLECTION = 3; // sms confirmation received, transaction complete

    @Required
    String transactionID;
    @Required
    String transactionDate;
    @Required
    String transactionAmount;
    String transactionStatus;
    int transProgress;
    String recipientName;
    double exchangeRate;
    String receiptPhotoUrl;

    public Transaction() {
    }

    @ParcelConstructor
    public Transaction(String transactionID, String transactionDate, String transactionAmount, String transactionStatus, int transProgress, String recipientName, double exchangeRate) {
        this.transactionID = transactionID;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionStatus = transactionStatus;
        this.transProgress = transProgress;
        this.recipientName = recipientName;
        this.exchangeRate = exchangeRate;
    }

    public Transaction(Builder builder){
        this.transactionID = builder.transactionID;
        this.transactionDate = builder.transactionDate;
        this.transactionAmount = builder.transactionAmount;
        this.transactionStatus = builder.transactionStatus;
        this.transProgress = builder.transProgress;
        this.recipientName = builder.recipientName;
        this.exchangeRate = builder.exchangeRate;
    }

    public String getReceiptPhotoUrl() {
        return receiptPhotoUrl;
    }

    public void setReceiptPhotoUrl(String receiptPhotoUrl) {
        this.receiptPhotoUrl = receiptPhotoUrl;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public int getTransProgress() {
        return transProgress;
    }

    public void setTransProgress(int transProgress) {
        this.transProgress = transProgress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public int getInItemProgress() {
        if (transProgress <= 1)
            return Const.IN_ITEM_PROGRESS_ONE;
        else if (transProgress == 2)
            return Const.IN_ITEM_PROGRESS_TWO;
        else
            return Const.IN_ITEM_MAX_PROGRESS;
    }

    /**
     * A builder for the Profile class. Follows a fluent interface.
     */
    public static final class Builder {

        private String transactionID;
        private String transactionDate;
        private String transactionAmount;
        private String transactionStatus;
        private int transProgress;
        private String recipientName;
        private double exchangeRate;

        public Builder() {
        }

        public Builder withID(String transactionID){
            this.transactionID = transactionID;
            return this;
        }

        public Builder withDate(String transactionDate){
            this.transactionDate = transactionDate;
            return this;
        }

        public Builder withAmount(String transactionAmount){
            this.transactionAmount = transactionAmount;
            return this;
        }

        public Builder withStatus(String transactionStatus){
            this.transactionStatus = transactionStatus;
            return this;
        }

        public Builder withProgress(int progress){
            this.transProgress = transProgress;
            return this;
        }

        public Builder withRecipientName(String recipientName){
            this.recipientName = recipientName;
            return this;
        }

        public Builder withExchangeRate(double exchangeRate){
            this.exchangeRate = exchangeRate;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
