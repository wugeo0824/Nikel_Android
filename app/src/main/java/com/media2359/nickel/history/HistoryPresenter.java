package com.media2359.nickel.history;

import android.os.Handler;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.media2359.nickel.model.NickelTransfer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Xijun on 25/4/16.
 */
public class HistoryPresenter extends MvpBasePresenter<HistoryView> {

    List<NickelTransfer> allTransactions = new ArrayList<>();
//    private boolean isDiskReady = false;
//    private boolean isServerReady = false;
//    private boolean isApiSuccess = false;

//    private Realm realm = Realm.getDefaultInstance();

    public void loadHistory(final boolean pullToRefresh) {
        getView().showLoading(pullToRefresh);

//        //reset the booleans. no need to reset the disk flag, since we only want to load all of them once per app launch
//        if (pullToRefresh){
//            isServerReady = false;
//        }

        loadTransactionsFromServer();
//        loadTransactionsFromDisk();
    }

    @Override
    public void attachView(HistoryView view) {
        super.attachView(view);
        //EventBus.getDefault().register(this);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        //EventBus.getDefault().unregister(this);
        //allTransactions.removeChangeListener(this);
        //allTransactions = null;
        //realm.close();
    }

//    @Subscribe
//    public void onEvent(OnTransactionsLoadedEvent event) {
//
//        // if internet loading failed, show error message, but we still have disk transactions to display
//        if (!event.isSuccess()){
//            getView().showError(event.getThrowable(), true);
//        }
//
//        //getView().setData(event.getTransactions());
//        getView().showContent();
//    }

    private void notifyLoadingComplete() {
//        if (!isServerReady || !isDiskReady)
//            return;

        if (allTransactions == null || allTransactions.isEmpty()) {
            getView().showEmptyView();
        } else {
            getView().setData(allTransactions);
            getView().showContent();
        }
    }


//    public void loadTransactionsFromDisk() {
//        if (isDiskReady)
//            return;
//
//        RealmQuery<Transaction> query = realm.where(Transaction.class);
//        allTransactions = query.findAll();
//        isDiskReady = true;
//        notifyLoadingComplete();
//        allTransactions.addChangeListener(this);
//    }


    public void loadTransactionsFromServer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                allTransactions.addAll(mockData());
                //addDataToRealm(mockData());
//                isServerReady = true;
//                isApiSuccess = false;
                notifyLoadingComplete();
                //syncDiskAndServerContent();
            }
        }, 500);
    }

//    private void addDataToRealm(final List<Transaction> data) {
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                realm.copyToRealmOrUpdate(data);
//            }
//        });
//    }

//    public void syncDiskAndServerContent() {
//
//        if (!isDiskReady || !isServerReady)
//            return;
//
//        notifyLoadingComplete();
//    }

    private List<NickelTransfer> mockData() {

        List<NickelTransfer> transactionList = new ArrayList<>();
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
