package com.media2359.nickel.network.responses;

import com.google.gson.annotations.SerializedName;
import com.media2359.nickel.model.Bank;

import java.util.List;

/**
 * Created by Xijun on 21/7/16.
 */
public class BanksResponse extends BaseResponse {

    @SerializedName("banks")
    List<Bank> bankList;

    public List<Bank> getBankList() {
        return bankList;
    }
}
