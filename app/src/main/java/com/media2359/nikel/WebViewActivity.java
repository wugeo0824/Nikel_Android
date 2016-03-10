package com.media2359.nikel;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Xijun on 10/3/16.
 */
public class WebViewActivity extends AppCompatActivity {

    private final static String EXTRA_WEB_URL = "web_url";
    private final static String EXTRA_WEB_TITLE = "web_title";

    private static final String webUrl = "http://www.uob.com.sg/uob-branches-and-atms/assets/uob-locator-new.html";
    private static final String webTitle = "Cash Deposit Machines";
    private WebView webView;
    private ImageButton btnCancel;
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        //loadData();
        initViews();
    }

    private void initViews() {

        btnCancel = (ImageButton) findViewById(R.id.button_web_cancel);
        btnCancel.setOnClickListener(cancelButtonClickListener);
        tvTitle = (TextView) findViewById(R.id.tv_web_title);
        tvTitle.setText(webTitle);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new mWebViewClient());
        webView.loadUrl(webUrl);
    }

    private View.OnClickListener cancelButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WebViewActivity.this.finish();
            //WebViewActivity.this.overridePendingTransition(R.anim.anim_fade_in,R.anim.anim_slide_out_from_bottom);
        }
    };

    private class mWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //load all web links inside the webview
            return false;
        }

        /**
         * Use this to customize the page,
         * goal is to show only the map with points of Cash Deposits
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


            // perform click on the Branch checkbox, to hide the branches
            view.loadUrl("javascript:document.getElementsByName(\"Branch\")[0].getElementsByClassName(\"checkbox\")[0].click();");
            // perform click on the Cash Deposit Machine (CDM) checkbox, to show the CDMs
            view.loadUrl("javascript:document.getElementsByName(\"CDM\")[0].getElementsByClassName(\"checkbox\")[0].click();");
            // hide the top bar
            view.loadUrl("javascript:document.getElementById(\"menu\").setAttribute(\"style\",\"display:none;\");");
            // hide the side pane
            view.loadUrl("javascript:document.getElementsByClassName(\"sidePane\")[0].setAttribute(\"style\",\"display:none;\");");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

}
