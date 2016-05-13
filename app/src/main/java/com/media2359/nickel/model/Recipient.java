package com.media2359.nickel.model;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Xijun on 21/3/16.
 */

@Parcel(Parcel.Serialization.BEAN)
public class Recipient extends RealmObject{

    String name;
    String nickName;
    String phoneNumber;
    String street;
    String city;
    String postalCode;
    String bankName;
    String bankAccount;
    boolean expanded = false;
    boolean greyedOut = false;

    Transaction currentTransaction;

    public Recipient() {
    }

    @ParcelConstructor
    public Recipient(String name, String nickName, String phoneNumber, String street, String city, String postalCode, String bankName, String bankAccount) {
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
    }

    public boolean isGreyedOut() {
        return greyedOut;
    }

    public void setGreyedOut(boolean greyedOut) {
        this.greyedOut = greyedOut;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getNickName() {
        return nickName;
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

    public boolean isInProgress() {
        return (currentTransaction != null);
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }
}
