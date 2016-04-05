package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;

import com.media2359.nickel.R;

/**
 * Created by Xijun on 4/4/16.
 */
public class ThemedSwipeRefreshLayout extends SwipeRefreshLayout {

    private static final String TAG = "RefreshTag";
    private boolean selfCancelled = false;

    public ThemedSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public ThemedSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
//        int offsetFromTop = getResources().get(android.R.attr.actionBarSize);
//        setProgressViewEndTarget(false, offsetFromTop);
        setColorSchemeResources(R.color.cornFlowerBlue, R.color.orangePink, R.color.butterScotch, R.color.greenyBlue);
    }

    // TODO: remove this once the bug is fixed
    // There is a know bug in the SwipeRefreshLayout where if you switch fragments during a refresh then the layout freezes and you encounter overlap.
    // See: https://code.google.com/p/android/issues/detail?id=78062   and   https://code.google.com/p/android/issues/detail?id=170288
    @Override
    protected Parcelable onSaveInstanceState()
    {
        if(isRefreshing()) {
            clearAnimation();
            setRefreshing(false);
            selfCancelled = true;
            Log.d(TAG, "For hide refreshing");
        }
        return super.onSaveInstanceState();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        super.setRefreshing(refreshing);
        selfCancelled = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(hasWindowFocus && selfCancelled) {
            setRefreshing(true);
            Log.d(TAG, "Force show refreshing");
        }
    }
}
