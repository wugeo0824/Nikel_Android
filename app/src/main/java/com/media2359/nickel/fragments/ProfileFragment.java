package com.media2359.nickel.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.media2359.nickel.R;
import com.media2359.nickel.activities.CaptureActivity;
import com.media2359.nickel.event.OnProfileChangedEvent;
import com.media2359.nickel.event.OnProfileLoadedEvent;
import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.network.RequestHandler;
import com.media2359.nickel.tasks.UploadProfileAsyncTask;
import com.media2359.nickel.ui.customview.ProfileField;
import com.media2359.nickel.utils.BitmapUtils;
import com.media2359.nickel.utils.Const;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

/**
 * Created by Xijun on 17/3/16.
 */
public class ProfileFragment extends BaseFragment {

    private static final String TAG = "ProfileFragment";

    private ProfileField pfName, pfDOB, pfStreet, pfCity, pfPostal, pfDocumentID, pfDocTypes;
    private ImageView ivIDFront, ivIDBack;
    private FrameLayout flStatus;
    private TextView tvIDFront, tvIDBack, tvStatus;
    private Button btnSaveChanges;
    private MyProfile myProfile;
    private String frontPhotoUrl, backPhotoUrl;
    private UploadProfileAsyncTask uploadProfileAsyncTask;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        pfName = (ProfileField) view.findViewById(R.id.pfDisplayName);
        pfDOB = (ProfileField) view.findViewById(R.id.pfDOB);
        pfStreet = (ProfileField) view.findViewById(R.id.pfStreetAddress);
        pfCity = (ProfileField) view.findViewById(R.id.pfCity);
        pfPostal = (ProfileField) view.findViewById(R.id.pfPostalCode);
        pfDocumentID = (ProfileField) view.findViewById(R.id.pfDocumentID);
        pfDocTypes = (ProfileField) view.findViewById(R.id.pfDocType);

        pfDOB.setShouldIntercept(true, ProfileField.POPUP_TYPE_CALENDAR);
        pfDocTypes.setShouldIntercept(true, ProfileField.POPUP_TYPE_LIST_DIALOG);

        ivIDFront = (ImageView) view.findViewById(R.id.ivIDFront);
        ivIDBack = (ImageView) view.findViewById(R.id.ivIDBack);
        ivIDFront.setOnClickListener(pickImageFront);
        ivIDBack.setOnClickListener(pickImageBack);

        tvIDFront = (TextView) view.findViewById(R.id.tvIDFront);
        tvIDBack = (TextView) view.findViewById(R.id.tvIDBack);

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

