package com.media2359.nickel.history;

import android.os.Handler;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.media2359.nickel.model.NickelTransfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;

/**
 * Created by Xijun on 25/4/16.
 */
public class HistoryPresenter extends MvpBasePresenter<HistoryView> {

    List<NickelTransfer> allTransactions = new ArrayList<>();

    public void loadHistory(final boolean pullToRefresh) {
        getView().showLoading(pullToRefresh);
        loadTransactionsFromServer();
    }

    private void notifyLoadingComplete() {
        if (allTransactions == null || allTransactions.isEmpty()) {
            getView().showEmptyView();
        } else {
            getView().setData(allTransactions);
            getView().showContent();
        }
    }



    public void loadTransactionsFromServer() {
        // TODO call api
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                allTransactions.addAll(mockData());
                notifyLoadingComplete();
            }
        }, 500);
    }

    private List<NickelTransfer> mockData() {

        final List<NickelTransfer> transactionList = new ArrayList<>();
        NickelTransfer a = new NickelTransfer("1238u9ashjd", "March 2, 2016", "500", "Funds Ready for Collection", NickelTransfer.TRANS_READY_COLLECTION, "Husband", 7235, "BIN 812312313");
        NickelTransfer b = new NickelTransfer("2238u9ashjd", "March 12, 2016", "100", "Please DO xxxxxx", NickelTransfer.TRANS_UPLOAD_COMPLETE, "Mother", 6235, "BIN 812312313");
        NickelTransfer c = new NickelTransfer("3238u9ashjd", "April 2, 2016", "540", "Please DO yyyyy", NickelTransfer.TRANS_PAYMENT_MADE, "Father", 4354, "BIN 812312313");
        NickelTransfer d = new NickelTransfer("4238u9ashjd", "May 2, 2016", "1300", "Please DO zzzzz", NickelTransfer.TRANS_NEW_BORN, "Wife", 8657, "BIN 812312313");
        transactionList.add(a);
        transactionList.add(b);
        transactionList.add(c);
        transactionList.add(d);
        transactionList.add(a);
        transactionList.add(b);
        transactionList.add(c);
        transactionList.add(d);

        return transactionList;
    }

}
