package com.media2359.nickel.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.media2359.nickel.R;

/**
 * Created by Xijun on 27/7/16.
 */
public class GuideActivity extends AppCompatActivity {

    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    int[] mResources = {
            R.drawable.uob1,
            R.drawable.uob2,
            R.drawable.uob3,
            R.drawable.uob4,
            R.drawable.uob5,
            R.drawable.uob6,
            R.drawable.uob7,
            R.drawable.uob8,
            R.drawable.uob9,
            R.drawable.uob10
    };


    public static void startGuideActivity(Activity activity) {
        Intent i = new Intent(activity, GuideActivity.class);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initViews();
    }

    private void initViews() {

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new GalleryAdapter(getSupportFragmentManager(), mResources);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);

    }


}
