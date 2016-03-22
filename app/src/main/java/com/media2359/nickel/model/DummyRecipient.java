package com.media2359.nickel.model;

/**
 * Created by Xijun on 21/3/16.
 */
public class DummyRecipient {

    String name;
    String bank;
    boolean expanded = false;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public DummyRecipient(String name, String bank) {
        this.name = name;
        this.bank = bank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
