package com.media2359.nickel.gallery;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Xijun on 27/7/16.
 */
public class GalleryAdapter extends FragmentStatePagerAdapter {

    int[] mResources;

    public GalleryAdapter(FragmentManager fm, int[] mResources) {
        super(fm);
        this.mResources = mResources;
    }

    @Override
    public Fragment getItem(int position) {
        return PhotoFragment.newInstance(position + 1, mResources[position]);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }
}
