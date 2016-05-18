package com.media2359.nickel.ui.customview;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.TextView;

import com.media2359.nickel.utils.TypefaceHelper;

/**
 * Created by Xijun on 17/5/16.
 */
public class CustomNavigationView extends NavigationView {

    public CustomNavigationView(Context context) {
        super(context);
        init();
    }

    public CustomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        for (int i=0; i<getMenu().size();i++){
//            if (getMenu().getItem(i) instanceof TextView){
//                //((TextView) getMenu().getItem(i)).setTypeface(TypefaceHelper.get(getContext()));
//            }
//        }
    }
}
