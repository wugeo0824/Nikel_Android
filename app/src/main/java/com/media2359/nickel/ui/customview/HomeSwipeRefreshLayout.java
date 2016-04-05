package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Xijun on 4/4/16.
 */
public class HomeSwipeRefreshLayout extends ThemedSwipeRefreshLayout {

    public HomeSwipeRefreshLayout(Context context) {
        super(context);
    }

    public HomeSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        return super.canChildScrollUp();
    }
}
