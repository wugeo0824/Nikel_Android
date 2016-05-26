package com.media2359.nickel.homeMVP;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;
import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.model.Recipient;

import java.util.List;

/**
 * Created by Xijun on 20/5/16.
 */
public interface HomeView extends MvpLceView<List<Recipient>> {

    boolean validTransaction();

    void showMyProfileScreen();

    void showEditMyProfileButton();

    void showNewRecipientScreen();

    void showAddNewRecipientButton();

    void showRecipientDetailScreen(Recipient recipient);

    void updateRates(double exchangeRate, double fee);

    void recipientDeleted();

    void showTransactionDetailScreen(NickelTransfer transaction);

    void shoePaymentConfirmationAlertDialog(String message);

    void showLoadingProgressDialog(boolean show);

}
