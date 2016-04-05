package com.media2359.nickel.ui.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.ui.MainActivity;
import com.media2359.nickel.ui.camera.ImageInputHelper;
import com.media2359.nickel.ui.customview.ProfileField;
import com.media2359.nickel.utils.PreferrencesUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Xijun on 17/3/16.
 */
public class ProfileFragment extends BaseFragment implements ImageInputHelper.ImageActionListener {

    private static final String TAG = "ProfileFragment";

    private ProfileField pfName, pfDOB, pfStreet, pfCity, pfPostal, pfDocumentID;
    private ImageView ivIDFront, ivIDBack;
    private ImageInputHelper imageInputHelper;
    private FrameLayout flStatus;
    private TextView tvIDFront, tvIDBack, tvStatus;
    private Button btnSaveChanges;
    private Spinner documentTypes;
    private MyProfile myProfile;
    private String frontPhotoUrl, backPhotoUrl;

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
        pfDocumentID = (ProfileField) view.findViewById(R.id.pfDocumentID);

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

        flStatus = (FrameLayout) view.findViewById(R.id.flProfileStatus);
        tvStatus = (TextView) view.findViewById(R.id.tvProfileStatus);
        btnSaveChanges = (Button) view.findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(OnSaveProfileClick);
    }

    private View.OnClickListener OnSaveProfileClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validFields())
                saveChanges();
        }
    };

    private boolean validFields(){

        if (!pfName.validateInput()){
            pfName.requestFocus();
            return false;
        }

        if (!pfDOB.validateInput()){
            pfDOB.requestFocus();
            return false;
        }

        if (!pfStreet.validateInput()){
            pfStreet.requestFocus();
            return false;
        }

        if (!pfCity.validateInput()){
            pfCity.requestFocus();
            return false;
        }

        if (!pfPostal.validateInput()){
            pfPostal.requestFocus();
            return false;
        }

        if (documentTypes.getSelectedItemPosition() <= 0){
            documentTypes.requestFocus();
            return false;
        }

        if (!pfDocumentID.validateInput()){
            pfDocumentID.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(frontPhotoUrl) || TextUtils.isEmpty(backPhotoUrl)){
            ivIDFront.requestFocus();
            return false;
        }

        return true;

    }

    private void saveChanges() {
        MyProfile.Builder builder = new MyProfile.Builder();
        builder.withFullName(pfName.getInput()).withDOB(pfDOB.getInput()).withStreetAddress(pfStreet.getInput())
                .withCity(pfCity.getInput()).withPostalCode(pfPostal.getInput()).withDocumentType(documentTypes.getSelectedItemPosition())
                .withDocumentID(pfDocumentID.getInput()).withFrontPhotoUrl(frontPhotoUrl).withBackPhotoUrl(backPhotoUrl)
                .build(getContext());

        Toast.makeText(getActivity(), "Profile Saved", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
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

    @Override
    public void onResume() {
        super.onResume();
        getMyProfile();
    }

    private void getMyProfile() {
        //TODO get my profile
        myProfile = MyProfile.getCurrentProfile(getActivity());

        if (myProfile == null){
            changeProfileStatus(MyProfile.STATUS_EMPTY);
        }else{
            frontPhotoUrl = myProfile.getFrontPhotoUrl();
            backPhotoUrl = myProfile.getBackPhotoUrl();
            fillInMyProfile();
            btnSaveChanges.setText("Resubmit");
            changeProfileStatus(MyProfile.STATUS_PENDING);
        }
    }

    private void fillInMyProfile() {
        pfName.setInputAndLock(myProfile.getFullName());
        pfDOB.setInputAndLock(myProfile.getDateOfBirth());
        pfCity.setInputAndLock(myProfile.getCity());
        pfStreet.setInputAndLock(myProfile.getStreetAddress());
        pfDocumentID.setInputAndLock(myProfile.getDocumentID());
        pfPostal.setInputAndLock(myProfile.getPostalCode());
        documentTypes.setSelection(myProfile.getDocumentType());
        documentTypes.setEnabled(false);

        Picasso.with(getActivity()).load(frontPhotoUrl).fit().centerCrop().placeholder(R.drawable.id_missing_front).into(ivIDFront);
        Picasso.with(getActivity()).load(backPhotoUrl).fit().centerCrop().placeholder(R.drawable.id_missing_back).into(ivIDBack);
        ivIDFront.setEnabled(false);
        ivIDFront.setClickable(false);
        ivIDBack.setEnabled(false);
        ivIDBack.setClickable(false);
    }

    private void changeProfileStatus(int status){
        switch (status){
            case MyProfile.STATUS_EMPTY:
                flStatus.setVisibility(View.GONE);
                break;
            case MyProfile.STATUS_PENDING:
                flStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(getString(R.string.profile_status_pending));
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_alert,0,0,0);
                break;
            case MyProfile.STATUS_APPROVED:
                flStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(getString(R.string.profile_status_approved));
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_ok,0,0,0);
                break;
            default:
                flStatus.setVisibility(View.GONE);
                break;
        }
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
        Bitmap thumbImage = getThumbnail(imageFile);
        if (isFront) {
            ivIDFront.setImageBitmap(thumbImage);
            tvIDFront.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_ok, 0, 0, 0);
        } else {
            ivIDBack.setImageBitmap(thumbImage);
            tvIDBack.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
        }

        // TODO crop the image before upload

    }

    @Override
    public void onImageTakenFromCamera(Uri uri, File imageFile, boolean isFront) {

        Bitmap thumbImage = getThumbnail(imageFile);
        if (isFront) {
            frontPhotoUrl = uri.toString();
            Log.d(TAG, "onImageTakenFromCamera: front url is " + frontPhotoUrl);
            ivIDFront.setImageBitmap(thumbImage);
            tvIDFront.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
        } else {
            backPhotoUrl = uri.toString();
            Log.d(TAG, "onImageTakenFromCamera: back url is " + backPhotoUrl);
            ivIDBack.setImageBitmap(thumbImage);
            tvIDBack.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
        }
    }

//    @Override
//    public void onImageCropped(Uri uri, File imageFile) {
//
//    }

    private Bitmap getThumbnail(Uri uri){
        File imageFile = new File(uri.getPath());
        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageFile.getPath()), getResources().getDimensionPixelSize(R.dimen.card_holder_width), getResources().getDimensionPixelSize(R.dimen.card_holder_height));
        return thumbImage;
    }

    private Bitmap getThumbnail(File imageFile){
        Bitmap thumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageFile.getPath()), getResources().getDimensionPixelSize(R.dimen.card_holder_width), getResources().getDimensionPixelSize(R.dimen.card_holder_height));
        return thumbImage;
    }


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
