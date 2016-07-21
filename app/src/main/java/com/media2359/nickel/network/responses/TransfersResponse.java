package com.media2359.nickel.network.responses;

import com.google.gson.annotations.SerializedName;
import com.media2359.nickel.model.NickelTransfer;

import java.util.List;

/**
 * Created by Xijun on 20/7/16.
 */
public class TransfersResponse extends BaseResponse {

    int totalPages;

    @SerializedName("transfers")
    List<NickelTransfer> transfers;

    public int getTotalPages() {
        return totalPages;
    }

    public List<NickelTransfer> getTransfers() {
        return transfers;
    }
}
