package com.media2359.nickel.model;

import com.media2359.nickel.utils.Const;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

/**
 * Created by Xijun on 1/4/16.
 */

public class NickelTransfer extends RealmObject {

    /**
     * The 5 stages of a transaction progress
     */
    public static final int TRANS_DRAFT = -1; // transaction is drafted, not confirmed
    public static final int TRANS_NEW_BORN = 0; // transaction is locked in, but payment has not made, so no receipt photo
    public static final int TRANS_PAYMENT_MADE = 1; // payment made, receipt photo taken but not uploaded
    public static final int TRANS_UPLOAD_COMPLETE = 2; // receipt photo uploaded
    public static final int TRANS_READY_COLLECTION = 3; // sms confirmation received, transaction complete

    @PrimaryKey
    String transactionID;

    String transactionDate;
    String transactionAmount;
    String transactionStatus;
    int transProgress;
    String recipientName;
    String recipientAccountNo;
    double exchangeRate;
    String receiptPhotoUrl;

    public NickelTransfer() {
    }

    public NickelTransfer(String transactionID, String transactionDate, String transactionAmount, String transactionStatus, int transProgress, String recipientName, double exchangeRate, String recipientAccountNo) {
        this.transactionID = transactionID;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.transactionStatus = transactionStatus;
        this.transProgress = transProgress;
        this.recipientName = recipientName;
        this.exchangeRate = exchangeRate;
        this.recipientAccountNo = recipientAccountNo;
    }

    public NickelTransfer(Builder builder) {
        this.transactionID = builder.transactionID;
        this.transactionDate = builder.transactionDate;
        this.transactionAmount = builder.transactionAmount;
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
        setReceiptPhotoUrl(receiptPhoto);
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
     * @param transProgress
     */
    private void setTransProgress(final int transProgress) {
        final NickelTransfer object = this;

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                object.transProgress = transProgress;
            }
        });
    }

    public void setReceiptPhotoUrl(final String receiptPhotoUrl) {
        final NickelTransfer object = this;

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                object.receiptPhotoUrl = receiptPhotoUrl;
            }
        });
    }

    public String getReceiptPhotoUrl() {
        return receiptPhotoUrl;
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

    public String getRecipientAccountNo() {
        return recipientAccountNo;
    }

    public int getTransProgress() {
        return transProgress;
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
     * A builder for the Profile class.
     */
    public static final class Builder {

        private String transactionID;
        private String transactionDate;
        private String transactionAmount;
        private String transactionStatus;
        private int transProgress;
        private String recipientName;
        private double exchangeRate;
        private String recipientAccountNo;

        public Builder() {
        }

        public Builder withID(String transactionID) {
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

        public Builder withExchangeRate(double exchangeRate) {
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
