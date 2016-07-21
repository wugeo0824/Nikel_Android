package com.media2359.nickel.network.responses;

/**
 * Created by Xijun on 20/7/16.
 */
public class ComputeResponse extends BaseResponse {

    String amtRecv;
    int amtPayable;
    String bankName;
    String bankAccountNum;
    double rate;


    public String getAmtRecv() {
        return amtRecv;
    }

    public int getAmtPayable() {
        return amtPayable;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public double getRate() {
        return rate;
    }
}

