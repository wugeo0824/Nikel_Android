package com.media2359.nickel.event;

/**
 * Created by Xijun on 13/7/16.
 */
public class OnResetPasswordEvent extends BaseInternetEvent {

    public OnResetPasswordEvent(boolean isSuccess, String message) {
        setSuccess(isSuccess);
        setMessage(message);
    }
}
