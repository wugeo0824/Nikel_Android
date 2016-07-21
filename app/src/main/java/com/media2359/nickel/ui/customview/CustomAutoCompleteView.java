package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.media2359.nickel.utils.TypefaceHelper;

/**
 * Created by Xijun on 21/7/16.
 */
public class CustomAutoCompleteView extends AutoCompleteTextView {

    public CustomAutoCompleteView(Context context) {
        super(context);
        init();
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(TypefaceHelper.get(getContext()));
    }
}
