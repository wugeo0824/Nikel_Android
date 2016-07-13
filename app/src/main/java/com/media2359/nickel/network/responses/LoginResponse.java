package com.media2359.nickel.network.responses;

/**
 * Created by Xijun on 12/7/16.
 */
public class LoginResponse extends BaseResponse {

    long expiry;
    String expiry_formatted;
    String token;

    public long getExpiry() {
        return expiry;
    }

    public String getExpiry_formatted() {
        return expiry_formatted;
    }

    public String getToken() {
        return token;
    }
}
