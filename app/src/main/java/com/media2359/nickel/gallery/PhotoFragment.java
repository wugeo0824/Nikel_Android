package com.media2359.nickel.gallery;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Xijun on 27/7/16.
 */
public class PhotoFragment extends Fragment {

    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_RESOURCE = "resource";

    private int position = -1;

    @DrawableRes
    private int resourceId = -1;

    private ImageView ivPhoto;
    private TextView tvPosition;


    public static PhotoFragment newInstance(int position, @DrawableRes int resourceId) {

        Bundle args = new Bundle();
        args.putInt(EXTRA_POSITION, position);
        args.putInt(EXTRA_RESOURCE, resourceId);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_photo, container, false);
        ivPhoto = (ImageView) rootView.findViewById(R.id.ivPhoto);
        tvPosition = (TextView) rootView.findViewById(R.id.tvPosition);
        position = getArguments().getInt(EXTRA_POSITION);
        resourceId = getArguments().getInt(EXTRA_RESOURCE);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (position < 0 || resourceId < 0)
            return;

        Picasso.with(getContext()).load(resourceId).fit().centerInside().into(ivPhoto);
        //ivPhoto.setImageResource(resourceId);
        tvPosition.setText(position + "/10");

    }
}
