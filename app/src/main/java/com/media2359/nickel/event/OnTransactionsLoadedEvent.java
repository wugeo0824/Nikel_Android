package com.media2359.nickel.event;

import com.media2359.nickel.model.NickelTransfer;

import java.util.HashSet;

/**
 * Created by Xijun on 24/5/16.
 */
public class OnTransactionsLoadedEvent extends BaseInternetEvent {

    HashSet<NickelTransfer> transactions;

    public OnTransactionsLoadedEvent(HashSet<NickelTransfer> transactions) {
        this.transactions = transactions;
    }

    public HashSet<NickelTransfer> getTransactions() {
        return transactions;
    }
}
