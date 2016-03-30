package com.media2359.nickel.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.media2359.nickel.R;

/**
 * Created by Xijun on 28/3/16.
 */
public class BarcodeUtils {

    public static BitMatrix getCode128(@NonNull String contents, int width, int height){
        Code128Writer writer = new Code128Writer();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, BarcodeFormat.CODE_128,width,height);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static final int BLACK = Color.BLACK;
    private static final int WHITE = Color.WHITE;

    public static Bitmap encodeAsBitmap(@NonNull String contents, int width, int height){
        BitMatrix result = getCode128(contents,width,height);
        if (result !=null){
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        }

        return null;
    }

    public static Bitmap encodeAsBitmapWithDisplayWidth(Context context, @NonNull String contents){
        int displayWidth = DisplayUtils.getDisplayWidth(context);
        int barcodeHeight = displayWidth / 3; // TODO change the height

        return encodeAsBitmap(contents,displayWidth,barcodeHeight);
    }

}
