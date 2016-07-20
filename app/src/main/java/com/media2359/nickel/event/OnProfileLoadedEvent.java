package com.media2359.nickel.event;

import com.media2359.nickel.model.MyProfile;

/**
 * Created by Xijun on 19/7/16.
 */
public class OnProfileLoadedEvent extends BaseInternetEvent {

    MyProfile profile;

    public OnProfileLoadedEvent(boolean isSuccess, String message, MyProfile profile) {
        this.profile = profile;
        setMessage(message);
        setSuccess(isSuccess);
    }

    public MyProfile getProfile() {
        return profile;
    }
}
