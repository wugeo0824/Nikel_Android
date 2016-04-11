package com.media2359.nickel.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.media2359.nickel.model.Transaction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xijun on 7/4/16.
 */
public class TransactionHistoryUtils {

    private static final String TAG = "TransactionHistoryUtils";

    private static final String PREF_FILE_NAME = "NickelTransactionHistory";
    private static final String PREF_SINGLE_TRANSACTION = "nickel_trans";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static List<Transaction> getAllTransactions(Context context) {
        List<Transaction> transactions = new ArrayList<>();
        List<String> tsKeys = new ArrayList<>();

        tsKeys.addAll(getSharedPreferences(context).getAll().keySet());

        for (String key : tsKeys) {
            String tsString = getSharedPreferences(context).getString(key, "");

            if (TextUtils.isEmpty(tsString))
                continue;

            Transaction transaction = (new Gson()).fromJson(tsString, Transaction.class);
            transactions.add(transaction);
        }

        return transactions;
    }

    public static void saveTransaction(Context context, Transaction transaction){
        if (transaction == null){
            Log.d(TAG, "saveTransaction: transaction is null");
        }
        // convert the content into string
        String tsString = (new Gson()).toJson(transaction);

        // generate the hash key for this transaction
        String tsKey = getTransactionKey(transaction);

        // save the string
        getSharedPreferences(context).edit().putString(tsKey, tsString).commit();
    }

    public static void deleteTransaction(Context context, Transaction transaction) {

        if (transaction == null)
            return;

        // generate the hash key for this transaction
        String hashKey = getTransactionKey(transaction);

        // delete the receipt photo
        File photo = new File(transaction.getReceiptPhotoUrl());
        boolean deleted = photo.delete();
        Log.d(TAG, "deleteTransaction: deleted" + deleted);

        // remove the transaction key
        getSharedPreferences(context).edit().remove(hashKey).commit();

    }

    public static String getTransactionKey(Transaction transaction){
        String key = transaction.getTransactionID() + transaction.getTransactionDate();
        return PREF_SINGLE_TRANSACTION + "_" + key;
    }
}
