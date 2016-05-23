package com.media2359.nickel.homeMVP;

import android.os.Handler;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.media2359.nickel.model.Recipient;
import com.media2359.nickel.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Xijun on 20/5/16.
 */
public class HomePresenter extends MvpBasePresenter<HomeView> {

    List<Recipient> recipientList = new ArrayList<>();
    double exchangeRate = 9670.90D;
    double fee = 7.00D;

    public void getRecipients(final boolean pullToRefresh) {

        Recipient a = new Recipient("Husband", "BRI 281973021894", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient b = new Recipient("Mother", "BRI 0123874123", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient c = new Recipient("Sister", "MYI 9012830912", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        Recipient d = new Recipient("Han", "SGW 0911298301", "92227744", "That street", "That city", "21314", "That bank", "SHF98098");
        a.setExpanded(false);
        b.setExpanded(false);
        c.setExpanded(false);
        d.setExpanded(false);

        Transaction aadd = new Transaction("1238u9ashjd", "March 2, 2016", "500.00", "Funds Ready for Collection", Transaction.TRANS_NEW_BORN, "Husband", 1235);
        a.setCurrentTransaction(aadd);

        recipientList.add(a);
        recipientList.add(b);
        recipientList.add(c);
        recipientList.add(d);
        recipientList.add(a);
        recipientList.add(b);
        recipientList.add(c);
        recipientList.add(d);

        //TODO, use api
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().setData(recipientList);
                getView().showContent();
            }
        },1000);
    }

    public void refreshRates(){
        Random random = new Random();
        exchangeRate += random.nextDouble() * 100D;
        fee += random.nextDouble();

        getView().updateRates(exchangeRate,fee);
    }

    public void makeNewTransaction(final Transaction transaction) {

        getView().showLoadingProgressDialog(true);

        // TODO: Call api

        // TODO: store the new transaction in local DB after succeed

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getView().showLoadingProgressDialog(false);
                getView().showTransactionDetailScreen(transaction);
            }
        },1000);


    }


}
