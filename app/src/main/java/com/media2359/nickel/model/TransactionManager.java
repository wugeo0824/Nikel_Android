package com.media2359.nickel.model;

import android.content.Context;

import com.media2359.nickel.utils.TransactionHistoryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xijun on 5/4/16.
 */
public class TransactionManager {

    private static TransactionManager manager;
    private Transaction currentTransaction;
    private List<Transaction> transactionList;
    private List<Transaction> inProgressList;

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

    /**
     * called after user clicked the confirm transaction button, and api callback came successfully
     */
    public void transactionConfirmed(){
        currentTransaction.setTransProgress(Transaction.TRANS_NEW_BORN);
    }

    /**
     * called when user made payment and took the receipt photo
     */
    public void paymentMadeAndPhotoTaken(String receiptPhoto){
        currentTransaction.setReceiptPhotoUrl(receiptPhoto);
        currentTransaction.setTransProgress(Transaction.TRANS_PAYMENT_MADE);
    }

    /**
     * called when receipt photo has been successfully uploaded
     */
    public void receiptUploaded(){
        currentTransaction.setTransProgress(Transaction.TRANS_UPLOAD_COMPLETE);
    }

    /**
     * called when sms confirmation came and funds are ready for collection
     */
    public void transactionReady(){
        currentTransaction.setTransProgress(Transaction.TRANS_READY_COLLECTION);
    }
//    public void saveTransaction(Context context, Transaction transaction){
//        if (!transactionList.contains(transaction)){
//            transactionList.add(transaction);
//            TransactionHistoryUtils.saveTransaction(context, transaction);
//        }
//    }
//
//    public void deleteTransaction(Context context, Transaction transaction){
//        if (transactionList.contains(transaction)){
//            transactionList.remove(transaction);
//            TransactionHistoryUtils.deleteTransaction(context, transaction);
//        }
//    }
//
//    public List<Transaction> getAllUncompletedTransactions(Context context){
//        if (transactionList == null){
//            transactionList = new ArrayList<>();
//            transactionList.addAll(TransactionHistoryUtils.getAllTransactions(context));
//        }
//        return transactionList;
//    }
}
