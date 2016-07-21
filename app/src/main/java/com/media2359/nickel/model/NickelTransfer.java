package com.media2359.nickel.model;

import com.google.gson.annotations.SerializedName;
import com.media2359.nickel.utils.Const;

/**
 * Created by Xijun on 1/4/16.
 */
public class NickelTransfer {

    /**
     * The 5 stages of a transaction progress
     */
    public static final int TRANS_DRAFT = -1; // transaction is drafted, not confirmed
    public static final int TRANS_NEW_BORN = 0; // transaction is locked in, but payment has not made, so no receipt photo
    public static final int TRANS_PAYMENT_MADE = 1; // payment made, receipt photo taken but not uploaded
    public static final int TRANS_UPLOAD_COMPLETE = 2; // receipt photo uploaded
    public static final int TRANS_READY_COLLECTION = 3; // sms confirmation received, transaction complete

    public static final String STATUS_PENDING_PAYMENT = "pending payment";
    public static final String STATUS_PENDING_VERIFICATION = "pending verification";
    public static final String STATUS_PAYMENT_RECEIVED = "payment received";
    public static final String STATUS_FUNDS_READY = "funds ready for collection";



    @SerializedName("userId")
    String userId;
    @SerializedName("recipientId")
    String recipientId;

    @SerializedName("id")
    int transactionID;

    String transactionDate;

    @SerializedName("amtSent")
    String amountSent;
    @SerializedName("amtRecv")
    String amtRecv;

    @SerializedName("currencySent")
    String currencySent;
    @SerializedName("currencyRecv")
    String currencyRecv;

    @SerializedName("status")
    String transactionStatus;

    int transProgress = 999;

    @SerializedName("recpDisplayName")
    String recipientName;

    @SerializedName("recpNameFirstLast")
    String recipientFullName;

    @SerializedName("recpPhoneNum")
    String recpPhoneNum;

    @SerializedName("recpBankName")
    String recpBankName;

    @SerializedName("recpBankAccountNum")
    String recipientAccountNo;

    @SerializedName("rate")
    String exchangeRate;

    @SerializedName("fee")
    String fee;

    @SerializedName("receipt")
    String receiptPhotoUrl;

    @SerializedName("createdAt")
    String createdAt;

    String receiptFilePath;

    public NickelTransfer() {
    }

    public NickelTransfer(int transactionID, String transactionDate, String amountSent, String transactionStatus, int transProgress, String recipientName, String exchangeRate, String recipientAccountNo) {
        this.transactionID = transactionID;
        this.transactionDate = transactionDate;
        this.amountSent = amountSent;
        this.transactionStatus = transactionStatus;
        this.transProgress = transProgress;
        this.recipientName = recipientName;
        this.exchangeRate = exchangeRate;
        this.recipientAccountNo = recipientAccountNo;
    }

    public NickelTransfer(Builder builder) {
        this.transactionID = builder.transactionID;
        this.transactionDate = builder.transactionDate;
        this.amountSent = builder.transactionAmount;
        this.transactionStatus = builder.transactionStatus;
        this.transProgress = builder.transProgress;
        this.recipientName = builder.recipientName;
        this.exchangeRate = builder.exchangeRate;
        this.recipientAccountNo = builder.recipientAccountNo;
    }

    /**
     * called after user clicked the confirm transaction button, and api callback came successfully
     */
    public void transactionConfirmed() {
        setTransProgress(NickelTransfer.TRANS_NEW_BORN);
    }

    /**
     * called when user made payment and took the receipt photo
     */
    public void paymentMadeAndPhotoTaken(String receiptPhoto) {
        setReceiptFilePath(receiptPhoto);
        setTransProgress(NickelTransfer.TRANS_PAYMENT_MADE);
    }

    /**
     * called when receipt photo has been successfully uploaded
     */
    public void receiptUploaded() {
        setTransProgress(NickelTransfer.TRANS_UPLOAD_COMPLETE);
    }

    /**
     * called when sms confirmation came and funds are ready for collection
     */
    public void transactionReady() {
        setTransProgress(NickelTransfer.TRANS_READY_COLLECTION);
    }


    /**
     * As realmObject, changes must be done inside transaction
     *
     * @param transProgress
     */
    private void setTransProgress(final int transProgress) {
        this.transProgress = transProgress;
    }

    public void setReceiptPhotoUrl(final String receiptPhotoUrl) {
        this.receiptPhotoUrl = receiptPhotoUrl;
    }

    public String getReceiptPhotoUrl() {
        return receiptPhotoUrl;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public String getAmountSent() {
        return amountSent;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getRecipientAccountNo() {
        return recipientAccountNo;
    }

    public int getTransProgress() {
        if (transProgress > 100){
            String status = getTransactionStatus().toLowerCase();
            if (status.equals(STATUS_PENDING_PAYMENT))
                return TRANS_NEW_BORN;
            if (status.equals(STATUS_PAYMENT_RECEIVED))
                return TRANS_PAYMENT_MADE;
            if (status.equals(STATUS_PENDING_VERIFICATION))
                return TRANS_UPLOAD_COMPLETE;
            if (status.equals(STATUS_FUNDS_READY))
                return TRANS_READY_COLLECTION;
        }

        return transProgress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public int getInItemProgress() {
        if (getTransProgress() <= 1)
            return Const.IN_ITEM_PROGRESS_ONE;
        else if (getTransProgress() == TRANS_UPLOAD_COMPLETE)
            return Const.IN_ITEM_PROGRESS_TWO;
        else
            return Const.IN_ITEM_MAX_PROGRESS;
    }

    public String getUserId() {
        return userId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getAmtRecv() {
        return amtRecv;
    }

    public String getCurrencySent() {
        return currencySent;
    }

    public String getCurrencyRecv() {
        return currencyRecv;
    }

    public String getRecipientFullName() {
        return recipientFullName;
    }

    public String getRecpPhoneNum() {
        return recpPhoneNum;
    }

    public String getRecpBankName() {
        return recpBankName;
    }

    public String getFee() {
        return fee;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getReceiptFilePath() {
        return receiptFilePath;
    }

    public void setReceiptFilePath(String receiptFilePath) {
        this.receiptFilePath = receiptFilePath;
    }

    /**
     * A builder for the Profile class.
     */
    public static final class Builder {

        private int transactionID;
        private String transactionDate;
        private String transactionAmount;
        private String transactionStatus;
        private int transProgress;
        private String recipientName;
        private String exchangeRate;
        private String recipientAccountNo;

        public Builder() {
        }

        public Builder withID(int transactionID) {
            this.transactionID = transactionID;
            return this;
        }

        public Builder withDate(String transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public Builder withAmount(String transactionAmount) {
            this.transactionAmount = transactionAmount;
            return this;
        }

        public Builder withStatus(String transactionStatus) {
            this.transactionStatus = transactionStatus;
            return this;
        }

        public Builder withProgress(int progress) {
            this.transProgress = progress;
            return this;
        }

        public Builder withRecipientName(String recipientName) {
            this.recipientName = recipientName;
            return this;
        }

        public Builder withExchangeRate(String exchangeRate) {
            this.exchangeRate = exchangeRate;
            return this;
        }

        public Builder withRecipientAccount(String recipientAccount) {
            this.recipientAccountNo = recipientAccount;
            return this;
        }

        public NickelTransfer build() {
            return new NickelTransfer(this);
        }
    }
}
