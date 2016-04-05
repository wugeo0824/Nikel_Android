package com.media2359.nickel.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.media2359.nickel.model.MyProfile;

/**
 * Created by Xijun on 14/3/16.
 */
public class PreferrencesUtils {

    private static final String PREF_FILE_NAME = "NickelPrefs";
    private static final String PREF_MY_PROFILE = "my_profile";

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static String getIDPhotoFront(Context context) {
        return getSharedPreferences(context).getString(Constants.ID_PHOTO_FRONT, "");
    }

    public static String getIDPhotoBack(Context context) {
        return getSharedPreferences(context).getString(Constants.ID_PHOTO_BACK, "");
    }

    public static void saveIDFront(Context context, String frontURL) {
        getSharedPreferences(context).edit().putString(Constants.ID_PHOTO_FRONT,frontURL).apply();
    }

    public static void saveIDBack(Context context, String backURL) {
        getSharedPreferences(context).edit().putString(Constants.ID_PHOTO_BACK,backURL).apply();
    }

    public static void saveProfile(Context context, MyProfile myProfile){
        SharedPreferences preferences = getSharedPreferences(context);
        preferences.edit().putString(PREF_MY_PROFILE, new Gson().toJson(myProfile)).apply();
    }

    public static MyProfile getProfile(Context context){
        SharedPreferences preferences = getSharedPreferences(context);
        return new Gson().fromJson(preferences.getString(PREF_MY_PROFILE, null), MyProfile.class);
    }
}
