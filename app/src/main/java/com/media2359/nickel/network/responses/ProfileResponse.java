package com.media2359.nickel.network.responses;

import com.media2359.nickel.model.MyProfile;

/**
 * Created by Xijun on 13/7/16.
 */
public class ProfileResponse extends BaseResponse {

    private MyProfile profile;

    public ProfileResponse(MyProfile profile) {
        this.profile = profile;
    }

    public MyProfile getProfile() {
        return profile;
    }
}
