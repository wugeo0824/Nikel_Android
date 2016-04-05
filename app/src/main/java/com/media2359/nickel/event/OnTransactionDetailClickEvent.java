package com.media2359.nickel.event;

/**
 * Created by Xijun on 4/4/16.
 */
public class OnTransactionDetailClickEvent {
    private int position;

    public OnTransactionDetailClickEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
