package com.media2359.nickel.event;

/**
 * Created by Xijun on 13/7/16.
 */
public class OnLoginEvent extends BaseInternetEvent{

    public OnLoginEvent(boolean isSuccess, String message) {
        setMessage(message);
        setSuccess(isSuccess);
    }
}
