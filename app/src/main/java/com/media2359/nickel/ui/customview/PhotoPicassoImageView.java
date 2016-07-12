package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.media2359.nickel.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Xijun on 31/5/16.
 */
public class PhotoPicassoImageView extends ImageView implements Target {

    public PhotoPicassoImageView(Context context) {
        super(context);
    }

    public PhotoPicassoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoPicassoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        setImageDrawable(drawable);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        setImageDrawable(placeHolderDrawable);
    }
}
