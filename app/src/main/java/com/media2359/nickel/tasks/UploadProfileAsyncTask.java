package com.media2359.nickel.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.network.RequestHandler;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Xijun on 13/7/16.
 */
public class UploadProfileAsyncTask extends AsyncTask<MyProfile, Void, Void> {

    @Override
    protected Void doInBackground(MyProfile... params) {
        MyProfile profile = params[0];

        if (profile.getFrontPhotoUri() == null)
            return null;

        File frontPic = new File(Uri.parse(profile.getFrontPhotoUri()).getPath());

        if (profile.getBackPhotoUri() == null)
            return null;

        File backPic = new File(Uri.parse(profile.getBackPhotoUri()).getPath());



        // create RequestBody instance from file
        RequestBody requestFront = RequestBody.create(MediaType.parse("image/jpeg"), frontPic);
        RequestBody requestBack = RequestBody.create(MediaType.parse("image/jpeg"), backPic);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part frontBody =
                MultipartBody.Part.createFormData("document1", frontPic.getName(), requestFront);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part backBody =
                MultipartBody.Part.createFormData("document2", backPic.getName(), requestBack);

        RequestHandler.updateProfile(profile, frontBody, backBody);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
