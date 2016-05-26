package com.media2359.nickel.event;

/**
 * Created by Xijun on 23/5/16.
 */
public class OnRecipientsChangedEvent extends BaseInternetEvent{

    public OnRecipientsChangedEvent(boolean isSuccess, String message) {
        setMessage(message);
        setSuccess(isSuccess);
    }
}
