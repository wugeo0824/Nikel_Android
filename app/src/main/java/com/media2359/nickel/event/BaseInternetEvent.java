package com.media2359.nickel.event;

/**
 * Created by Xijun on 23/5/16.
 */
public abstract class BaseInternetEvent {

    protected boolean isSuccess;
    protected String message;
    protected Throwable throwable;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
        if (throwable!=null)
            this.message = throwable.getLocalizedMessage();
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
