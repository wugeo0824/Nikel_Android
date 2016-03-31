package com.media2359.nickel.event;

/**
 * Created by Xijun on 31/3/16.
 */
public class OnRecipientDeleteClickEvent {
    private int position;

    public OnRecipientDeleteClickEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
