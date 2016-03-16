package com.media2359.nickel.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.ui.camera.ImageInputHelper;
import com.media2359.nickel.utils.PreferrencesUtils;

import java.io.File;

/**
 * This activity handles all the profile/recipient detail logic
 */
public class ProfileActivity extends AppCompatActivity implements ImageInputHelper.ImageActionListener {

    private static final String TAG = "ProfileActivity";

    private ImageInputHelper imageInputHelper;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
    }

    private void initViews() {

        imageInputHelper = new ImageInputHelper(this);
        imageInputHelper.setImageActionListener(this);

        toolbar = (Toolbar) findViewById(R.id.customAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_left_24dp));
        ((TextView) findViewById(R.id.tvTitle_appBar)).setText("My Profile");
        findViewById(R.id.btnCancel_appBar).setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }


    public void addPhoto(View view) {
        //imageInputHelper.selectImageFromGallery();
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {
        // cropping the selected image. crop intent will have aspect ratio 16/9 and result image
        // will have size 800x450
        imageInputHelper.requestCropImage(uri, 100, 100, 1, 1);
    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {
        // cropping the selected image. crop intent will have aspect ratio 16/9 and result image
        // will have size 800x450
        imageInputHelper.requestCropImage(uri, 100, 100, 1, 1);
    }

    @Override
    public void onImageCropped(Uri uri, File imageFile) {

        String base64image = "";
        Log.d(TAG, "IMAGE CROPPED!!!");

        String sUri = uri.toString();

        PreferrencesUtils.saveIDFront(ProfileActivity.this, sUri);

        try {
            base64image = new String(Base64.encode(org.apache.commons.io.FileUtils.readFileToByteArray(imageFile), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (base64image.trim().length() > 0) {
            if (progressDialog == null)
                progressDialog = new ProgressDialog(ProfileActivity.this);
            progressDialog.setMessage("Updating image...");
            progressDialog.show();
        }

        Log.d("", "Base64: " + base64image);

        //RequestHandler.changeAvatar(changAvatarResponseListener, base64image.trim());
    }


}
