package com.media2359.nickel.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Xijun on 12/7/16.
 */
public class BaseResponse {

    private boolean isSuccess;

    @SerializedName("error")
    private String error;

    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
