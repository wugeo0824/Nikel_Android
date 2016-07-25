package com.media2359.nickel.event;

import com.media2359.nickel.model.NickelTransfer;
import com.media2359.nickel.network.responses.BaseResponse;

/**
 * Created by Xijun on 20/7/16.
 */
public class OnReceiptUploadedEvent extends BaseInternetEvent {

    NickelTransfer nickelTransfer;

    public NickelTransfer getNickelTransfer() {
        return nickelTransfer;
    }

    public void setNickelTransfer(NickelTransfer nickelTransfer) {
        this.nickelTransfer = nickelTransfer;
    }
}
