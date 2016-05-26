package com.media2359.nickel.fragments;

import android.app.Activity;
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
import com.media2359.nickel.model.MyProfile;
import com.media2359.nickel.ui.customview.ProfileField;
import com.media2359.nickel.utils.BitmapUtils;
import com.media2359.nickel.utils.Const;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

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
//        documentTypes = (DocTypeSpinner) view.findViewById(R.id.spinnerDocType);

//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
//                R.array.document_types, R.layout.item_spinner_entry);
//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        documentTypes.setAdapter(adapter);

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
        MyProfile.Builder builder = new MyProfile.Builder();
        builder.withFullName(pfName.getInput()).withDOB(pfDOB.getInput()).withStreetAddress(pfStreet.getInput())
                .withCity(pfCity.getInput()).withPostalCode(pfPostal.getInput())
                .withDocumentType(pfDocTypes.getInput())
                .withDocumentID(pfDocumentID.getInput()).withFrontPhotoUrl(frontPhotoUrl).withBackPhotoUrl(backPhotoUrl)
                .buildAndSave(getContext());

        Toast.makeText(getActivity(), "Profile Saved", Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new OnProfileChangedEvent());
        getActivity().onBackPressed();
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
        myProfile = MyProfile.getCurrentProfile(getActivity());

        if (myProfile == null) {
            changeProfileStatus(MyProfile.STATUS_EMPTY);
        } else {
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
//        documentTypes.setSelection(myProfile.getDocumentType());
//        documentTypes.setEnabled(false);
        pfDocTypes.setInputAndLock(myProfile.getDocumentType());

        Picasso.with(getActivity()).load(frontPhotoUrl).fit().centerCrop().placeholder(R.drawable.id_missing_front).into(ivIDFront);
        Picasso.with(getActivity()).load(backPhotoUrl).fit().centerCrop().placeholder(R.drawable.id_missing_back).into(ivIDBack);
        ivIDFront.setEnabled(false);
        ivIDFront.setClickable(false);
        ivIDBack.setEnabled(false);
        ivIDBack.setClickable(false);
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
                tvStatus.setText(getString(R.string.profile_status_pending));
                tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_alert, 0, 0, 0);
                break;
            case MyProfile.STATUS_APPROVED:
                flStatus.setVisibility(View.VISIBLE);
                tvStatus.setText(getString(R.string.profile_status_approved));
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


}
