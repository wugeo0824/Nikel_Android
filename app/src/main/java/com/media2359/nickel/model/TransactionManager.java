package com.media2359.nickel.model;

/**
 * Created by Xijun on 5/4/16.
 */
public class TransactionManager {

    private static TransactionManager manager;
    private Transaction currentTransaction;

    private TransactionManager() {
    }

    public static TransactionManager getManager() {
        if (manager == null){
            manager = new TransactionManager();
        }
        return manager;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }
}