            Log.d(TAG, "onClick: " + validFields());
        }
    };

    private boolean validFields() {

        if (!pfName.validateInput()) {
            pfName.requestFocus();
            return false;
        }

        if (!pfDOB.validateInput()) {
            pfDOB.requestFocus();
            return false;
        }

        if (!pfStreet.validateInput()) {
            pfStreet.requestFocus();
            return false;
        }

        if (!pfCity.validateInput()) {
            pfCity.requestFocus();
            return false;
        }

        if (!pfPostal.validateInput()) {
            pfPostal.requestFocus();
            return false;
        }

//        if (!documentTypes.validateInput()) {
//            documentTypes.showSpinner();
//            return false;
//        }

        if (pfDocTypes.getInput().isEmpty()) {
            pfDocTypes.requestFocus();
            return false;
        }

        if (!pfDocumentID.validateInput()) {
            pfDocumentID.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(frontPhotoUrl) || TextUtils.isEmpty(backPhotoUrl)) {
            ivIDFront.requestFocus();
            Toast.makeText(getActivity(), "Please provide photos of your ID", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    private void saveChanges() {
        btnSaveChanges.setEnabled(false);
        MyProfile.Builder builder = new MyProfile.Builder();
        builder.withFullName(pfName.getInput()).withDOB(pfDOB.getInput()).withStreetAddress(pfStreet.getInput())
                .withCity(pfCity.getInput()).withPostalCode(pfPostal.getInput())
                .withDocumentType(pfDocTypes.getInput())
                .withDocumentID(pfDocumentID.getInput()).withFrontPhotoUrl(frontPhotoUrl).withBackPhotoUrl(backPhotoUrl)
                .buildAndSave(getContext());

        uploadProfileAsyncTask = new UploadProfileAsyncTask();
        uploadProfileAsyncTask.execute(MyProfile.getCurrentProfile(getContext()));

        progressDialog = ProgressDialog.show(getContext(), "Uploading", "Please wait...", true);

    }

    @Override
    public void onDestroy() {
        if (uploadProfileAsyncTask != null) {
            uploadProfileAsyncTask.cancel(false);
        }
        super.onDestroy();
    }

    //    private void showSelectionDialog(final boolean isFront) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setTitle("Get your photo from Gallery?")
//                .setPositiveButton("Yes, open Gallery now", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        imageInputHelper.selectImageFromGallery(isFront);
//                    }
//                })
//                .setNegativeButton("No, take from Camera", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        imageInputHelper.takePhotoWithCamera(isFront);
//                    }
//                });
//
//        // Create the AlertDialog
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    @Override
    public void onResume() {
        super.onResume();
        getMyProfile();
    }

    private void getMyProfile() {
        //TODO get my profile
//        myProfile = MyProfile.getCurrentProfile(getActivity());
//
//        if (myProfile == null) {
//            changeProfileStatus(MyProfile.STATUS_EMPTY);
//        } else {
//            frontPhotoUrl = myProfile.getFrontPhotoUri();
//            backPhotoUrl = myProfile.getBackPhotoUri();
//            fillInMyProfile();
//            changeProfileStatus(MyProfile.STATUS_PENDING);
//        }

        progressDialog = ProgressDialog.show(getContext(), "", "Please wait...", true);
        RequestHandler.getProfile();
    }

    private void fillInMyProfile(int status) {
        if (status == MyProfile.STATUS_APPROVED){
            pfName.setInputAndLock(myProfile.getFullName());
            pfDOB.setInputAndLock(myProfile.getDateOfBirth());
            pfCity.setInputAndLock(myProfile.getCity());
            pfStreet.setInputAndLock(myProfile.getStreetAddress());
            pfDocumentID.setInputAndLock(myProfile.getDocumentID());
            pfPostal.setInputAndLock(myProfile.getPostalCode());
//        documentTypes.setSelection(myProfile.getDocumentType());
//        documentTypes.setEnabled(false);
            pfDocTypes.setInputAndLock(myProfile.getDocumentType());
            ivIDFront.setEnabled(false);
            ivIDFront.setClickable(false);
            ivIDBack.setEnabled(false);
            ivIDBack.setClickable(false);
        }else {
            pfName.setInput(myProfile.getFullName());
            pfDOB.setInput(myProfile.getDateOfBirth());
            pfCity.setInput(myProfile.getCity());
            pfStreet.setInput(myProfile.getStreetAddress());
            pfDocumentID.setInput(myProfile.getDocumentID());
            pfPostal.setInput(myProfile.getPostalCode());
            pfDocTypes.setInput(myProfile.getDocumentType());
            ivIDFront.setEnabled(true);
            ivIDFront.setClickable(true);
            ivIDBack.setEnabled(true);
            ivIDBack.setClickable(true);
        }


        Picasso.with(getActivity()).load(frontPhotoUrl).fit().centerCrop().placeholder(R.drawable.id_missing_front).into(ivIDFront);
        Picasso.with(getActivity()).load(backPhotoUrl).fit().centerCrop().placeholder(R.drawable.id_missing_back).into(ivIDBack);

        tvIDFront.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
        tvIDFront.setText("");
        tvIDBack.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
        tvIDBack.setText("");
    }

    private void changeProfileStatus(int status) {
        switch (status) {
            case MyProfile.STATUS_EMPTY:
                flStatus.setVisibility(View.GONE);
                break;
            case MyProfile.STATUS_PENDING:
                flStatus.setVisibility(View.VISIBLE);
                btnSaveChanges.setText("Resubmit");
                tvStatus.setText(getString(R.string.profile_status_pending));
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_alert, 0, 0, 0);
                break;
            case MyProfile.STATUS_APPROVED:
                flStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(getString(R.string.profile_status_approved));
                btnSaveChanges.setText("Resubmit");
                btnSaveChanges.setEnabled(false);
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_ok, 0, 0, 0);
                break;
            default:
                flStatus.setVisibility(View.GONE);
                break;
        }
    }


    private View.OnClickListener pickImageFront = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //showSelectionDialog(true);
            CaptureActivity.startCapturingIDCard(ProfileFragment.this, Const.REQUEST_PICTURE_FROM_CAMERA_FRONT);
        }
    };

    private View.OnClickListener pickImageBack = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //showSelectionDialog(false);
            CaptureActivity.startCapturingIDCard(ProfileFragment.this, Const.REQUEST_PICTURE_FROM_CAMERA_BACK);
        }
    };

    @Override
    public void onPause() {
        Picasso.with(getActivity()).cancelRequest(ivIDFront);
        Picasso.with(getActivity()).cancelRequest(ivIDBack);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Const.REQUEST_PICTURE_FROM_CAMERA_FRONT) && (resultCode == Activity.RESULT_OK)) {
            String filePath = data.getStringExtra(Const.DATA_PHOTO_FILE);
            File result = new File(filePath);

            Bitmap thumbImage = BitmapUtils.getThumbnail(getActivity(), result);
            frontPhotoUrl = Uri.fromFile(result).toString();
            //PreferencesUtils.saveIDFront(getContext(),frontPhotoUrl);
            Log.d(TAG, "onImageTakenFromCamera: front url is " + frontPhotoUrl);

            ivIDFront.setImageBitmap(thumbImage);
            tvIDFront.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
            tvIDFront.setText("");

        } else if ((requestCode == Const.REQUEST_PICTURE_FROM_CAMERA_BACK) && (resultCode == Activity.RESULT_OK)) {
            Log.d(TAG, "Image selected from camera");
            String filePath = data.getStringExtra(Const.DATA_PHOTO_FILE);
            File result = new File(filePath);


            Bitmap thumbImage = BitmapUtils.getThumbnail(getActivity(), result);
            backPhotoUrl = Uri.fromFile(result).toString();
            //PreferencesUtils.saveIDBack(getContext(),backPhotoUrl);

            Log.d(TAG, "onImageTakenFromCamera: back url is " + backPhotoUrl);
            ivIDBack.setImageBitmap(thumbImage);
            tvIDBack.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ico_ok), null, null, null);
            tvIDBack.setText("");
        }
    }

    @Override
    protected String getPageTitle() {
        return getString(R.string.my_profile);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(OnProfileChangedEvent onProfileChangedEvent) {

        btnSaveChanges.setEnabled(true);
        progressDialog.dismiss();

        if (onProfileChangedEvent.isSuccess()) {
            Toast.makeText(getActivity(), "Profile Saved", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        } else {
            Toast.makeText(getActivity(), onProfileChangedEvent.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Subscribe
    public void onEvent(OnProfileLoadedEvent onProfileLoadedEvent) {

        progressDialog.dismiss();

        if (onProfileLoadedEvent.isSuccess()) {
            myProfile = onProfileLoadedEvent.getProfile();
            int status = -1;

            if (myProfile != null) {
                status = myProfile.getStatusInt();
                frontPhotoUrl = myProfile.getDocument1();
                backPhotoUrl = myProfile.getDocument2();
            }

            if (status == MyProfile.STATUS_EMPTY) {
                changeProfileStatus(MyProfile.STATUS_EMPTY);

            } else if (status == MyProfile.STATUS_PENDING) {
//                frontPhotoUrl = myProfile.getDocument1();
//                backPhotoUrl = myProfile.getDocument2();
                fillInMyProfile(status);
                changeProfileStatus(MyProfile.STATUS_PENDING);

            } else if (status == MyProfile.STATUS_APPROVED) {
//                frontPhotoUrl = myProfile.getDocument1();
//                backPhotoUrl = myProfile.getDocument2();
                fillInMyProfile(status);
                changeProfileStatus(MyProfile.STATUS_APPROVED);
            }

            Log.d("MyProfile", "getStatusInt: " + onProfileLoadedEvent.isSuccess() + status);

        } else {
            changeProfileStatus(MyProfile.STATUS_EMPTY);
            Toast.makeText(getActivity(), onProfileLoadedEvent.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }
}
