package com.media2359.nikel.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.media2359.nikel.R;

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
}
