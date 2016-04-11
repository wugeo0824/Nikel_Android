package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.media2359.nickel.utils.TypefaceHelper;

import java.util.Hashtable;

/**
 * Created by Xijun on 8/4/16.
 */
public class CustomTextView extends TextView {


    public CustomTextView(Context context) {
        super(context);
        init(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(TypefaceHelper.get(context));
    }

}
