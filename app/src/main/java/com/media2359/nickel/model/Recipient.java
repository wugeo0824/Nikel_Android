package com.media2359.nickel.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Xijun on 21/3/16.
 */

public class Recipient {

    @SerializedName("nameFirstLast")
    String fullName;

    @SerializedName("displayName")
    String displayName;

    @SerializedName("phoneNum")
    String phoneNumber;

    @SerializedName("address")
    String street;

    @SerializedName("city")
    String city;

    @SerializedName("postalCode")
    String postalCode;

    @SerializedName("bankName")
    String bankName;

    @SerializedName("bankAccountNum")
    String bankAccount;

    @SerializedName("bankShortName")
    String bankShortName;

    @SerializedName("bank")
    int bankId;

    @SerializedName("id")
    String recipientId;

    @SerializedName("latestTransfer")
    NickelTransfer currentTransaction;

    @SerializedName("status")
    String status;

    @Expose(serialize = false, deserialize = false)
    private boolean expanded = false;

    @Expose(serialize = false, deserialize = false)
    private boolean greyedOut = false;

    public Recipient() {
    }

    public Recipient(String fullName, String displayName, String phoneNumber, String street, String city, String postalCode, String bankName, int bankId, String bankAccount) {
        this.fullName = fullName;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.bankId = bankId;
    }

    public boolean isGreyedOut() {
        return greyedOut;
    }

    public void setGreyedOut(final boolean greyedOut) {
        this.greyedOut = greyedOut;
    }

    public void setCurrentTransaction(final NickelTransfer currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public String getFullName() {
        return fullName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getBankName() {
        return bankName;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getBankShortName() {
        return bankShortName;
    }

    public String getStatus() {
        return status;
    }

    public boolean isInProgress() {
        return (currentTransaction != null);
    }

    public NickelTransfer getCurrentTransaction() {
        return currentTransaction;
    }

}
