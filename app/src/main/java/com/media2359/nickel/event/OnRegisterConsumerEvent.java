package com.media2359.nickel.event;

/**
 * Created by Xijun on 12/7/16.
 */
public class OnRegisterConsumerEvent extends BaseInternetEvent {

    public OnRegisterConsumerEvent(boolean isSuccess, String message) {
        setMessage(message);
        setSuccess(isSuccess);
    }
}
