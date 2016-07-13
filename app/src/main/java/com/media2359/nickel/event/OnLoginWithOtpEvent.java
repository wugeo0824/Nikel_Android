package com.media2359.nickel.event;

/**
 * Created by Xijun on 12/7/16.
 */
public class OnLoginWithOtpEvent extends BaseInternetEvent {

    public OnLoginWithOtpEvent(boolean isSuccess, String message) {
        setMessage(message);
        setSuccess(isSuccess);
    }
}
