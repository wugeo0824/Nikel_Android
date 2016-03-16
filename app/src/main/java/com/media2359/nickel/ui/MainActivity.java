package com.media2359.nickel.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.media2359.nickel.R;

/**
 * This handles the transaction logic
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openWebView(View view){
        Intent i = new Intent(MainActivity.this,WebViewActivity.class);
        startActivity(i);
    }

    public void openCamera(View view){
        Intent i = new Intent(MainActivity.this,CaptureActivity.class);
        startActivity(i);
    }

    public void goToLogin(View view){
        Intent i = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(i);
    }

    public void goToProfile(View view){
        Intent i = new Intent(MainActivity.this,ProfileActivity.class);
        startActivity(i);
    }


}
