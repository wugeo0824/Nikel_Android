package com.media2359.nickel.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Xijun on 5/4/16.
 */
public class BaseActivity extends AppCompatActivity {

    private static final long TIME_OUT_THRESHOLD = 30 * 60 * 1000; // 30 minutes in milliseconds
    private long lastActiveTime = -1;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        lastActiveTime = new Date().getTime();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (lastActiveTime > 0 && timeOut()){
            logOutUser();
        }
    }

    private void logOutUser() {
        lastActiveTime = -1;

        Toast.makeText(getApplicationContext(), "Sorry, user session timed out. Please login again", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private boolean timeOut() {
        if (lastActiveTime <0)
            return false;

        long currentTime = new Date().getTime();
        long difference = Math.abs(lastActiveTime - currentTime);
        if (difference > TIME_OUT_THRESHOLD){
            return true;
        }else
            return false;
    }
}
