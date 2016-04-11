package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import com.media2359.nickel.utils.TypefaceHelper;

/**
 * Created by Xijun on 8/4/16.
 */
public class CustomTextInputEditText extends TextInputEditText{

    public CustomTextInputEditText(Context context) {
        super(context);
        init(context);
    }

    public CustomTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(TypefaceHelper.get(context));
    }
}
