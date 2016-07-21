package com.media2359.nickel;

import android.app.Application;
import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 * Created by Xijun on 10/3/16.
 */
public class NickelApplication extends Application {

    //private RefWatcher refWatcher;
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = this;

        //To add OkHttp3 integration to Picasso https://github.com/JakeWharton/picasso2-okhttp3-downloader
        OkHttpClient client = new OkHttpClient.Builder().build();
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))
                .build();

        Picasso.setSingletonInstance(picasso);

    }

    public static Context getAppContext() {
        return appContext;
    }

}
