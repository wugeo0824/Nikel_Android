package com.media2359.nickel.history;

import android.os.Handler;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.media2359.nickel.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xijun on 25/4/16.
 */
public class HistoryPresenter extends MvpBasePresenter<HistoryView> {

    List<Transaction> historyList = new ArrayList<>();

    public void loadHistory(final boolean pullToRefresh) {

        //getView().showLoading(pullToRefresh);
        historyList.clear();
        Transaction a = new Transaction("1238u9ashjd", "March 2, 2016", "500", "Funds Ready for Collection", Transaction.TRANS_READY_COLLECTION, "Husband", 1235);
        Transaction b = new Transaction("2238u9ashjd", "March 12, 2016", "100", "Please DO xxxxxx", Transaction.TRANS_UPLOAD_COMPLETE, "Husband", 1235);
        Transaction c = new Transaction("3238u9ashjd", "April 2, 2016", "540", "Please DO yyyyy", Transaction.TRANS_PAYMENT_MADE, "Husband", 2354);
        Transaction d = new Transaction("4238u9ashjd", "May 2, 2016", "1300", "Please DO zzzzz", Transaction.TRANS_NEW_BORN, "Husband", 8657);
        historyList.add(a);
        historyList.add(b);
        historyList.add(c);
        historyList.add(d);
        historyList.add(a);
        historyList.add(b);
        historyList.add(c);
        historyList.add(d);

        //TODO, use api
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().setData(historyList);
                getView().showContent();
            }
        },1000);

    }
}
