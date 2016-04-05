package com.media2359.nickel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

/**
 * Created by Xijun on 1/4/16.
 */
public class BitmapUtils {

    public static Bitmap cropCenter(Bitmap input) {

        // crop Image
        Bitmap croppedImage;

        if (input.getHeight() >= input.getWidth()){
            croppedImage =  Bitmap.createBitmap(input, 0, input.getHeight() / 3, // we only need the middle third of the image
                    input.getWidth(), input.getHeight() / 3);
        }else{

            croppedImage =  Bitmap.createBitmap(input, input.getWidth() / 3, 0, // we only need the middle third of the image
                    input.getWidth()/3, input.getHeight());
        }

        return croppedImage;


    }

    public static Bitmap rotateImage(Bitmap input, int rotation) {
        // rotate Image
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.postRotate(rotation);
        Bitmap rotatedBitmap = Bitmap.createBitmap(input, 0,
                0, input.getWidth(), input.getHeight(),
                rotateMatrix, false);
        return rotatedBitmap;
    }

    public static int getRotation(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        //ROTATE PHOTO!

        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 90;
                break; //Natural orientation
//            case Surface.ROTATION_90:
//                degrees = 270;
//                break; //Landscape left
//            case Surface.ROTATION_180:
//                degrees = 0;
//                break;//Upside down
            case Surface.ROTATION_270:
                degrees = 180;
                break;//Landscape right
            default:
                degrees = 0;
                break;
        }

//        if (isFacingFront) {
//            // frontFacing
//            rotation = (info.orientation + degrees) % 330;
//            rotation = (360 - rotation) % 360;
//        } else {
//            // Back-facing
//            rotation = (info.orientation - degrees + 360) % 360;
//        }

//        if (degrees == Surface.ROTATION_0) {
//            rotation = 90;
//        }
//
//        if (degrees == Surface.ROTATION_270) {
//            rotation = 180;
//        }

        return degrees;
    }
}
