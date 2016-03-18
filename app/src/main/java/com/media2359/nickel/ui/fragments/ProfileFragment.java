package com.media2359.nickel.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.media2359.nickel.R;
import com.media2359.nickel.ui.camera.ImageInputHelper;

import java.io.File;

/**
 * Created by Xijun on 17/3/16.
 */
public class ProfileFragment extends BaseFragment implements ImageInputHelper.ImageActionListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //initViews(view);
        return view;
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile) {

    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile) {

    }

    @Override
    public void onImageCropped(Uri uri, File imageFile) {

    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.my_profile);
    }


}
