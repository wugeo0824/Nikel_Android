package com.media2359.nickel.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.network.RequestHandler;
import com.media2359.nickel.utils.MistUtils;

/**
 * Created by Xijun on 13/7/16.
 */
public class UploadProfileAsyncTask extends AsyncTask<MyProfile, Void, Void> {

    @Override
    protected Void doInBackground(MyProfile... params) {
        MyProfile profile = params[0];

        if (profile.getFrontPhotoUri() == null)
            return null;

        String frontBase64 = MistUtils.getBase64FromUri(Uri.parse(profile.getFrontPhotoUri()));

        if (profile.getBackPhotoUri() == null)
            return null;

        String backBase64 = MistUtils.getBase64FromUri(Uri.parse(profile.getBackPhotoUri()));


        return null;
    }
}
