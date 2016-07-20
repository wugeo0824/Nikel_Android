package com.media2359.nickel.event;

/**
 * Created by Xijun on 23/5/16.
 */
public class OnProfileChangedEvent {

    public boolean isSuccess;
    public String message;

    public OnProfileChangedEvent(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
