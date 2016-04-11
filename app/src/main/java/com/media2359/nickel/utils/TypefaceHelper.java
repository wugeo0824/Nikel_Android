package com.media2359.nickel.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

/**
 * Create and keep custom typeface
 */
public class TypefaceHelper {

    private static final String TAG = "TypefaceHelper";

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface get(Context c) {
        String fileName = "fonts/HelveticaNeueLTStd_Lt.otf";

        synchronized (cache) {
            if (!cache.containsKey(fileName)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(),
                            fileName);
                    cache.put(fileName, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + fileName
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(fileName);
        }
    }
}
