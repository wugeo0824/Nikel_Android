package com.media2359.nickel.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Xijun on 21/3/16.
 */

public class Recipient extends RealmObject {

    @PrimaryKey
    private int ID;

    private String name;
    private String nickName;
    private String phoneNumber;
    private String street;
    private String city;
    private String postalCode;
    private String bankName;
    private String bankAccount;

    @Ignore
    private boolean expanded = false;
    @Ignore
    private boolean greyedOut = false;

    private NickelTransfer currentTransaction;

    public Recipient() {
    }

    public Recipient(int ID, String name, String nickName, String phoneNumber, String street, String city, String postalCode, String bankName, String bankAccount) {
        this.ID = ID;
        this.name = name;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.city = city;
        this.postalCode = postalCode;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
    }

    public int getID() {
        return ID;
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

    public String getName() {
        return name;
    }

    public String getBankAccount() {
        return bankAccount;
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

    public NickelTransfer getCurrentTransaction() {
        return currentTransaction;
    }

}
