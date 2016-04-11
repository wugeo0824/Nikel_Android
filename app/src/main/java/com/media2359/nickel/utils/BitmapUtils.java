package com.media2359.nickel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.media2359.nickel.R;

import java.io.File;

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

    public static Bitmap getThumbnail(Context context, Uri uri){
        File imageFile = new File(uri.getPath());
        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageFile.getPath()), context.getResources().getDimensionPixelSize(R.dimen.card_holder_width), context.getResources().getDimensionPixelSize(R.dimen.card_holder_height));
        return thumbImage;
    }

    public static Bitmap getThumbnail(Context context, File imageFile){
        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageFile.getPath()), context.getResources().getDimensionPixelSize(R.dimen.card_holder_width), context.getResources().getDimensionPixelSize(R.dimen.card_holder_height));
        return thumbImage;
    }

}
