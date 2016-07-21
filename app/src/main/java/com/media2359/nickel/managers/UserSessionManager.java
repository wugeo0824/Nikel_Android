package com.media2359.nickel.managers;

import android.content.Context;

import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.utils.PreferencesUtils;

/**
 * Created by Xijun on 25/5/16.
 */
public class UserSessionManager {

    private static UserSessionManager instance;

    private String token;

    private UserSessionManager() {

    }

    public static UserSessionManager getInstance() {

        if (instance == null)
            instance = new UserSessionManager();

        return instance;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void signOut(Context context) {
        // clear user cache and sign out
        token = "";
        MyProfile.clearProfile(context);
    }
}
