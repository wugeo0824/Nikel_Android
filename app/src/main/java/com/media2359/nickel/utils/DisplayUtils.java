package com.media2359.nickel.utils;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Xijun on 10/3/16.
 */
public class DisplayUtils {

    public static int getDisplayHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getRotation(Context context){
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = display.getRotation();


        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break; //Natural orientation
            case Surface.ROTATION_90: degrees = 90; break; //Landscape left
            case Surface.ROTATION_180: degrees = 180; break;//Upside down
            case Surface.ROTATION_270: degrees = 270; break;//Landscape right
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degrees) % 360;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degrees + 360) % 360;
        }

        return rotation;
    }

    public static void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

//        if (v instanceof EditText){
//            v.clearFocus();
//            ((EditText) v).setCursorVisible(false);
//        }
    }

}
