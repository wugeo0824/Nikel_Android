package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

import com.media2359.nickel.utils.TypefaceHelper;

/**
 * Created by Xijun on 8/4/16.
 */
public class CustomTextInputLayout extends TextInputLayout {

    public CustomTextInputLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(TypefaceHelper.get(context));
    }
}
