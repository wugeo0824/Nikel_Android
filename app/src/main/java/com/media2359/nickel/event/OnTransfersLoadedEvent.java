package com.media2359.nickel.event;

import com.media2359.nickel.model.NickelTransfer;

import java.util.List;

/**
 * Created by Xijun on 20/7/16.
 */
public class OnTransfersLoadedEvent extends BaseInternetEvent {

    List<NickelTransfer> transfers;

    public OnTransfersLoadedEvent(boolean isSuccess, String message, List<NickelTransfer> transfers) {
        this.transfers = transfers;
        setSuccess(isSuccess);
        setMessage(message);
    }

    public List<NickelTransfer> getTransfers() {
        return transfers;
    }
}
