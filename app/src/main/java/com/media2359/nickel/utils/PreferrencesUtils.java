package com.media2359.nickel.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Xijun on 14/3/16.
 */
public class PreferrencesUtils {

    private static final String PREF_FILE_NAME = "NickelPrefs";

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
}
