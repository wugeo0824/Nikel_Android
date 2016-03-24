package com.media2359.nickel.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.media2359.nickel.R;
import com.media2359.nickel.ui.camera.ImageInputHelper;
import com.media2359.nickel.ui.customview.ProfileField;

import java.io.File;

/**
 * Created by Xijun on 17/3/16.
 */
public class ProfileFragment extends BaseFragment implements ImageInputHelper.ImageActionListener {

    private ProfileField pfName, pfDOB, pfStreet, pfCity, pfPostal;
    private ImageView ivIDFront, ivIDBack;
    private ImageInputHelper imageInputHelper;
    private TextView tvIDFront, tvIDBack;
    private Spinner documentTypes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        imageInputHelper = new ImageInputHelper(this);
        imageInputHelper.setImageActionListener(this);

        pfName = (ProfileField) view.findViewById(R.id.pfDisplayName);
        pfDOB = (ProfileField) view.findViewById(R.id.pfDOB);
        pfStreet = (ProfileField) view.findViewById(R.id.pfStreetAddress);
        pfCity = (ProfileField) view.findViewById(R.id.pfCity);
        pfPostal = (ProfileField) view.findViewById(R.id.pfPostalCode);

        pfDOB.setShouldIntercept(true);

        ivIDFront = (ImageView) view.findViewById(R.id.ivIDFront);
        ivIDBack = (ImageView) view.findViewById(R.id.ivIDBack);
        ivIDFront.setOnClickListener(pickImageFront);
        ivIDBack.setOnClickListener(pickImageBack);

        tvIDFront = (TextView) view.findViewById(R.id.tvIDFront);
        tvIDBack = (TextView) view.findViewById(R.id.tvIDBack);
        documentTypes = (Spinner) view.findViewById(R.id.spinnerDocType);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.document_types, R.layout.item_spinner_entry);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        documentTypes.setAdapter(adapter);
    }

    private void showSelectionDialog(final boolean isFront) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Get your photo from Gallery?")
                .setPositiveButton("Yes, open Gallery now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageInputHelper.selectImageFromGallery(isFront);
                    }
                })
                .setNegativeButton("No, take from Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        imageInputHelper.takePhotoWithCamera(isFront);
                    }
                });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private View.OnClickListener pickImageFront = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showSelectionDialog(true);
        }
    };

    private View.OnClickListener pickImageBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showSelectionDialog(false);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onImageSelectedFromGallery(Uri uri, File imageFile, boolean isFront) {
        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageFile.getPath()), getResources().getDimensionPixelSize(R.dimen.card_holder_width), getResources().getDimensionPixelSize(R.dimen.card_holder_height));

        if (isFront) {
            ivIDFront.setImageBitmap(thumbImage);
            tvIDFront.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_ok, 0, 0, 0);
        } else {
            ivIDBack.setImageBitmap(thumbImage);
            tvIDBack.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
        }

    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile, boolean isFront) {
        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageFile.getPath()), getResources().getDimensionPixelSize(R.dimen.card_holder_width), getResources().getDimensionPixelSize(R.dimen.card_holder_height));
        if (isFront) {
            ivIDFront.setImageBitmap(thumbImage);
            tvIDFront.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
        } else {
            ivIDBack.setImageBitmap(thumbImage);
            tvIDBack.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
        }
    }

//    @Override
//    public void onImageCropped(Uri uri, File imageFile) {
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInputHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.my_profile);
    }


}
